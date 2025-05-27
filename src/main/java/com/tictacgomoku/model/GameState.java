package com.tictacgomoku.model;

/**
 * 游戏状态类
 * 跟踪当前游戏的状态信息
 */
public class GameState {
    private Player currentPlayer;
    private Position activeGomokuPosition;  // 当前活跃的五子棋位置（正在进行井字棋的位置）
    private boolean canChooseFreely;        // 是否可以自由选择位置
    private Position lastTicTacToeMove;     // 上一步井字棋的位置
    private boolean gameStarted;
    
    /**
     * 构造函数，初始化游戏状态
     */
    public GameState() {
        currentPlayer = Player.BLACK;  // 黑方先手
        activeGomokuPosition = null;
        canChooseFreely = true;        // 游戏开始时可以自由选择
        lastTicTacToeMove = null;
        gameStarted = false;
    }
    
    /**
     * 获取当前玩家
     * @return 当前轮到的玩家
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * 设置当前玩家
     * @param player 要设置的玩家
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
    
    /**
     * 切换到下一个玩家
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer.getOpponent();
    }
    
    /**
     * 获取当前活跃的五子棋位置
     * @return 正在进行井字棋的五子棋位置
     */
    public Position getActiveGomokuPosition() {
        return activeGomokuPosition;
    }
    
    /**
     * 设置当前活跃的五子棋位置
     * @param position 要设置的位置
     */
    public void setActiveGomokuPosition(Position position) {
        this.activeGomokuPosition = position;
        this.canChooseFreely = (position == null);
    }
    
    /**
     * 检查是否可以自由选择位置
     * @return 如果可以自由选择返回true
     */
    public boolean canChooseFreely() {
        return canChooseFreely;
    }
    
    /**
     * 设置是否可以自由选择位置
     * @param canChooseFreely 是否可以自由选择
     */
    public void setCanChooseFreely(boolean canChooseFreely) {
        this.canChooseFreely = canChooseFreely;
        if (canChooseFreely) {
            this.activeGomokuPosition = null;
        }
    }
    
    /**
     * 获取上一步井字棋的位置
     * @return 上一步井字棋的位置
     */
    public Position getLastTicTacToeMove() {
        return lastTicTacToeMove;
    }
    
    /**
     * 设置上一步井字棋的位置
     * @param position 井字棋位置
     */
    public void setLastTicTacToeMove(Position position) {
        this.lastTicTacToeMove = position;
    }
    
    /**
     * 检查游戏是否已开始
     * @return 如果游戏已开始返回true
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    /**
     * 设置游戏是否已开始
     * @param gameStarted 游戏是否已开始
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    
    /**
     * 重置游戏状态
     */
    public void reset() {
        currentPlayer = Player.BLACK;
        activeGomokuPosition = null;
        canChooseFreely = true;
        lastTicTacToeMove = null;
        gameStarted = false;
    }
    
    /**
     * 创建游戏状态的副本
     * @return 游戏状态的副本
     */
    public GameState copy() {
        GameState copy = new GameState();
        copy.currentPlayer = this.currentPlayer;
        copy.activeGomokuPosition = this.activeGomokuPosition;
        copy.canChooseFreely = this.canChooseFreely;
        copy.lastTicTacToeMove = this.lastTicTacToeMove;
        copy.gameStarted = this.gameStarted;
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("游戏状态:\n");
        sb.append("  当前玩家: ").append(currentPlayer.getDisplayName()).append("\n");
        sb.append("  活跃位置: ").append(activeGomokuPosition != null ? activeGomokuPosition.toString() : "无").append("\n");
        sb.append("  可自由选择: ").append(canChooseFreely ? "是" : "否").append("\n");
        sb.append("  上次井字棋位置: ").append(lastTicTacToeMove != null ? lastTicTacToeMove.toString() : "无").append("\n");
        sb.append("  游戏已开始: ").append(gameStarted ? "是" : "否").append("\n");
        return sb.toString();
    }
}
