package com.tictacgomoku.model;

import java.util.Objects;

/**
 * 位置坐标类
 * 表示棋盘上的一个位置坐标
 */
public class Position {
    private final int row;
    private final int col;
    
    /**
     * 构造函数
     * @param row 行坐标
     * @param col 列坐标
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * 获取行坐标
     * @return 行坐标
     */
    public int getRow() {
        return row;
    }
    
    /**
     * 获取列坐标
     * @return 列坐标
     */
    public int getCol() {
        return col;
    }
    
    /**
     * 检查位置是否有效（在棋盘范围内）
     * @param maxRow 最大行数
     * @param maxCol 最大列数
     * @return 如果位置有效返回true，否则返回false
     */
    public boolean isValid(int maxRow, int maxCol) {
        return row >= 0 && row < maxRow && col >= 0 && col < maxCol;
    }
    
    /**
     * 根据方向偏移计算新位置
     * @param rowOffset 行偏移量
     * @param colOffset 列偏移量
     * @return 新的位置对象
     */
    public Position offset(int rowOffset, int colOffset) {
        return new Position(row + rowOffset, col + colOffset);
    }
    
    /**
     * 计算曼哈顿距离
     * @param other 另一个位置
     * @return 曼哈顿距离
     */
    public int manhattanDistance(Position other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}
