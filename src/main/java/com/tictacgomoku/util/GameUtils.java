package com.tictacgomoku.util;

import com.tictacgomoku.model.Position;
import com.tictacgomoku.model.Player;

/**
 * 游戏工具类
 * 提供各种辅助功能和工具方法
 */
public class GameUtils {
    
    /**
     * 私有构造函数，防止实例化
     */
    private GameUtils() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }
    
    /**
     * 根据像素坐标计算棋盘位置
     * @param x 像素x坐标
     * @param y 像素y坐标
     * @param boardSize 棋盘大小
     * @param cellSize 格子大小
     * @param margin 边距
     * @return 计算出的棋盘位置，如果无效返回null
     */
    public static Position pixelToPosition(int x, int y, int boardSize, int cellSize, int margin) {
        int col = (x - margin) / cellSize;
        int row = (y - margin) / cellSize;
        
        Position pos = new Position(row, col);
        if (pos.isValid(boardSize, boardSize)) {
            return pos;
        }
        return null;
    }
    
    /**
     * 将棋盘位置转换为像素坐标
     * @param position 棋盘位置
     * @param cellSize 格子大小
     * @param margin 边距
     * @return 像素坐标数组 [x, y]
     */
    public static int[] positionToPixel(Position position, int cellSize, int margin) {
        int x = position.getCol() * cellSize + margin;
        int y = position.getRow() * cellSize + margin;
        return new int[]{x, y};
    }
    
    /**
     * 计算两点之间的欧几里得距离
     * @param pos1 第一个位置
     * @param pos2 第二个位置
     * @return 欧几里得距离
     */
    public static double euclideanDistance(Position pos1, Position pos2) {
        int dx = pos1.getCol() - pos2.getCol();
        int dy = pos1.getRow() - pos2.getRow();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 检查位置是否在指定范围内
     * @param position 要检查的位置
     * @param topLeft 范围左上角
     * @param bottomRight 范围右下角
     * @return 如果在范围内返回true
     */
    public static boolean isInRange(Position position, Position topLeft, Position bottomRight) {
        return position.getRow() >= topLeft.getRow() &&
               position.getRow() <= bottomRight.getRow() &&
               position.getCol() >= topLeft.getCol() &&
               position.getCol() <= bottomRight.getCol();
    }
    
    /**
     * 创建位置的邻域（8-连通）
     * @param center 中心位置
     * @param boardSize 棋盘大小
     * @return 邻域位置数组
     */
    public static Position[] getNeighborhood(Position center, int boardSize) {
        Position[] neighbors = new Position[8];
        int count = 0;
        
        for (int[] direction : GameConstants.DIRECTIONS) {
            Position neighbor = center.offset(direction[0], direction[1]);
            if (neighbor.isValid(boardSize, boardSize)) {
                neighbors[count++] = neighbor;
            }
        }
        
        // 返回有效邻居的数组
        Position[] validNeighbors = new Position[count];
        System.arraycopy(neighbors, 0, validNeighbors, 0, count);
        return validNeighbors;
    }
    
    /**
     * 格式化游戏状态信息
     * @param currentPlayer 当前玩家
     * @param activePosition 活跃位置
     * @param canChooseFreely 是否可自由选择
     * @return 格式化的状态字符串
     */
    public static String formatGameStatus(Player currentPlayer, Position activePosition, boolean canChooseFreely) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(GameConstants.CURRENT_PLAYER_FORMAT, currentPlayer.getDisplayName()));
        sb.append(" | ");
        
        if (canChooseFreely) {
            sb.append(String.format(GameConstants.ACTIVE_BOARD_FORMAT, GameConstants.FREE_CHOICE));
        } else if (activePosition != null) {
            sb.append(String.format(GameConstants.ACTIVE_BOARD_FORMAT, activePosition.toString()));
        } else {
            sb.append(String.format(GameConstants.ACTIVE_BOARD_FORMAT, "无"));
        }
        
        return sb.toString();
    }
    
    /**
     * 格式化游戏结果信息
     * @param winner 获胜者
     * @param isDraw 是否平局
     * @return 格式化的结果字符串
     */
    public static String formatGameResult(Player winner, boolean isDraw) {
        if (isDraw) {
            return GameConstants.DRAW_MESSAGE;
        } else if (winner != null) {
            return String.format(GameConstants.WIN_MESSAGE_FORMAT, winner.getDisplayName());
        } else {
            return "游戏进行中...";
        }
    }
    
    /**
     * 验证游戏配置参数
     * @param gomokuSize 五子棋盘大小
     * @param ticTacToeSize 井字棋盘大小
     * @param winCondition 获胜条件
     * @return 如果配置有效返回true
     */
    public static boolean validateGameConfiguration(int gomokuSize, int ticTacToeSize, int winCondition) {
        return gomokuSize > 0 && 
               ticTacToeSize == 3 && 
               winCondition > 0 && 
               winCondition <= gomokuSize;
    }
    
    /**
     * 将井字棋位置转换为方向索引
     * @param ticTacToePos 井字棋位置
     * @return 方向索引（0-8）
     */
    public static int ticTacToePositionToDirectionIndex(Position ticTacToePos) {
        if (!ticTacToePos.isValid(3, 3)) {
            return -1;
        }
        return ticTacToePos.getRow() * 3 + ticTacToePos.getCol();
    }
    
    /**
     * 将方向索引转换为井字棋位置
     * @param directionIndex 方向索引（0-8）
     * @return 井字棋位置
     */
    public static Position directionIndexToTicTacToePosition(int directionIndex) {
        if (directionIndex < 0 || directionIndex >= 9) {
            return null;
        }
        return new Position(directionIndex / 3, directionIndex % 3);
    }
    
    /**
     * 检查两个位置是否相邻（8-连通）
     * @param pos1 第一个位置
     * @param pos2 第二个位置
     * @return 如果相邻返回true
     */
    public static boolean areAdjacent(Position pos1, Position pos2) {
        int rowDiff = Math.abs(pos1.getRow() - pos2.getRow());
        int colDiff = Math.abs(pos1.getCol() - pos2.getCol());
        return rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);
    }
    
    /**
     * 计算位置的哈希值（用于调试和日志）
     * @param position 位置
     * @return 哈希值字符串
     */
    public static String positionHash(Position position) {
        return String.format("%02d%02d", position.getRow(), position.getCol());
    }
}
