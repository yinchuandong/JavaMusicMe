package Ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;

import main.PlayBack;

import jmp123.decoder.Header;

import Service.PlayService;

public class IndexFrame extends JFrame {

	private JPanel contentPanel;
	private BottomPanel bottomPanel;
	
	private PlayService playService;
	private MainPanel mainPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexFrame frame = new IndexFrame();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IndexFrame() {
		this.playService = new PlayService(this);
		this.initCompents();
	}
	
	private void initCompents(){
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 482, 570);
		contentPanel = new JPanel();
		contentPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		setContentPane(contentPanel);
		
		//设置背景图片和icon
		Toolkit toolkit = getToolkit();
		Image iconImg = toolkit.getImage("img/appicon.jpg");
		setIconImage(iconImg);
		contentPanel.setLayout(null);
		
		mainPanel = new MainPanel(this);
		mainPanel.setBounds(0, 0, 482, 460);
		contentPanel.add(mainPanel);
		
		bottomPanel = new BottomPanel(this);
		bottomPanel.setBounds(0, 459, 482, 73);
		contentPanel.add(bottomPanel);
	}
	
	public PlayService getPlayService(){
		return this.playService;
	}
	
	public void setPlayService(PlayService playService) {
		this.playService = playService;
		bottomPanel.setPlayService(playService);
	}

	public void initProgress(int totalSec){
		bottomPanel.initProgressSlider(totalSec);
	}
	
	public MainPanel getMainPanel(){
		return this.mainPanel;
	}

}
