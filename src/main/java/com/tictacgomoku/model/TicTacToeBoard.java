package com.tictacgomoku.model;

import com.tictacgomoku.util.GameConstants;

/**
 * 井字棋盘类
 * 表示一个3x3的井字棋盘
 */
public class TicTacToeBoard {
    private Player[][] board;
    private Player winner;
    private boolean isFinished;
    private int moveCount;
    
    /**
     * 构造函数，创建一个空的井字棋盘
     */
    public TicTacToeBoard() {
        board = new Player[GameConstants.TICTACTOE_BOARD_SIZE][GameConstants.TICTACTOE_BOARD_SIZE];
        winner = null;
        isFinished = false;
        moveCount = 0;
    }
    
    /**
     * 在指定位置下棋
     * @param position 下棋位置
     * @param player 下棋的玩家
     * @return 如果下棋成功返回true，否则返回false
     */
    public boolean makeMove(Position position, Player player) {
        if (!isValidMove(position)) {
            return false;
        }
        
        board[position.getRow()][position.getCol()] = player;
        moveCount++;
        
        // 检查是否获胜
        if (checkWin(position, player)) {
            winner = player;
            isFinished = true;
        } else if (moveCount == 9) {
            // 棋盘已满，平局
            isFinished = true;
        }
        
        return true;
    }
    
    /**
     * 检查指定位置是否可以下棋
     * @param position 要检查的位置
     * @return 如果可以下棋返回true，否则返回false
     */
    public boolean isValidMove(Position position) {
        if (!position.isValid(GameConstants.TICTACTOE_BOARD_SIZE, GameConstants.TICTACTOE_BOARD_SIZE)) {
            return false;
        }
        return board[position.getRow()][position.getCol()] == null && !isFinished;
    }
    
    /**
     * 检查在指定位置下棋后是否获胜
     * @param position 下棋位置
     * @param player 下棋的玩家
     * @return 如果获胜返回true，否则返回false
     */
    private boolean checkWin(Position position, Player player) {
        int row = position.getRow();
        int col = position.getCol();
        
        // 检查行
        if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
            return true;
        }
        
        // 检查列
        if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
            return true;
        }
        
        // 检查主对角线
        if (row == col && board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        
        // 检查副对角线
        if (row + col == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 获取指定位置的玩家
     * @param position 位置
     * @return 该位置的玩家，如果为空返回null
     */
    public Player getPlayer(Position position) {
        if (!position.isValid(GameConstants.TICTACTOE_BOARD_SIZE, GameConstants.TICTACTOE_BOARD_SIZE)) {
            return null;
        }
        return board[position.getRow()][position.getCol()];
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
     * @return 如果游戏结束返回true，否则返回false
     */
    public boolean isFinished() {
        return isFinished;
    }
    
    /**
     * 检查是否是平局
     * @return 如果是平局返回true，否则返回false
     */
    public boolean isDraw() {
        return isFinished && winner == null;
    }
    
    /**
     * 获取当前下棋数量
     * @return 当前已下的棋子数量
     */
    public int getMoveCount() {
        return moveCount;
    }
    
    /**
     * 重置棋盘
     */
    public void reset() {
        board = new Player[GameConstants.TICTACTOE_BOARD_SIZE][GameConstants.TICTACTOE_BOARD_SIZE];
        winner = null;
        isFinished = false;
        moveCount = 0;
    }
    
    /**
     * 创建棋盘的副本
     * @return 棋盘的深拷贝
     */
    public TicTacToeBoard copy() {
        TicTacToeBoard copy = new TicTacToeBoard();
        for (int i = 0; i < GameConstants.TICTACTOE_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.TICTACTOE_BOARD_SIZE; j++) {
                copy.board[i][j] = this.board[i][j];
            }
        }
        copy.winner = this.winner;
        copy.isFinished = this.isFinished;
        copy.moveCount = this.moveCount;
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < GameConstants.TICTACTOE_BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.TICTACTOE_BOARD_SIZE; j++) {
                if (board[i][j] == null) {
                    sb.append("- ");
                } else {
                    sb.append(board[i][j].getSymbol()).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
