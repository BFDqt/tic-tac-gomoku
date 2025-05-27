package com.tictacgomoku.model;

import com.tictacgomoku.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * 五子棋盘类
 * 表示一个15x15的五子棋盘，每个位置对应一个井字棋盘
 */
public class GomokuBoard {
    private Player[][] board;
    private TicTacToeBoard[][] ticTacToeBoards;
    private Player winner;
    private boolean isFinished;
    private int moveCount;
    
    /**
     * 构造函数，创建一个空的五子棋盘
     */
    public GomokuBoard() {
        board = new Player[GameConstants.GOMOKU_BOARD_SIZE][GameConstants.GOMOKU_BOARD_SIZE];
        ticTacToeBoards = new TicTacToeBoard[GameConstants.GOMOKU_BOARD_SIZE][GameConstants.GOMOKU_BOARD_SIZE];
        
        // 初始化所有井字棋盘
        for (int i = 0; i < GameConstants.GOMOKU_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.GOMOKU_BOARD_SIZE; j++) {
                ticTacToeBoards[i][j] = new TicTacToeBoard();
            }
        }
        
        winner = null;
        isFinished = false;
        moveCount = 0;
    }
    
    /**
     * 在指定位置放置五子棋棋子（通过赢得井字棋获得）
     * @param position 位置
     * @param player 玩家
     * @return 如果成功放置返回true
     */
    public boolean placeStone(Position position, Player player) {
        if (!isValidPosition(position) || board[position.getRow()][position.getCol()] != null) {
            return false;
        }
        
        board[position.getRow()][position.getCol()] = player;
        moveCount++;
        
        // 检查是否获胜
        if (checkWin(position, player)) {
            winner = player;
            isFinished = true;
        }
        
        return true;
    }
    
    /**
     * 获取指定位置的井字棋盘
     * @param position 位置
     * @return 对应的井字棋盘
     */
    public TicTacToeBoard getTicTacToeBoard(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }
        return ticTacToeBoards[position.getRow()][position.getCol()];
    }
    
    /**
     * 获取指定位置的五子棋棋子
     * @param position 位置
     * @return 该位置的玩家，如果为空返回null
     */
    public Player getStone(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }
        return board[position.getRow()][position.getCol()];
    }
    
    /**
     * 检查位置是否有效
     * @param position 位置
     * @return 如果位置有效返回true
     */
    public boolean isValidPosition(Position position) {
        return position.isValid(GameConstants.GOMOKU_BOARD_SIZE, GameConstants.GOMOKU_BOARD_SIZE);
    }
    
    /**
     * 检查指定位置是否可以开始井字棋游戏
     * @param position 位置
     * @return 如果可以开始返回true
     */
    public boolean canStartTicTacToe(Position position) {
        if (!isValidPosition(position)) {
            return false;
        }
        
        // 如果该位置已经有五子棋棋子，则不能开始井字棋
        if (board[position.getRow()][position.getCol()] != null) {
            return false;
        }
        
        // 如果井字棋已经结束，则不能继续
        return !ticTacToeBoards[position.getRow()][position.getCol()].isFinished();
    }
    
    /**
     * 获取所有可以开始井字棋的位置
     * @return 可用位置的列表
     */
    public List<Position> getAvailablePositions() {
        List<Position> available = new ArrayList<>();
        for (int i = 0; i < GameConstants.GOMOKU_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.GOMOKU_BOARD_SIZE; j++) {
                Position pos = new Position(i, j);
                if (canStartTicTacToe(pos)) {
                    available.add(pos);
                }
            }
        }
        return available;
    }
    
    /**
     * 检查在指定位置放置棋子后是否获胜
     * @param position 位置
     * @param player 玩家
     * @return 如果获胜返回true
     */
    private boolean checkWin(Position position, Player player) {
        int row = position.getRow();
        int col = position.getCol();
        
        // 检查所有8个方向
        for (int[] direction : GameConstants.DIRECTIONS) {
            int count = 1; // 包括当前位置
            
            // 正方向检查
            int r = row + direction[0];
            int c = col + direction[1];
            while (r >= 0 && r < GameConstants.GOMOKU_BOARD_SIZE && 
                   c >= 0 && c < GameConstants.GOMOKU_BOARD_SIZE && 
                   board[r][c] == player) {
                count++;
                r += direction[0];
                c += direction[1];
            }
            
            // 反方向检查
            r = row - direction[0];
            c = col - direction[1];
            while (r >= 0 && r < GameConstants.GOMOKU_BOARD_SIZE && 
                   c >= 0 && c < GameConstants.GOMOKU_BOARD_SIZE && 
                   board[r][c] == player) {
                count++;
                r -= direction[0];
                c -= direction[1];
            }
            
            // 如果连续棋子数量达到获胜条件
            if (count >= GameConstants.WIN_CONDITION) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取获胜者
     * @return 获胜的玩家，如果没有获胜者返回null
     */
    public Player getWinner() {
        return winner;
    }
    
    /**
     * 检查游戏是否结束
     * @return 如果游戏结束返回true
     */
    public boolean isFinished() {
        return isFinished;
    }
    
    /**
     * 获取当前棋子数量
     * @return 当前已放置的棋子数量
     */
    public int getMoveCount() {
        return moveCount;
    }
    
    /**
     * 重置棋盘
     */
    public void reset() {
        board = new Player[GameConstants.GOMOKU_BOARD_SIZE][GameConstants.GOMOKU_BOARD_SIZE];
        
        // 重置所有井字棋盘
        for (int i = 0; i < GameConstants.GOMOKU_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.GOMOKU_BOARD_SIZE; j++) {
                ticTacToeBoards[i][j].reset();
            }
        }
        
        winner = null;
        isFinished = false;
        moveCount = 0;
    }
    
    /**
     * 检查是否是平局（理论上很难达到）
     * @return 如果是平局返回true
     */
    public boolean isDraw() {
        // 在井字五子棋中，平局的情况非常罕见
        // 只有当所有井字棋都结束且没有玩家获胜时才算平局
        if (isFinished && winner == null) {
            return true;
        }
        
        // 检查是否还有可用的井字棋位置
        return getAvailablePositions().isEmpty() && winner == null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("五子棋盘状态:\n");
        for (int i = 0; i < GameConstants.GOMOKU_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.GOMOKU_BOARD_SIZE; j++) {
                if (board[i][j] == null) {
                    sb.append("+ ");
                } else {
                    sb.append(board[i][j].getSymbol()).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
