package com.tictacgomoku;

import com.tictacgomoku.view.GameWindow;
import com.tictacgomoku.util.GameConstants;

import javax.swing.*;

/**
 * 井字五子棋游戏主程序
 * 程序入口点和应用程序启动类
 */
public class TicTacGomokuGame {
    
    /**
     * 应用程序主入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置系统属性，确保更好的UI显示
        setupSystemProperties();
        
        // 设置Look and Feel
        setupLookAndFeel();
        
        // 在事件分发线程中启动GUI
        SwingUtilities.invokeLater(() -> {
            try {
                startApplication();
            } catch (Exception e) {
                handleStartupError(e);
            }
        });
    }
    
    /**
     * 设置系统属性
     */
    private static void setupSystemProperties() {
        // 设置字体抗锯齿
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // 设置字体渲染质量
        System.setProperty("sun.java2d.renderer", "sun.java2d.marlin.MarlinRenderingEngine");
        
        // 设置应用程序名称
        System.setProperty("apple.awt.application.name", GameConstants.GAME_TITLE);
        
        // 启用硬件加速
        System.setProperty("sun.java2d.opengl", "true");
    }
      /**
     * 设置外观和感觉
     */
    private static void setupLookAndFeel() {
        try {            // 尝试使用系统默认的Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // 自定义UI属性
            customizeUIDefaults();
            
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("无法设置系统Look and Feel，使用默认样式: " + e.getMessage());
            
            try {
                // 如果系统Look and Feel失败，尝试使用Nimbus
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("无法设置Nimbus Look and Feel: " + ex.getMessage());
            }
        }
    }
    
    /**
     * 自定义UI默认设置
     */
    private static void customizeUIDefaults() {
        // 设置默认字体
        java.awt.Font defaultFont = new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 12);
        
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        
        // 设置颜色主题
        UIManager.put("Panel.background", java.awt.Color.WHITE);
        UIManager.put("Button.background", new java.awt.Color(240, 240, 240));
        UIManager.put("Button.foreground", java.awt.Color.BLACK);
        
        // 设置工具提示
        UIManager.put("ToolTip.font", defaultFont);
        UIManager.put("ToolTip.background", new java.awt.Color(255, 255, 225));
        UIManager.put("ToolTip.foreground", java.awt.Color.BLACK);
    }
    
    /**
     * 启动应用程序
     */
    private static void startApplication() {
        // 显示启动画面（可选）
        showSplashScreen();
        
        // 创建并显示主窗口
        GameWindow gameWindow = new GameWindow();
        gameWindow.showWindow();
        
        // 输出启动信息
        printStartupInfo();
    }
    
    /**
     * 显示启动画面
     */
    private static void showSplashScreen() {
        try {
            // 创建简单的启动画面
            JWindow splash = new JWindow();
            splash.setSize(400, 200);
            splash.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new java.awt.BorderLayout());
            panel.setBorder(BorderFactory.createRaisedBevelBorder());
            
            JLabel titleLabel = new JLabel(GameConstants.GAME_TITLE, SwingConstants.CENTER);
            titleLabel.setFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 24));
            
            JLabel subtitleLabel = new JLabel("正在加载游戏...", SwingConstants.CENTER);
            subtitleLabel.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 14));
            
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            
            panel.add(titleLabel, java.awt.BorderLayout.CENTER);
            panel.add(subtitleLabel, java.awt.BorderLayout.SOUTH);
            panel.add(progressBar, java.awt.BorderLayout.PAGE_END);
            
            splash.setContentPane(panel);
            splash.setVisible(true);
            
            // 显示启动画面1.5秒
            Timer timer = new Timer(1500, e -> splash.dispose());
            timer.setRepeats(false);
            timer.start();
            
        } catch (Exception e) {
            System.err.println("无法显示启动画面: " + e.getMessage());
        }
    }
    
    /**
     * 处理启动错误
     * @param e 异常
     */
    private static void handleStartupError(Exception e) {
        System.err.println("应用程序启动失败: " + e.getMessage());
        e.printStackTrace();
        
        // 显示错误对话框
        try {
            String errorMessage = "应用程序启动失败：\n" + e.getMessage() + 
                                 "\n\n请检查Java环境是否正确安装。";
            
            JOptionPane.showMessageDialog(
                null,
                errorMessage,
                "启动错误",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception dialogError) {
            System.err.println("无法显示错误对话框: " + dialogError.getMessage());
        }
        
        System.exit(1);
    }
    
    /**
     * 打印启动信息
     */
    private static void printStartupInfo() {
        System.out.println("========================================");
        System.out.println("  " + GameConstants.GAME_TITLE);
        System.out.println("========================================");
        System.out.println("版本: 1.0.0");
        System.out.println("Java版本: " + System.getProperty("java.version"));
        System.out.println("操作系统: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("JVM: " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
        System.out.println("启动时间: " + new java.util.Date());
        System.out.println("========================================");
        System.out.println("游戏已成功启动！");
        System.out.println("享受您的井字五子棋游戏体验！");
        System.out.println("========================================");
    }
    
    /**
     * 验证运行环境
     * @return 如果环境验证通过返回true
     */
    public static boolean validateEnvironment() {
        try {
            // 检查Java版本
            String javaVersion = System.getProperty("java.version");
            System.out.println("检测到Java版本: " + javaVersion);
            
            // 检查是否支持Swing
            Class.forName("javax.swing.JFrame");
            
            // 检查是否支持AWT
            if (java.awt.GraphicsEnvironment.isHeadless()) {
                System.err.println("错误：检测到无头环境，无法运行GUI应用程序");
                return false;
            }
            
            System.out.println("环境验证通过");
            return true;
            
        } catch (ClassNotFoundException e) {
            System.err.println("错误：未找到必需的GUI类库");
            return false;
        } catch (Exception e) {
            System.err.println("环境验证失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 静态初始化块
     */
    static {
        // 验证运行环境
        if (!validateEnvironment()) {
            System.err.println("环境验证失败，程序将退出");
            System.exit(1);
        }
    }
}
