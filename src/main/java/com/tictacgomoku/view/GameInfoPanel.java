package com.tictacgomoku.view;

import com.tictacgomoku.model.GameLogic;
import com.tictacgomoku.model.GameState;
import com.tictacgomoku.model.Player;
import com.tictacgomoku.model.Position;
import com.tictacgomoku.util.GameConstants;
import com.tictacgomoku.util.GameUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏信息面板
 * 显示游戏状态、当前玩家、游戏规则等信息
 */
public class GameInfoPanel extends JPanel {
    private GameLogic gameLogic;
    private JLabel currentPlayerLabel;
    private JLabel activeBoardLabel;
    private JLabel gameStatusLabel;
    private JLabel moveCountLabel;
    private JTextArea rulesArea;
    private JTextArea historyArea;
    
    /**
     * 构造函数
     * @param gameLogic 游戏逻辑
     */
    public GameInfoPanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        initializeComponents();
        setupLayout();
        updateDisplay();
    }
    
    /**
     * 初始化组件
     */
    private void initializeComponents() {
        // 当前玩家标签
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 活跃棋盘标签
        activeBoardLabel = new JLabel();
        activeBoardLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        activeBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 游戏状态标签
        gameStatusLabel = new JLabel();
        gameStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        gameStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 移动计数标签
        moveCountLabel = new JLabel();
        moveCountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        moveCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 游戏规则区域
        rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setBackground(getBackground());
        rulesArea.setText(getGameRulesText());
        
        // 游戏历史区域
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Courier New", Font.PLAIN, 10));
        historyArea.setBackground(Color.WHITE);
    }
      /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("游戏信息"));
        
        // 根据屏幕尺寸动态调整信息面板宽度
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int panelWidth = Math.min(250, screenSize.width / 6); // 最大250px，或屏幕宽度的1/6
        int panelHeight = Math.min(500, screenSize.height - 200); // 最大500px，适应屏幕高度
        
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setMinimumSize(new Dimension(200, 300)); // 设置最小尺寸
        
        // 状态信息面板
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 3, 3)); // 减小间距
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 减小边距
        statusPanel.add(currentPlayerLabel);
        statusPanel.add(activeBoardLabel);
        statusPanel.add(gameStatusLabel);
        statusPanel.add(moveCountLabel);
        
        // 规则面板 - 压缩高度
        JPanel rulesPanel = new JPanel(new BorderLayout());
        rulesPanel.setBorder(BorderFactory.createTitledBorder("游戏规则"));
        JScrollPane rulesScrollPane = new JScrollPane(rulesArea);
        int rulesHeight = Math.min(150, panelHeight / 3); // 动态调整规则区域高度
        rulesScrollPane.setPreferredSize(new Dimension(panelWidth - 20, rulesHeight));
        rulesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rulesPanel.add(rulesScrollPane, BorderLayout.CENTER);
        
        // 历史面板 - 压缩高度
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("游戏历史"));
        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        int historyHeight = Math.min(120, panelHeight / 4); // 动态调整历史区域高度
        historyScrollPane.setPreferredSize(new Dimension(panelWidth - 20, historyHeight));
        historyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);
        
        // 布局组装
        add(statusPanel, BorderLayout.NORTH);
        add(rulesPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.SOUTH);
        
        System.out.println("游戏信息面板尺寸: " + panelWidth + "x" + panelHeight + "px");
    }
    
    /**
     * 更新显示内容
     */
    public void updateDisplay() {
        GameState gameState = gameLogic.getGameState();
        
        // 更新当前玩家
        Player currentPlayer = gameState.getCurrentPlayer();
        currentPlayerLabel.setText(String.format(GameConstants.CURRENT_PLAYER_FORMAT, 
                                                 currentPlayer.getDisplayName()));
        currentPlayerLabel.setForeground(currentPlayer == Player.BLACK ? Color.BLACK : Color.BLUE);
        
        // 更新活跃棋盘
        Position activePos = gameState.getActiveGomokuPosition();
        if (gameState.canChooseFreely()) {
            activeBoardLabel.setText(String.format(GameConstants.ACTIVE_BOARD_FORMAT, 
                                                  GameConstants.FREE_CHOICE));
            activeBoardLabel.setForeground(Color.GREEN);
        } else if (activePos != null) {
            activeBoardLabel.setText(String.format(GameConstants.ACTIVE_BOARD_FORMAT, 
                                                  activePos.toString()));
            activeBoardLabel.setForeground(Color.ORANGE);
        } else {
            activeBoardLabel.setText(String.format(GameConstants.ACTIVE_BOARD_FORMAT, "无"));
            activeBoardLabel.setForeground(Color.GRAY);
        }
        
        // 更新游戏状态
        if (gameLogic.isGameOver()) {
            String resultText = GameUtils.formatGameResult(gameLogic.getWinner(), gameLogic.isDraw());
            gameStatusLabel.setText(resultText);
            gameStatusLabel.setForeground(Color.RED);
        } else {
            gameStatusLabel.setText("游戏进行中");
            gameStatusLabel.setForeground(Color.BLACK);
        }
        
        // 更新移动计数
        int totalMoves = gameLogic.getGomokuBoard().getMoveCount();
        moveCountLabel.setText(String.format("已下棋子：%d", totalMoves));
        
        repaint();
    }
    
    /**
     * 添加游戏历史记录
     * @param message 历史消息
     */
    public void addHistoryMessage(String message) {
        historyArea.append(message + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
    
    /**
     * 清空游戏历史
     */
    public void clearHistory() {
        historyArea.setText("");
    }
    
    /**
     * 获取游戏规则文本
     * @return 游戏规则文本
     */
    private String getGameRulesText() {
        return "井字五子棋游戏规则：\n\n" +
               "1. 游戏目标：在15×15的五子棋盘上连成5子获胜\n\n" +
               "2. 游戏机制：\n" +
               "   • 每个五子棋位置对应一个3×3井字棋\n" +
               "   • 通过赢得井字棋来在五子棋盘上落子\n" +
               "   • 井字棋的落子位置决定对手下一步的战场\n\n" +
               "3. 控制规则：\n" +
               "   • 井字棋位置映射到九个方向\n" +
               "   • 指向边界外或已占据位置时，可自由选择新战场\n" +
               "   • 平局的井字棋该位置保持为永久空位，双方都无法占有\n\n" +
               "4. 操作方式：\n" +
               "   • 绿色背景：当前可选/活跃的井字棋战场\n" +
               "   • 黄色背景：已完成的井字棋（有获胜者）\n" +
               "   • 灰色背景：平局的井字棋（永久空位）";
    }
    
    /**
     * 重置显示
     */
    public void resetDisplay() {
        clearHistory();
        updateDisplay();
    }
}
