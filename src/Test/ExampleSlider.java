package Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * 
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class ExampleSlider extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ExampleSlider() {
        // 设定布局器
        super(new BorderLayout());
        // 设定监听器
        ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSlider) {
                    System.out.println("刻度: "
                            + ((JSlider) e.getSource()).getValue());
                }
            }
        };
        //--------------------------------
        // 设定JSlider1
        JSlider s1 = new JSlider(0, 100, 0);
        // 注入自定义ui
        s1.setUI(new MySliderUI());
        // 主刻度
        s1.setMajorTickSpacing(10);
        // 次刻度
        s1.setMinorTickSpacing(5);
        // 设定为显示
//        s1.setPaintTicks(true);
//        s1.setPaintLabels(true);
        // 监听slider1
        s1.addChangeListener(listener);
        s1.setValue(50);
        
        //------------------------------------
        JSlider s2 = new JSlider(0, 100, 0);
        s2.setOrientation(JSlider.VERTICAL);
        // 使用MetalSliderUI为ui
        s2.setUI(new MySliderUI());
        s2.setValue(10);
        
        s2.addChangeListener(listener);

        //使用盒式容器
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(5));
        box.add(s1);
        box.add(Box.createVerticalStrut(5));
        box.add(s2);
        box.add(Box.createVerticalGlue());
        add(box, BorderLayout.CENTER);
        add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        //设定窗体大小
        setPreferredSize(new Dimension(500, 600));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createUI();
            }
        });
    }

    public static void createUI() {
        JFrame frame = new JFrame("音量刻度设置");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ExampleSlider());
        frame.setResizable(false);
        frame.pack();
        //居中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class MySliderUI extends javax.swing.plaf.metal.MetalSliderUI {
	
	private String ballColor;
	private String fillColor;
	private String unFillColor;
	
	public MySliderUI(){
		this("#282828", "#75A101", "#737373");
	}
	
	/**
	 * 带颜色的填充
	 * @param ballColor 如：#282828
	 * @param fillColor 如：#75A101
	 * @param unFillColor 如：#737373
	 */
	public MySliderUI(String ballColor, String fillColor, String unFillColor){
		this.ballColor = ballColor;
		this.fillColor = fillColor;
		this.unFillColor = unFillColor;
	}
    /**
     * 绘制指示物
     */
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //填充椭圆框为当前thumb位置
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width/2, thumbRect.height/2);
        //也可以帖图(利用鼠标事件转换image即可体现不同状态)
        //g2d.drawImage(image, thumbRect.x, thumbRect.y, thumbRect.width,thumbRect.height,null);
    }

    /** 
     * 绘制刻度轨迹
     */
    public void paintTrack(Graphics g) {
       
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
        	drawHorizontal(g);
        } else {
        	drawVertical(g);
        }
    }
    
    private void drawHorizontal(Graphics g){
    	 int ch, cw;
         Rectangle trackBounds = trackRect;
         Graphics2D g2 = (Graphics2D) g;
         ch = 3;
         cw = trackBounds.width;
         
         //内部整个view的偏移
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.translate(trackBounds.x, trackBounds.y + ch + 3);

         // 背景设为灰色
         g2.setPaint(Color.decode(unFillColor));
         g2.fillRect(0, -ch + 1 , cw + thumbRect.width/2, ch );

         int trackLeft = 0;
         int trackRight = 0;
         trackRight = trackRect.width;

         int middleOfThumb = 0;

         int fillLeft = 0;

         int fillRight = 0;

         //坐标换算
         middleOfThumb = thumbRect.x + (thumbRect.width / 2);
         middleOfThumb -= trackRect.x;

         if (!drawInverted()) {
            fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
            fillRight = middleOfThumb;
         } else {
            fillLeft = middleOfThumb;
            fillRight = !slider.isEnabled() ? trackRight - 1 : trackRight - 2;
         }
         // 设定左边背景
         g2.setPaint(Color.decode(fillColor));
         g2.fillRect(0, -ch + 1, fillRight - fillLeft + thumbRect.width/2, ch );

         //圆点的背景
         g2.setPaint(Color.decode(ballColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
         g2.translate(-trackBounds.x + thumbRect.width/2, -(trackBounds.y + ch + 1));
    }
    
    private void drawVertical(Graphics g){
    	 int ch, cw;
         Rectangle trackBounds = trackRect;
    	 Graphics2D g2 = (Graphics2D) g;
         ch = trackBounds.height;
         cw = 3;

         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON);
         g2.translate(trackBounds.x, trackBounds.y + ch);

         // 背景设为灰色
         g2.setPaint(Color.decode(fillColor));
         g2.fillRect(cw, -ch , cw, ch);

         int trackTop = 0;
         int trackBottom = 0;
         trackBottom = trackRect.height;

         int middleOfThumb = 0;
         int fillTop = 0;
         int fillBottom = 0;
         //坐标换算
         middleOfThumb = thumbRect.y + (thumbRect.height / 2);
         middleOfThumb -= trackRect.y;

         if (!drawInverted()) {
             fillTop = !slider.isEnabled() ? trackTop : trackTop + 1;
             fillBottom = middleOfThumb;
         } else {
             fillTop = middleOfThumb;
             fillBottom = !slider.isEnabled() ? trackBottom - 1 : trackBottom - 2;
         }
         // 设定上边背景
         g2.setPaint(Color.decode(unFillColor));
         g2.fillRect(cw, -ch, cw ,fillBottom - fillTop);

         //圆点的背景
         g2.setPaint(Color.decode(ballColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
         g2.translate(-trackBounds.x + 1, -(trackBounds.y + ch));
    }
}