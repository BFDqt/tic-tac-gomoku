package com.tictacgomoku.view;

import com.tictacgomoku.model.*;
import com.tictacgomoku.util.GameConstants;
import com.tictacgomoku.util.GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 棋盘面板
 * 显示整个游戏棋盘的主要可视化组件
 */
public class BoardPanel extends JPanel {
    private GameLogic gameLogic;
    private Map<Position, TicTacToePanel> ticTacToePanels;
    private Position selectedPosition;
      private static final int PANEL_SPACING = 2;
    private static final int BOARD_MARGIN = 20;
    private int ticTacToePanelSize;
    
    /**
     * 构造函数
     * @param gameLogic 游戏逻辑
     */
    public BoardPanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.ticTacToePanels = new HashMap<>();
        this.selectedPosition = null;
        
        calculateOptimalSizes();
        initializeLayout();
        createTicTacToePanels();
        setupMouseListener();
    }
      /**
     * 计算最优的面板尺寸
     */
    private void calculateOptimalSizes() {
        // 获取屏幕尺寸和DPI信息
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        // 获取系统DPI缩放因子
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        // 计算DPI缩放因子
        double dpiScale = gc.getDefaultTransform().getScaleX();
          // 更保守的尺寸计算，确保在小屏幕上也能完整显示
        // 基础尺寸改为屏幕最小边的1/25，减小面板尺寸
        int baseSize = Math.min(screenSize.width, screenSize.height) / 25;
        
        // 考虑游戏信息面板的宽度（约200-300px）和窗口边距
        int availableWidth = (int)(screenSize.width * 0.8) - 300; // 80%屏幕宽度减去信息面板
        int availableHeight = (int)(screenSize.height * 0.8) - 100; // 80%屏幕高度减去菜单和状态栏
        
        // 根据15x15网格计算每个井字棋面板的最大允许尺寸
        int maxPanelSizeByWidth = (availableWidth - 2 * BOARD_MARGIN - 14 * PANEL_SPACING) / 15;
        int maxPanelSizeByHeight = (availableHeight - 2 * BOARD_MARGIN - 14 * PANEL_SPACING) / 15;
        
        // 确保五子棋格子是严格的正方形：取宽度和高度限制的最小值
        int maxPanelSize = Math.min(maxPanelSizeByWidth, maxPanelSizeByHeight);
          // 选择较小的尺寸以确保在小屏幕上正常显示
        ticTacToePanelSize = Math.min(baseSize, maxPanelSize);
        
        // 设置最小和最大尺寸限制（增大10%）
        ticTacToePanelSize = Math.max(55, Math.min(ticTacToePanelSize, 132)); // 最小55px，最大132px
        
        // 应用DPI缩放
        ticTacToePanelSize = (int)(ticTacToePanelSize * dpiScale);
        
        // 强制确保正方形约束，确保每个五子棋格子都是正方形
        ticTacToePanelSize = ensureSquareSize(ticTacToePanelSize);
        
        System.out.println("计算的井字棋面板尺寸: " + ticTacToePanelSize + "px (正方形: " + ticTacToePanelSize + "x" + ticTacToePanelSize + ")");
    }
      /**
     * 初始化布局
     */
    private void initializeLayout() {
        setLayout(new GridLayout(GameConstants.GOMOKU_BOARD_SIZE, GameConstants.GOMOKU_BOARD_SIZE, 
                                PANEL_SPACING, PANEL_SPACING));
        setBackground(new Color(GameConstants.BOARD_COLOR));
          // 根据面板尺寸调整边距，小面板使用更小的边距
        int adaptiveBoardMargin = Math.max(10, ticTacToePanelSize / 8);
        setBorder(BorderFactory.createEmptyBorder(adaptiveBoardMargin, adaptiveBoardMargin, 
                                                 adaptiveBoardMargin, adaptiveBoardMargin));
          // 计算面板大小 - 确保是严格的正方形
        int calculatedSize = GameConstants.GOMOKU_BOARD_SIZE * ticTacToePanelSize +
                        (GameConstants.GOMOKU_BOARD_SIZE - 1) * PANEL_SPACING + 
                        2 * adaptiveBoardMargin;
        
        // 确保棋盘面板是严格的正方形 - 强制使用相同的尺寸
        int squareSize = ensureSquareSize(calculatedSize);
        setPreferredSize(new Dimension(squareSize, squareSize));
        setMinimumSize(new Dimension(squareSize / 2, squareSize / 2)); // 设置最小尺寸
        
        System.out.println("棋盘面板尺寸: " + squareSize + "x" + squareSize + "px (强制正方形)");
    }
      /**
     * 创建井字棋面板
     */
    private void createTicTacToePanels() {
        for (int row = 0; row < GameConstants.GOMOKU_BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.GOMOKU_BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                TicTacToeBoard board = gameLogic.getTicTacToeBoard(pos);
                
                TicTacToePanel panel = new TicTacToePanel(board, pos, gameLogic, ticTacToePanelSize);
                ticTacToePanels.put(pos, panel);
                add(panel);
            }
        }
    }
    
    /**
     * 设置鼠标监听器
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
            }
        });
    }
    
    /**
     * 处理棋盘点击事件
     * @param x 鼠标x坐标
     * @param y 鼠标y坐标
     */
    private void handleBoardClick(int x, int y) {
        if (gameLogic.isGameOver()) {
            return;
        }
        
        // 如果可以自由选择位置，允许选择五子棋位置
        if (gameLogic.getGameState().canChooseFreely()) {
            Position clickedPos = getPositionFromPixel(x, y);
            if (clickedPos != null && gameLogic.canMakeTicTacToeMove(clickedPos)) {
                selectGomokuPosition(clickedPos);
            }
        }
    }
    
    /**
     * 从像素坐标获取五子棋位置
     * @param x 像素x坐标
     * @param y 像素y坐标
     * @return 五子棋位置
     */
    private Position getPositionFromPixel(int x, int y) {
        // 考虑边距和间距
        int adjustedX = x - BOARD_MARGIN;
        int adjustedY = y - BOARD_MARGIN;
          if (adjustedX < 0 || adjustedY < 0) {
            return null;
        }
        
        int panelSize = ticTacToePanelSize + PANEL_SPACING;
        int col = adjustedX / panelSize;
        int row = adjustedY / panelSize;
        
        Position pos = new Position(row, col);
        if (pos.isValid(GameConstants.GOMOKU_BOARD_SIZE, GameConstants.GOMOKU_BOARD_SIZE)) {
            return pos;
        }
        return null;
    }
    
    /**
     * 选择五子棋位置
     * @param position 要选择的位置
     */
    private void selectGomokuPosition(Position position) {
        if (gameLogic.selectGomokuPosition(position)) {
            selectedPosition = position;
            updatePanelStates();
        }
    }
    
    /**
     * 更新所有面板的状态
     */
    public void updatePanelStates() {
        GameState gameState = gameLogic.getGameState();
        List<Position> availablePositions = gameLogic.getAvailableGomokuPositions();
        Position activePosition = gameState.getActiveGomokuPosition();
          for (Map.Entry<Position, TicTacToePanel> entry : ticTacToePanels.entrySet()) {
            Position pos = entry.getKey();
            TicTacToePanel panel = entry.getValue();
            
            // 更新井字棋盘数据
            TicTacToeBoard board = gameLogic.getTicTacToeBoard(pos);
            panel.updateBoard(board);
            
            // 设置活跃状态
            boolean isActive;
            if (gameState.canChooseFreely()) {
                // 如果可以自由选择，所有可用位置都应该是活跃的
                isActive = availablePositions.contains(pos) && !gameLogic.isGameOver();
            } else {
                // 否则只有指定的活跃位置才是活跃的
                isActive = pos.equals(activePosition) && !gameLogic.isGameOver();
            }
            panel.setActive(isActive);
            
            // 设置高亮状态（可选择的位置）
            boolean isHighlighted = availablePositions.contains(pos) && 
                                   gameState.canChooseFreely() && 
                                   !gameLogic.isGameOver();
            panel.setHighlighted(isHighlighted);
        }
        
        repaint();
    }
    
    /**
     * 重置棋盘显示
     */
    public void resetBoard() {
        selectedPosition = null;
        updatePanelStates();
    }
    
    /**
     * 获取指定位置的井字棋面板
     * @param position 位置
     * @return 井字棋面板
     */
    public TicTacToePanel getTicTacToePanel(Position position) {
        return ticTacToePanels.get(position);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawGomokuStones(g2d);
        drawWinningLine(g2d);
        
        g2d.dispose();
    }
    
    /**
     * 绘制五子棋棋子
     * @param g2d 图形对象
     */
    private void drawGomokuStones(Graphics2D g2d) {
        GomokuBoard gomokuBoard = gameLogic.getGomokuBoard();
        
        for (int row = 0; row < GameConstants.GOMOKU_BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.GOMOKU_BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                Player player = gomokuBoard.getStone(pos);
                
                if (player != null) {
                    drawGomokuStone(g2d, pos, player);
                }
            }
        }
    }
      /**
     * 绘制单个五子棋棋子
     * @param g2d 图形对象
     * @param position 位置
     * @param player 玩家
     */
    private void drawGomokuStone(Graphics2D g2d, Position position, Player player) {
        // 计算棋子在面板上的位置，使用动态的面板尺寸
        int panelSizeWithSpacing = ticTacToePanelSize + PANEL_SPACING;
        int centerX = BOARD_MARGIN + position.getCol() * panelSizeWithSpacing + ticTacToePanelSize / 2;
        int centerY = BOARD_MARGIN + position.getRow() * panelSizeWithSpacing + ticTacToePanelSize / 2;        // 动态计算五子棋棋子尺寸，进一步减小尺寸确保不截断井字棋盘
        // 改为面板尺寸的30%，确保有足够空间显示井字棋
        int maxStoneSize = (int)(ticTacToePanelSize * 0.3);
        int stoneSize = Math.max(12, Math.min(maxStoneSize, 25)); // 最小12px，最大25px，不超过面板30%
        
        // 绘制棋子阴影
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(centerX - stoneSize/2 + 2, centerY - stoneSize/2 + 2, stoneSize, stoneSize);
        
        // 绘制棋子
        if (player == Player.BLACK) {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
            // 添加高光效果，尺寸按比例调整
            g2d.setColor(new Color(255, 255, 255, 100));
            int highlightSize = Math.max(8, stoneSize / 4);
            int highlightOffset = Math.max(5, stoneSize / 8);
            g2d.fillOval(centerX - stoneSize/2 + highlightOffset, centerY - stoneSize/2 + highlightOffset, 
                        highlightSize, highlightSize);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(Math.max(2, stoneSize / 20)));
            g2d.drawOval(centerX - stoneSize/2, centerY - stoneSize/2, stoneSize, stoneSize);
            // 添加高光效果，尺寸按比例调整
            g2d.setColor(new Color(255, 255, 255, 150));
            int highlightSize = Math.max(8, stoneSize / 4);
            int highlightOffset = Math.max(5, stoneSize / 8);
            g2d.fillOval(centerX - stoneSize/2 + highlightOffset, centerY - stoneSize/2 + highlightOffset, 
                        highlightSize, highlightSize);
        }
    }
    
    /**
     * 绘制获胜连线
     * @param g2d 图形对象
     */
    private void drawWinningLine(Graphics2D g2d) {
        if (gameLogic.isGameOver() && gameLogic.getWinner() != null) {
            // 这里可以添加获胜连线的绘制逻辑
            // 由于需要检测具体的获胜位置，暂时省略实现
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(5));
            // TODO: 实现获胜连线的绘制
        }
    }
    
    /**
     * 动态调整面板尺寸
     * 当窗口大小改变时调用此方法
     */
    public void adjustPanelSize() {
        calculateOptimalSizes();
        updatePanelSizes();
        revalidate();
        repaint();
    }
    
    /**
     * 更新所有井字棋面板的尺寸
     */
    private void updatePanelSizes() {
        for (TicTacToePanel panel : ticTacToePanels.values()) {
            panel.updatePanelSize(ticTacToePanelSize);
        }
        
        // 重新计算布局
        initializeLayout();
    }
    
    /**
     * 获取当前井字棋面板尺寸
     * @return 面板尺寸
     */
    public int getTicTacToePanelSize() {
        return ticTacToePanelSize;
    }
    
    /**
     * 强制确保面板尺寸是正方形
     * @param size 输入尺寸
     * @return 确保为正方形的尺寸
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
