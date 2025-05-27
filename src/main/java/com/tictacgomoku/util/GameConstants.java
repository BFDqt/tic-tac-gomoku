package com.tictacgomoku.util;

/**
 * 游戏常量类
 * 定义游戏中使用的各种常量
 */
public class GameConstants {
    
    // 棋盘尺寸常量
    public static final int GOMOKU_BOARD_SIZE = 15;  // 五子棋盘大小 15x15
    public static final int TICTACTOE_BOARD_SIZE = 3; // 井字棋盘大小 3x3
    public static final int WIN_CONDITION = 5;        // 五子棋获胜条件：连续5子
    
    // 方向向量：用于检查连线（8个方向）
    public static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},  // 左上、上、右上
        {0, -1},           {0, 1},    // 左、右
        {1, -1},  {1, 0},  {1, 1}     // 左下、下、右下
    };
    
    // 井字棋位置到五子棋方向的映射
    // 对应3x3井字棋盘的9个位置
    public static final int[][] TICTACTOE_TO_DIRECTION = {
        {-1, -1}, {-1, 0}, {-1, 1},  // 第一行：左上、上、右上
        {0, -1},  {0, 0},  {0, 1},   // 第二行：左、中、右
        {1, -1},  {1, 0},  {1, 1}    // 第三行：左下、下、右下
    };
    
    // UI常量
    public static final int CELL_SIZE = 40;           // 每个格子的像素大小
    public static final int BOARD_MARGIN = 50;       // 棋盘边距
    public static final int WINDOW_WIDTH = 1000;     // 窗口宽度
    public static final int WINDOW_HEIGHT = 800;     // 窗口高度
    
    // 颜色常量（RGB值）
    public static final int BOARD_COLOR = 0xDEB887;      // 棋盘颜色（浅棕色）
    public static final int LINE_COLOR = 0x000000;       // 线条颜色（黑色）
    public static final int BLACK_STONE_COLOR = 0x000000; // 黑子颜色
    public static final int WHITE_STONE_COLOR = 0xFFFFFF; // 白子颜色
    public static final int HIGHLIGHT_COLOR = 0xFF0000;   // 高亮颜色（红色）
    public static final int ACTIVE_COLOR = 0x00FF00;     // 活跃区域颜色（绿色）
    
    // 游戏状态常量
    public static final String GAME_TITLE = "井字五子棋 (Tic-Tac-Gomoku)";
    public static final String NEW_GAME_TEXT = "新游戏";
    public static final String RESET_TEXT = "重置";
    public static final String EXIT_TEXT = "退出";
    
    // 游戏结果消息
    public static final String WIN_MESSAGE_FORMAT = "%s 获胜！";
    public static final String DRAW_MESSAGE = "平局！";
    public static final String CURRENT_PLAYER_FORMAT = "当前玩家：%s";
    public static final String ACTIVE_BOARD_FORMAT = "活跃井字棋：%s";
    public static final String FREE_CHOICE = "自由选择";
    
    // 私有构造函数，防止实例化
    private GameConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }
}
