package com.tictacgomoku.model;

/**
 * 玩家枚举类
 * 表示游戏中的两个玩家
 */
public enum Player {
    BLACK('●', "黑方"),  // 先手方，使用黑色实心圆
    WHITE('○', "白方");  // 后手方，使用白色空心圆
    
    private final char symbol;
    private final String displayName;
    
    /**
     * 构造函数
     * @param symbol 玩家在棋盘上的符号
     * @param displayName 玩家的显示名称
     */
    Player(char symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }
    
    /**
     * 获取玩家符号
     * @return 玩家在棋盘上的符号
     */
    public char getSymbol() {
        return symbol;
    }
    
    /**
     * 获取玩家显示名称
     * @return 玩家的中文显示名称
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 获取对手玩家
     * @return 另一个玩家
     */
    public Player getOpponent() {
        return this == BLACK ? WHITE : BLACK;
    }
    
    @Override
    public String toString() {
        return displayName + "(" + symbol + ")";
    }
}
