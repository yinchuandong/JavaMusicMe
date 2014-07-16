package Ui;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import Model.Song;
import Service.PlayService;
import Service.RemoteService;
import Util.HttpUtil;
import Widget.AlphaLabel;
import Widget.AlphaPane;
import Widget.AlphaScrollPane;
import Widget.AlphaTextField;
import Widget.PlayList;
import Widget.PlayListItem;
import Widget.ScaleIcon;
import Widget.SearchList;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JList;

import jmp123.decoder.ID3Tag;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MainPanel extends JLayeredPane {
	private AlphaTextField textField;
	private AlphaLabel searchLabel;
	private SearchList searchList;
	private PlayList playList;
	private IndexFrame frame;
	private PlayService playService;
	private RemoteService remoteService;
	private JLabel bgLabel;
	private AlphaScrollPane searchPanel;
	private AlphaPane playListPane;
	private AlphaLabel deleteLabel;
	private AlphaLabel addLocalLabel;

	/**
	 * Create the panel.
	 */
	public MainPanel(JFrame frame) {
		this.frame = (IndexFrame)frame;
		this.initComponents();
		this.bindEvent();
		playService = this.frame.getPlayService();
		remoteService = new RemoteService(this);
	}
	
	private void initComponents(){
		setLayout(null);
		
		textField = new AlphaTextField();
		textField.setForeground(Color.decode("#000000"));
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textField.setBounds(29, 56, 354, 31);
		add(textField);
		textField.setColumns(10);
		
		searchLabel = new AlphaLabel("搜索");
		searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchLabel.setBackground(Color.decode("#75A101"));
		searchLabel.setBounds(388, 56, 54, 31);
		searchLabel.setOpaque(true);
		add(searchLabel);
		
		
		searchList = new SearchList();
		searchPanel = new AlphaScrollPane();
		searchPanel.setBounds(29, 97, 354, 300);
		searchPanel.setViewportView(searchList);
		searchPanel.setVisible(false);
		add(searchPanel, 10);
		
		playListPane = new AlphaPane();
		playListPane.setAlpha(0.9f);
		playListPane.setAlignmentX(0.5f);
		playListPane.setBounds(125, 100, 354, 373);
		playListPane.setBackground(Color.decode("#343434"));
		add(playListPane, new Integer(Integer.MAX_VALUE));
		playListPane.setLayout(null);
		
		
		bgLabel = new JLabel("");
		bgLabel.setBounds(0, 0, 484, 460);
		bgLabel.setBorder(null);
		ImageIcon bgImg = new ImageIcon("img/bg1.jpg");
		bgLabel.setIcon(bgImg);
		add(bgLabel);
		
		AlphaScrollPane scrollPane = new AlphaScrollPane();
		scrollPane.setAlpha(0.5f);
		scrollPane.setBounds(0, 36, 354, 323);
		playListPane.add(scrollPane);
		playListPane.setVisible(false);
		
		playList = new PlayList();
		scrollPane.setViewportView(playList);
		
		addLocalLabel = new AlphaLabel("搜索");
		addLocalLabel.setForeground(Color.WHITE);
		addLocalLabel.setText("添加");
		addLocalLabel.setOpaque(true);
		addLocalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addLocalLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		addLocalLabel.setBackground(new Color(117, 161, 1));
		addLocalLabel.setBounds(20, 0, 54, 36);
		playListPane.add(addLocalLabel);
		
		deleteLabel = new AlphaLabel("搜索");
		deleteLabel.setForeground(Color.WHITE);
		deleteLabel.setText("删除");
		deleteLabel.setOpaque(true);
		deleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		deleteLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		deleteLabel.setBackground(new Color(117, 161, 1));
		deleteLabel.setBounds(80, 0, 54, 36);
		playListPane.add(deleteLabel);
		
	}
	
	private void bindEvent(){
		searchLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				doSearch();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				searchLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				searchLabel.setForeground(Color.WHITE);
			}
			
		});
		
		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doSearch();
				}
			}
		});

		searchList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int clickCount = e.getClickCount();
				if (clickCount == 2) {
					PlayListItem item = searchList.getSelectedValue();
					remoteService.download(item);
					playList.append(item);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				searchPanel.setVisible(false);
			}
			
			

		});
		
		
		playList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int clickCount = e.getClickCount();
				if (clickCount == 2) {
					int index = playList.getSelectedIndex();
					if(!playService.isAlive() || playService.isInterrupt()){
						playService.setPlayList(playList);
						playService.startPlay(index);
						playService.start();
					}else{
						playService.startPlay(index);
					}
				}
			}

		});
		
		//删除按钮
		deleteLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int index = playList.getSelectedIndex();
				playList.removeItem(index);
				playList.savePlayList();
				playService.playNext();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				deleteLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				deleteLabel.setForeground(Color.WHITE);
			}
			
		});
		
		//添加本地音乐
		addLocalLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JFileChooser chooser = new JFileChooser();
//				FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3","dat","vob");
//				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("F:\\music"));//E:\\android\\windows\\Classification\\article
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File[] files = chooser.getSelectedFiles();
					for (File file : files) {
						String title = file.getName();
						title = title.substring(0, title.lastIndexOf("."));
						String path = file.getPath();
						PlayListItem item = new PlayListItem(title, null, path, null, true);
						playList.append(item);
					}
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				addLocalLabel.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				addLocalLabel.setForeground(Color.WHITE);
			}
			
		});
	}
	
	/**
	 * 实际搜索的事件
	 */
	private void doSearch(){
		String key = textField.getText();
		try {
			JSONArray resultJson = HttpUtil.get("http://www.xiami.com/web/search-songs?_xiamitoken=452ee4737d91d56cea00cb5a38860058&key=" + key);
			searchList.clear();
			for(int i=0; i<resultJson.size(); i++){
				JSONObject obj = resultJson.getJSONObject(i);
				String title = obj.getString("title");
				String author = obj.getString("author");
				String path = obj.getString("src");
				String cover = obj.getString("cover");
				PlayListItem playItem = new PlayListItem(title, author, path, cover, false);
				searchList.append(playItem);
			}
			searchPanel.setVisible(true);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public PlayList getPlayList(){
		return this.playList;
	}
	
	public AlphaPane getPlayListPanel(){
		return this.playListPane;
	}
	
	/**
	 * 初始化歌曲,如切换背景等
	 * @param item
	 * @param id3Tag
	 */
	public void initSong(PlayListItem item, ID3Tag id3Tag){
		//如果该歌曲没有被保存到本地，则将其下载
		if (item.getPath().startsWith("http")) {
			remoteService.download(item);
		}
		ImageIcon bgImg = null;
		if (item.getCover() == null) {
			bgImg = new ImageIcon("img/bg1.jpg");
			bgLabel.setIcon(new ScaleIcon(bgImg));
			return;
		}
		File file = new File(item.getCover());
		if (file.exists()) {
			bgImg = new ImageIcon(item.getCover());
		}else{
			bgImg = new ImageIcon("img/bg1.jpg");
		}
		bgLabel.setIcon(new ScaleIcon(bgImg));
	}
	
	/**
	 * 图片下载完成之后
	 * @param item
	 */
	public void onCoverComplete(PlayListItem item){
		playList.savePlayList();
	}
	
	/**
	 * 音乐下载完成之后
	 * @param item
	 */
	public void onSongComplete(PlayListItem item){
		playList.savePlayList();
	}
}
