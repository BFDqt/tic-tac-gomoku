package com.tictacgomoku.view;

import com.tictacgomoku.model.*;
import com.tictacgomoku.util.GameConstants;
import com.tictacgomoku.util.GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 井字棋面板
 * 显示单个井字棋盘的可视化组件
 */
public class TicTacToePanel extends JPanel {
    private TicTacToeBoard board;
    private Position gomokuPosition;
    private GameLogic gameLogic;
    private boolean isActive;
    private boolean isHighlighted;    private int panelSize;
    private int cellSize;
    private int margin;
    private int stoneSize;
    
    /**
     * 构造函数
     * @param board 井字棋盘
     * @param gomokuPosition 对应的五子棋位置
     * @param gameLogic 游戏逻辑
     * @param panelSize 面板大小
     */
    public TicTacToePanel(TicTacToeBoard board, Position gomokuPosition, GameLogic gameLogic, int panelSize) {
        this.board = board;
        this.gomokuPosition = gomokuPosition;
        this.gameLogic = gameLogic;
        this.isActive = false;
        this.isHighlighted = false;
        this.panelSize = panelSize;        // 根据面板大小动态计算组件尺寸，优化小面板的显示
        this.margin = Math.max(2, this.panelSize / 15); // 减小边距，最小2px
        this.cellSize = (this.panelSize - 2 * margin) / GameConstants.TICTACTOE_BOARD_SIZE;
        this.stoneSize = Math.max(cellSize * 3 / 4, 6); // 调整棋子大小，最小6px
        
        // 强制设置为正方形尺寸
        setPreferredSize(new Dimension(this.panelSize, this.panelSize));
        setMinimumSize(new Dimension(this.panelSize, this.panelSize));
        setMaximumSize(new Dimension(this.panelSize, this.panelSize));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        setupMouseListener();
    }
    
    /**
     * 兼容的构造函数（保持向后兼容）
     * @param board 井字棋盘
     * @param gomokuPosition 对应的五子棋位置
     * @param gameLogic 游戏逻辑
     */
    public TicTacToePanel(TicTacToeBoard board, Position gomokuPosition, GameLogic gameLogic) {
        this(board, gomokuPosition, gameLogic, 120); // 默认尺寸
    }
    
    /**
     * 设置鼠标监听器
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }    /**
     * 处理鼠标点击事件
     * @param x 鼠标x坐标
     * @param y 鼠标y坐标
     */
    private void handleMouseClick(int x, int y) {
        // 如果游戏结束，不处理点击
        if (gameLogic.isGameOver()) {
            return;
        }
        
        // 如果可以自由选择五子棋位置，则选择这个位置
        if (gameLogic.getGameState().canChooseFreely()) {
            if (gameLogic.canMakeTicTacToeMove(gomokuPosition)) {
                boolean success = gameLogic.selectGomokuPosition(gomokuPosition);
                if (success) {
                    // 通知父组件更新
                    Container parent = getParent();
                    if (parent != null) {
                        parent.repaint();
                    }
                }
            }
            return;
        }
        
        // 否则，如果这是活跃面板且井字棋未完成，处理井字棋内的点击
        if (!isActive || board.isFinished()) {
            return;
        }
        
        Position ticTacToePos = GameUtils.pixelToPosition(x, y, 
            GameConstants.TICTACTOE_BOARD_SIZE, cellSize, margin);
        
        if (ticTacToePos != null) {
            boolean success = gameLogic.makeMove(gomokuPosition, ticTacToePos);
            if (success) {
                repaint();
                // 通知父组件更新
                Container parent = getParent();
                if (parent != null) {
                    parent.repaint();
                }
            }
        }
    }
    
    /**
     * 设置是否为活跃状态
     * @param active 是否活跃
     */
    public void setActive(boolean active) {
        this.isActive = active;
        repaint();
    }
    
    /**
     * 设置是否高亮显示
     * @param highlighted 是否高亮
     */
    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
        repaint();
    }
    
    /**
     * 更新井字棋盘
     * @param newBoard 新的井字棋盘
     */
    public void updateBoard(TicTacToeBoard newBoard) {
        this.board = newBoard;
        repaint();
    }    /**
     * 动态更新面板尺寸
     * @param newPanelSize 新的面板尺寸
     */    public void updatePanelSize(int newPanelSize) {
        // 确保新面板尺寸是正方形
        newPanelSize = ensureSquareSize(newPanelSize);
        
        // 更新面板尺寸字段
        this.panelSize = newPanelSize;
        
        // 重新计算组件尺寸
        this.margin = Math.max(2, newPanelSize / 15);
        this.cellSize = (newPanelSize - 2 * margin) / GameConstants.TICTACTOE_BOARD_SIZE;
        this.stoneSize = Math.max(cellSize * 3 / 4, 6);
        
        // 确保设置的是严格正方形尺寸
        setPreferredSize(new Dimension(newPanelSize, newPanelSize));
        setMinimumSize(new Dimension(newPanelSize / 2, newPanelSize / 2));
        setMaximumSize(new Dimension(newPanelSize * 2, newPanelSize * 2));
        
        revalidate();
        repaint();
    }
      @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBackground(g2d);
        
        // 如果井字棋已完成，显示巨大的结果标记，否则显示正常的网格和棋子
        if (board.isFinished()) {
            drawLargeResult(g2d);
        } else {
            drawGrid(g2d);
            drawStones(g2d);
        }
        
        drawStatus(g2d);
        
        g2d.dispose();
    }
      /**
     * 绘制背景
     * @param g2d 图形对象
     */
    private void drawBackground(Graphics2D g2d) {
        Color bgColor = Color.WHITE;
        
        if (board.isFinished()) {
            // 已完成的井字棋使用更醒目的背景
            if (board.getWinner() != null) {
                // 有赢家时使用金色背景
                bgColor = new Color(255, 248, 220); // 浅金色
            } else {
                // 平局时使用银色背景
                bgColor = new Color(240, 240, 240); // 浅银色
            }
        } else if (isActive) {
            bgColor = new Color(200, 255, 200); // 浅绿色表示活跃
        } else if (isHighlighted) {
            bgColor = new Color(255, 255, 200); // 浅黄色表示高亮
        }
        
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // 为已完成的井字棋添加特殊边框
        if (board.isFinished()) {
            g2d.setColor(new Color(200, 150, 50)); // 金褐色边框
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
    }
      /**
     * 绘制网格线
     * @param g2d 图形对象
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        
        // 绘制垂直线
        for (int i = 1; i < GameConstants.TICTACTOE_BOARD_SIZE; i++) {
            int x = margin + i * cellSize;
            g2d.drawLine(x, margin, x, margin + GameConstants.TICTACTOE_BOARD_SIZE * cellSize);
        }
        
        // 绘制水平线
        for (int i = 1; i < GameConstants.TICTACTOE_BOARD_SIZE; i++) {
            int y = margin + i * cellSize;
            g2d.drawLine(margin, y, margin + GameConstants.TICTACTOE_BOARD_SIZE * cellSize, y);
        }
        
        // 绘制边框
        g2d.drawRect(margin, margin, 
            GameConstants.TICTACTOE_BOARD_SIZE * cellSize, 
            GameConstants.TICTACTOE_BOARD_SIZE * cellSize);
    }
      /**
     * 绘制棋子
     * @param g2d 图形对象
     */
    private void drawStones(Graphics2D g2d) {
        for (int row = 0; row < GameConstants.TICTACTOE_BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.TICTACTOE_BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                Player player = board.getPlayer(pos);
                
                if (player != null) {
                    int[] pixel = GameUtils.positionToPixel(pos, cellSize, margin);
                    int centerX = pixel[0] + cellSize / 2;
                    int centerY = pixel[1] + cellSize / 2;
                    
                    if (player == Player.BLACK) {
                        g2d.setColor(Color.BLACK);
                        g2d.fillOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
                    } else {
                        g2d.setColor(Color.WHITE);
                        g2d.fillOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
                        g2d.setColor(Color.BLACK);
                        g2d.drawOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
                    }
                }
            }
        }
    }
      /**
     * 绘制状态信息
     * @param g2d 图形对象
     */    private void drawStatus(Graphics2D g2d) {
        // 去除坐标标签显示以释放更多空间
        
        // 只有在井字棋未完成时才在底部显示状态文本
        // 完成时的状态由巨大标记本身表示
        if (board.isFinished()) {
            // 可以选择在底部显示简洁的状态
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("微软雅黑", Font.BOLD, Math.max(8, panelSize / 12)));
            FontMetrics fm = g2d.getFontMetrics();
            
            String statusText;
            if (board.getWinner() != null) {
                statusText = "✓"; // 简洁的完成标记
            } else {
                statusText = "="; // 平局标记
            }
            
            int textWidth = fm.stringWidth(statusText);
            int textX = (getWidth() - textWidth) / 2;
            int textY = getHeight() - 3;
            
            g2d.drawString(statusText, textX, textY);
        }
    }/**
     * 获取对应的五子棋位置
     * @return 五子棋位置
     */
    public Position getGomokuPosition() {
        return gomokuPosition;
    }
    
    /**
     * 绘制巨大的结果标记（当井字棋完成时）
     * @param g2d 图形对象
     */
    private void drawLargeResult(Graphics2D g2d) {
        Player winner = board.getWinner();
        
        // 计算巨大棋子的尺寸和位置
        int resultSize = Math.min(panelSize - 2 * margin, panelSize - 2 * margin) * 4 / 5; // 占据面板的80%
        int centerX = panelSize / 2;
        int centerY = panelSize / 2;
        
        if (winner != null) {
            // 有赢家：绘制巨大的赢方棋子
            if (winner == Player.BLACK) {
                // 黑棋获胜 - 绘制巨大黑子
                g2d.setColor(Color.BLACK);
                g2d.fillOval(centerX - resultSize/2, centerY - resultSize/2, resultSize, resultSize);
                
                // 添加高光效果
                g2d.setColor(new Color(255, 255, 255, 120));
                int highlightSize = resultSize / 4;
                g2d.fillOval(centerX - resultSize/3, centerY - resultSize/3, highlightSize, highlightSize);
            } else {
                // 白棋获胜 - 绘制巨大白子
                g2d.setColor(Color.WHITE);
                g2d.fillOval(centerX - resultSize/2, centerY - resultSize/2, resultSize, resultSize);
                
                // 黑色边框
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(centerX - resultSize/2, centerY - resultSize/2, resultSize, resultSize);
                
                // 添加高光效果
                g2d.setColor(new Color(200, 200, 200, 150));
                int highlightSize = resultSize / 4;
                g2d.fillOval(centerX - resultSize/3, centerY - resultSize/3, highlightSize, highlightSize);
            }
        } else {
            // 平局：绘制特殊标记
            drawDrawResult(g2d, centerX, centerY, resultSize);
        }
    }
    
    /**
     * 绘制平局结果标记
     * @param g2d 图形对象
     * @param centerX 中心X坐标
     * @param centerY 中心Y坐标
     * @param size 标记尺寸
     */
    private void drawDrawResult(Graphics2D g2d, int centerX, int centerY, int size) {
        // 绘制半黑半白的圆形表示平局
        int radius = size / 2;
        
        // 先绘制白色半圆（左半部分）
        g2d.setColor(Color.WHITE);
        g2d.fillArc(centerX - radius, centerY - radius, size, size, 90, 180);
        
        // 再绘制黑色半圆（右半部分）
        g2d.setColor(Color.BLACK);
        g2d.fillArc(centerX - radius, centerY - radius, size, size, 270, 180);
        
        // 绘制分割线
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(centerX, centerY - radius, centerX, centerY + radius);
        
        // 绘制外边框
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - radius, centerY - radius, size, size);
        
        // 在中央绘制"平"字或"="符号
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, Math.max(12, size / 6)));
        FontMetrics fm = g2d.getFontMetrics();
        String drawText = "平";
        int textWidth = fm.stringWidth(drawText);
        int textHeight = fm.getHeight();
        g2d.drawString(drawText, centerX - textWidth/2, centerY + textHeight/4);
    }
    
    /**
     * 确保面板尺寸为正方形
     * @param size 输入尺寸
     * @return 正方形尺寸
     */
    private int ensureSquareSize(int size) {
        // 确保尺寸是正数且为偶数，便于居中对齐
        size = Math.max(1, size);
        if (size % 2 != 0) {
            size++;
        }
        return size;
    }
}
