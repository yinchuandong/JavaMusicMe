package Ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.print.attribute.standard.Sides;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSlider;

import Service.PlayService;
import Widget.AlphaPane;
import Widget.PlayList;
import Widget.ScaleIcon;
import Widget.SliderUi;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import jmp123.decoder.Header;

import main.PlayBack;

public class BottomPanel extends JPanel {
	private JSlider progressSlider;
	private JLabel playLabel;
	private JLabel nextLabel;
	private JSlider volumeSlider;
	private JLabel preLabel;
	
	private IndexFrame frame;
	private PlayService playService;
	private PlayBack player;
	private PlayList playList;
	private Header header;
	private ProgessThread progressThread;
	
	private boolean isPaused = true;
	
	/**
	 * Create the panel.
	 */
	public BottomPanel(JFrame frame) {
		this.frame = (IndexFrame)frame;
		initComponents();
		initObjects();
		bindStyleEvent();
		bindProgressEvent();
	}
	
	private void initObjects(){
		this.playService = frame.getPlayService();
		this.progressThread = new ProgessThread();
		this.playList = frame.getMainPanel().getPlayList();
	}
	
	private void initComponents(){
		progressSlider = new JSlider();
		progressSlider.setBounds(10, 10, 400, 20);
		progressSlider.setBackground(Color.decode("#e8e8e1"));
		progressSlider.setValue(0);
		this.setBackground(Color.decode("#e8e8e1"));
		setLayout(null);
		add(progressSlider);
		
		playLabel = new JLabel("");
		playLabel.setBounds(70, 30, 40, 40);
		add(playLabel);
		ImageIcon playIcon = new ImageIcon("img/play.png");
		playLabel.setIcon(new ScaleIcon(playIcon));
		
		nextLabel = new JLabel("");
		nextLabel.setBounds(120, 30, 40, 40);
		ImageIcon nextIcon = new ImageIcon("img/next.png");
		nextLabel.setIcon(new ScaleIcon(nextIcon));
		add(nextLabel);
		
		preLabel = new JLabel("");
		preLabel.setBounds(20, 30, 40, 40);
		ImageIcon preIcon = new ImageIcon("img/pre.png");
		preLabel.setIcon(new ScaleIcon(preIcon));
		add(preLabel);
		
		volumeSlider = new JSlider();
		volumeSlider.setMinimum(-80);
		volumeSlider.setMaximum(6);
		volumeSlider.setValue(0);
		volumeSlider.setOrientation(JSlider.VERTICAL);
		volumeSlider.setUI(new Widget.SliderUi());
		progressSlider.setUI(new Widget.SliderUi());		
		volumeSlider.setBounds(420, 10, 20, 70);
		add(volumeSlider);
		
		showListLabel = new JLabel("");
		showListLabel.setBounds(173, 30, 40, 40);
		showListLabel.setIcon(new ScaleIcon(new ImageIcon("img/circle-hover.png")));
		add(showListLabel);
		
	}
	
	private void bindStyleEvent(){
		//播放、暂停按钮的事件和样式
		playLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				if (isPaused) {
					ImageIcon icon = new ImageIcon("img/play-hover.png");
					playLabel.setIcon(new ScaleIcon(icon));
				}else{
					ImageIcon icon = new ImageIcon("img/pause-hover.png");
					playLabel.setIcon(new ScaleIcon(icon));
				}
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				if (isPaused) {
					ImageIcon icon = new ImageIcon("img/play.png");
					playLabel.setIcon(new ScaleIcon(icon));
				}else{
					ImageIcon icon = new ImageIcon("img/pause.png");
					playLabel.setIcon(new ScaleIcon(icon));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (playList == null || playList.getCount() == 0) {
					return;
				}
				if (playService.isInterrupt() || !playService.isAlive()) {
					int index = playList.getSelectedIndex();
					index = index < 0 ? 0 : index;
					playService.setPlayList(playList);
					playService.startPlay(index);
					playService.start();
				}else{
					playService.pause();
				}
				if (isPaused) {
					isPaused = false;
					ImageIcon icon = new ImageIcon("img/pause.png");
					playLabel.setIcon(new ScaleIcon(icon));
					new ProgessThread().start();
				}else{
					isPaused = true;
					ImageIcon icon = new ImageIcon("img/play.png");
					playLabel.setIcon(new ScaleIcon(icon));
				}
			}
		});
		
		//下一曲的事件和样式
		nextLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				ImageIcon nextIcon = new ImageIcon("img/next-hover.png");
				nextLabel.setIcon(new ScaleIcon(nextIcon));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				ImageIcon nextIcon = new ImageIcon("img/next.png");
				nextLabel.setIcon(new ScaleIcon(nextIcon));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				playService.playNext();
			}
		});
		
		//上一首的事件和样式
		preLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				ImageIcon nextIcon = new ImageIcon("img/pre-hover.png");
				preLabel.setIcon(new ScaleIcon(nextIcon));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				ImageIcon nextIcon = new ImageIcon("img/pre.png");
				preLabel.setIcon(new ScaleIcon(nextIcon));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				playService.playPre();
			}
			
		});
		
		
		showListLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				AlphaPane playListPanel = frame.getMainPanel().getPlayListPanel();
				if (playListPanel.isVisible()) {
					playListPanel.setVisible(false);
				}else{
					playListPanel.setVisible(true);
				}
			}
		});
		
	}
	

	
	private boolean isDragged = false;
	private boolean isMouseDown = false;
	private JLabel showListLabel;
	
	/**
	 * 绑定进度条事件
	 */
	private void bindProgressEvent(){
		progressSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (isMouseDown) {
					isDragged = true;
				}
			}
		});
		progressSlider.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				isMouseDown = true;
				playService.pause();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseReleased(e);
				if (isDragged) {
					int elapse = progressSlider.getValue();
					player.seek(elapse);
					player.pause();
					isDragged = false;
				}
				isMouseDown = false;
			}
			
		});
	
		volumeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float volume = (float)volumeSlider.getValue();
				playService.setVolume(volume);
			}
		});
		
		volumeSlider.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int max = volumeSlider.getMaximum();
				int min = volumeSlider.getMinimum();
				int cur = volumeSlider.getValue();
				if (e.getWheelRotation() < 0) {
					volumeSlider.setValue(cur + 10 >= max ? max : cur + 10);
				}else{
					volumeSlider.setValue(cur - 10 <= min ? min : cur - 10);
				}
			}
		});
	}
	
	/**
	 * 总共的秒数
	 * @param totalSec
	 */
	public void initProgressSlider(int totalSec){
		//重新获得播放的音乐对象
		header = playService.getHeader();
		player = playService.getPlayer();
		progressSlider.setMinimum(0);
		progressSlider.setMaximum(totalSec);
		progressSlider.setValue(0);
		isPaused = false;
		ImageIcon icon = new ImageIcon("img/pause.png");
		playLabel.setIcon(new ScaleIcon(icon));
		new ProgessThread().start();
	}
	
	public void setPlayService(PlayService playService){
		this.playService = playService;
	}
	
	
	class ProgessThread extends Thread{
		
		@Override
		public void run(){
			while(!isPaused){
				try {
					int curSec = header.getElapse();
					progressSlider.setValue(curSec);
					Thread.sleep(1000);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		}
	}
	
}
