package com.tictacgomoku.model;

import com.tictacgomoku.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏逻辑控制器
 * 管理整个井字五子棋游戏的逻辑
 */
public class GameLogic {
    private GomokuBoard gomokuBoard;
    private GameState gameState;
    
    /**
     * 构造函数，初始化游戏
     */
    public GameLogic() {
        gomokuBoard = new GomokuBoard();
        gameState = new GameState();
    }
    
    /**
     * 开始新游戏
     */
    public void newGame() {
        gomokuBoard.reset();
        gameState.reset();
    }
    
    /**
     * 在井字棋盘上下棋
     * @param gomokuPosition 五子棋盘位置
     * @param ticTacToePosition 井字棋盘位置
     * @return 如果下棋成功返回true
     */
    public boolean makeMove(Position gomokuPosition, Position ticTacToePosition) {
        // 检查是否可以在该五子棋位置下井字棋
        if (!canMakeTicTacToeMove(gomokuPosition)) {
            return false;
        }
        
        TicTacToeBoard ticTacToeBoard = gomokuBoard.getTicTacToeBoard(gomokuPosition);
        if (ticTacToeBoard == null) {
            return false;
        }
        
        // 在井字棋盘上下棋
        if (!ticTacToeBoard.makeMove(ticTacToePosition, gameState.getCurrentPlayer())) {
            return false;
        }
          gameState.setGameStarted(true);
        gameState.setLastTicTacToeMove(ticTacToePosition);
        
        // 检查井字棋是否结束
        if (ticTacToeBoard.isFinished()) {
            if (ticTacToeBoard.getWinner() != null) {
                // 有获胜者，在五子棋盘上放置棋子
                gomokuBoard.placeStone(gomokuPosition, ticTacToeBoard.getWinner());
            }
        }
        
        // 每一步棋都要根据落子位置决定下一个战场
        determineNextPosition(gomokuPosition, ticTacToePosition);
        
        return true;
    }
    
    /**
     * 检查是否可以在指定五子棋位置进行井字棋游戏
     * @param gomokuPosition 五子棋位置
     * @return 如果可以返回true
     */
    public boolean canMakeTicTacToeMove(Position gomokuPosition) {
        // 如果游戏已结束，不能下棋
        if (gomokuBoard.isFinished()) {
            return false;
        }
        
        // 如果可以自由选择，检查位置是否可用
        if (gameState.canChooseFreely()) {
            return gomokuBoard.canStartTicTacToe(gomokuPosition);
        }
        
        // 如果不能自由选择，必须在指定位置下棋
        Position activePos = gameState.getActiveGomokuPosition();
        return activePos != null && activePos.equals(gomokuPosition) && 
               gomokuBoard.canStartTicTacToe(gomokuPosition);
    }
      /**
     * 根据井字棋的最后一步决定下一个五子棋位置
     * @param currentGomokuPos 当前五子棋位置
     * @param lastTicTacToePos 井字棋的最后一步位置
     */
    private void determineNextPosition(Position currentGomokuPos, Position lastTicTacToePos) {
        // 根据井字棋位置计算方向偏移
        int ticTacToeIndex = lastTicTacToePos.getRow() * 3 + lastTicTacToePos.getCol();
        int[] direction = GameConstants.TICTACTOE_TO_DIRECTION[ticTacToeIndex];
        
        // 计算下一个五子棋位置
        Position nextGomokuPos = currentGomokuPos.offset(direction[0], direction[1]);
        
        // 检查下一个位置是否有效且可用
        if (!gomokuBoard.isValidPosition(nextGomokuPos) || 
            !gomokuBoard.canStartTicTacToe(nextGomokuPos)) {
            // 位置无效或不可用，允许自由选择
            gameState.setCanChooseFreely(true);
        } else {
            // 正常情况，设置下一个活跃位置（包括中心位置指向当前位置的情况）
            gameState.setActiveGomokuPosition(nextGomokuPos);
        }
        
        gameState.switchPlayer();
    }    
    /**
     * 获取当前可以选择的五子棋位置
     * @return 可选位置列表
     */public List<Position> getAvailableGomokuPositions() {
        if (gomokuBoard.isFinished()) {
            return new ArrayList<>(); // 空列表
        }
        
        if (gameState.canChooseFreely()) {
            return gomokuBoard.getAvailablePositions();
        } else {
            Position activePos = gameState.getActiveGomokuPosition();
            if (activePos != null && gomokuBoard.canStartTicTacToe(activePos)) {
                List<Position> result = new ArrayList<>();
                result.add(activePos);
                return result;
            } else {
                return new ArrayList<>(); // 空列表
            }
        }
    }
    
    /**
     * 手动选择五子棋位置（在可自由选择时）
     * @param gomokuPosition 要选择的五子棋位置
     * @return 如果选择成功返回true
     */
    public boolean selectGomokuPosition(Position gomokuPosition) {
        if (!gameState.canChooseFreely()) {
            return false;
        }
        
        if (!gomokuBoard.canStartTicTacToe(gomokuPosition)) {
            return false;
        }
        
        gameState.setActiveGomokuPosition(gomokuPosition);
        return true;
    }
    
    /**
     * 获取五子棋盘
     * @return 五子棋盘对象
     */
    public GomokuBoard getGomokuBoard() {
        return gomokuBoard;
    }
    
    /**
     * 获取游戏状态
     * @return 游戏状态对象
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * 检查游戏是否结束
     * @return 如果游戏结束返回true
     */
    public boolean isGameOver() {
        return gomokuBoard.isFinished();
    }
    
    /**
     * 获取游戏获胜者
     * @return 获胜者，如果没有获胜者返回null
     */
    public Player getWinner() {
        return gomokuBoard.getWinner();
    }
    
    /**
     * 检查是否是平局
     * @return 如果是平局返回true
     */
    public boolean isDraw() {
        return gomokuBoard.isDraw();
    }
    
    /**
     * 获取指定五子棋位置的井字棋盘
     * @param gomokuPosition 五子棋位置
     * @return 对应的井字棋盘
     */
    public TicTacToeBoard getTicTacToeBoard(Position gomokuPosition) {
        return gomokuBoard.getTicTacToeBoard(gomokuPosition);
    }
    
    /**
     * 检查指定五子棋位置是否为当前活跃位置
     * @param gomokuPosition 五子棋位置
     * @return 如果是活跃位置返回true
     */
    public boolean isActivePosition(Position gomokuPosition) {
        Position activePos = gameState.getActiveGomokuPosition();
        return activePos != null && activePos.equals(gomokuPosition);
    }
}
