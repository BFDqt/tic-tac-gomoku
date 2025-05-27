package com.tictacgomoku.view;

import com.tictacgomoku.model.GameLogic;
import com.tictacgomoku.model.GameState;
import com.tictacgomoku.model.Player;
import com.tictacgomoku.util.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * 主游戏窗口
 * 整合所有游戏组件的主窗口
 */
public class GameWindow extends JFrame {
    private GameLogic gameLogic;
    private BoardPanel boardPanel;
    private GameInfoPanel infoPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JLabel statusBar;
      /**
     * 构造函数
     */
    public GameWindow() {
        gameLogic = new GameLogic();
        initializeComponents();
        setupLayout();
        setupMenuAndToolbar();
        setupEventHandlers();
        startGameUpdateTimer();
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle(GameConstants.GAME_TITLE);
        setResizable(true);
        
        // 设置自适应窗口大小
        setupAdaptiveWindow();
        
        pack();
        setLocationRelativeTo(null);
    }
      /**
     * 设置自适应窗口大小
     */
    private void setupAdaptiveWindow() {
        // 获取屏幕尺寸
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        // 计算合适的窗口大小（屏幕的75%，更保守）
        int windowWidth = (int)(screenSize.width * 0.75);
        int windowHeight = (int)(screenSize.height * 0.75);
        
        // 设置更小的最小窗口大小，适应小屏幕
        int minWidth = 600;  // 从800减少到600
        int minHeight = 500; // 从600减少到500
        
        // 确保窗口不会太小，但也不会太大
        windowWidth = Math.max(windowWidth, minWidth);
        windowHeight = Math.max(windowHeight, minHeight);
        
        // 添加最大尺寸限制，防止在大屏幕上过度拉伸
        int maxWidth = Math.min(1200, screenSize.width - 100);
        int maxHeight = Math.min(900, screenSize.height - 100);
        windowWidth = Math.min(windowWidth, maxWidth);
        windowHeight = Math.min(windowHeight, maxHeight);
        
        setMinimumSize(new Dimension(minWidth, minHeight));
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        
        System.out.println("窗口尺寸设置: " + windowWidth + "x" + windowHeight);
        System.out.println("屏幕尺寸: " + screenSize.width + "x" + screenSize.height);
    }
    
    /**
     * 初始化组件
     */
    private void initializeComponents() {
        boardPanel = new BoardPanel(gameLogic);
        infoPanel = new GameInfoPanel(gameLogic);
        statusBar = new JLabel("准备开始游戏");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(0, 25));
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 主游戏区域
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.EAST);
        
        // 整体布局
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        // 设置窗口图标
        try {
            setIconImage(createGameIcon());
        } catch (Exception e) {
            // 如果无法创建图标，忽略错误
        }
    }
    
    /**
     * 设置菜单栏和工具栏
     */
    private void setupMenuAndToolbar() {
        setupMenuBar();
        setupToolBar();
    }
    
    /**
     * 设置菜单栏
     */
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // 游戏菜单
        JMenu gameMenu = new JMenu("游戏(G)");
        gameMenu.setMnemonic('G');
        
        JMenuItem newGameItem = new JMenuItem(GameConstants.NEW_GAME_TEXT);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newGameItem.addActionListener(e -> startNewGame());
        
        JMenuItem resetItem = new JMenuItem(GameConstants.RESET_TEXT);
        resetItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        resetItem.addActionListener(e -> resetGame());
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem(GameConstants.EXIT_TEXT);
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> exitApplication());
        
        gameMenu.add(newGameItem);
        gameMenu.add(resetItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic('H');
        
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem rulesItem = new JMenuItem("游戏规则");
        rulesItem.addActionListener(e -> showRulesDialog());
        
        helpMenu.add(rulesItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * 设置工具栏
     */
    private void setupToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton newGameButton = new JButton(GameConstants.NEW_GAME_TEXT);
        newGameButton.setToolTipText("开始新游戏 (Ctrl+N)");
        newGameButton.addActionListener(e -> startNewGame());
        
        JButton resetButton = new JButton(GameConstants.RESET_TEXT);
        resetButton.setToolTipText("重置当前游戏 (Ctrl+R)");
        resetButton.addActionListener(e -> resetGame());
        
        toolBar.add(newGameButton);
        toolBar.add(resetButton);
        toolBar.addSeparator();
        
        // 添加当前玩家指示器
        JLabel playerIndicator = new JLabel("当前玩家: ");
        toolBar.add(playerIndicator);
        
        add(toolBar, BorderLayout.NORTH);
    }
      /**
     * 设置事件处理器
     */
    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        // 添加组件大小改变监听器，用于响应式布局
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // 当窗口大小改变时，动态调整棋盘面板尺寸
                SwingUtilities.invokeLater(() -> {
                    if (boardPanel != null) {
                        boardPanel.adjustPanelSize();
                    }
                });
            }
        });
    }
    
    /**
     * 启动游戏更新定时器
     */
    private void startGameUpdateTimer() {
        Timer updateTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameDisplay();
            }
        });
        updateTimer.start();
    }
    
    /**
     * 更新游戏显示
     */
    private void updateGameDisplay() {
        boardPanel.updatePanelStates();
        infoPanel.updateDisplay();
        updateStatusBar();
    }
    
    /**
     * 更新状态栏
     */
    private void updateStatusBar() {
        GameState gameState = gameLogic.getGameState();
        
        if (gameLogic.isGameOver()) {
            if (gameLogic.getWinner() != null) {
                statusBar.setText(String.format("游戏结束 - %s 获胜！", 
                                               gameLogic.getWinner().getDisplayName()));
            } else {
                statusBar.setText("游戏结束 - 平局");
            }
        } else if (!gameState.isGameStarted()) {
            statusBar.setText("点击任意位置开始游戏");
        } else {
            Player currentPlayer = gameState.getCurrentPlayer();
            if (gameState.canChooseFreely()) {
                statusBar.setText(String.format("%s 的回合 - 可自由选择五子棋位置", 
                                               currentPlayer.getDisplayName()));
            } else {
                statusBar.setText(String.format("%s 的回合 - 在活跃井字棋中下棋", 
                                               currentPlayer.getDisplayName()));
            }
        }
    }
    
    /**
     * 开始新游戏
     */
    private void startNewGame() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "确定要开始新游戏吗？当前游戏进度将丢失。",
            "新游戏",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            gameLogic.newGame();
            boardPanel.resetBoard();
            infoPanel.resetDisplay();
            infoPanel.addHistoryMessage("开始新游戏");
        }
    }
    
    /**
     * 重置游戏
     */
    private void resetGame() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "确定要重置当前游戏吗？",
            "重置游戏",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            gameLogic.newGame();
            boardPanel.resetBoard();
            infoPanel.resetDisplay();
            infoPanel.addHistoryMessage("游戏已重置");
        }
    }
    
    /**
     * 显示关于对话框
     */
    private void showAboutDialog() {
        String aboutText = "井字五子棋 (Tic-Tac-Gomoku)\n\n" +
                          "版本: 1.0.0\n" +
                          "一个创新的策略棋类游戏\n" +
                          "结合井字棋的局部战术和五子棋的全局战略\n\n" +
                          "开发环境: Java 11 + Swing\n" +
                          "构建工具: Maven\n\n" +
                          "© 2025 井字五子棋项目";
        
        JOptionPane.showMessageDialog(
            this,
            aboutText,
            "关于 " + GameConstants.GAME_TITLE,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * 显示游戏规则对话框
     */
    private void showRulesDialog() {
        String rulesText = "井字五子棋游戏规则\n\n" +
                          "游戏目标：\n" +
                          "在15×15的五子棋盘上率先连成5个棋子\n\n" +
                          "游戏机制：\n" +
                          "1. 每个五子棋位置对应一个3×3的井字棋\n" +
                          "2. 通过赢得井字棋来在五子棋盘上落子\n" +
                          "3. 井字棋的落子位置决定对手下一步的位置\n\n" +
                          "特殊规则：\n" +
                          "• 正中位置：允许自由选择下一个位置\n" +
                          "• 无效位置：被引导到边界外或已占据位置时可自由选择\n" +
                          "• 平局井字棋：该位置保持空白，继续游戏\n\n" +
                          "操作提示：\n" +
                          "• 绿色：当前活跃的井字棋\n" +
                          "• 黄色：可选择的五子棋位置\n" +
                          "• 蓝色：已完成的井字棋";
        
        JTextArea textArea = new JTextArea(rulesText);
        textArea.setEditable(false);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "游戏规则",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * 退出应用程序
     */
    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "确定要退出游戏吗？",
            "退出确认",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    /**
     * 创建游戏图标
     * @return 游戏图标
     */
    private Image createGameIcon() {
        // 创建一个简单的游戏图标
        int size = 32;
        Image icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) icon.getGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制背景
        g2d.setColor(new Color(GameConstants.BOARD_COLOR));
        g2d.fillRect(0, 0, size, size);
        
        // 绘制网格
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 1; i < 4; i++) {
            int pos = i * size / 4;
            g2d.drawLine(pos, 4, pos, size - 4);
            g2d.drawLine(4, pos, size - 4, pos);
        }
        
        // 绘制棋子
        g2d.setColor(Color.BLACK);
        g2d.fillOval(6, 6, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(20, 20, 6, 6);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(20, 20, 6, 6);
        
        g2d.dispose();
        return icon;
    }
    
    /**
     * 显示窗口
     */
    public void showWindow() {
        setVisible(true);
    }
}
