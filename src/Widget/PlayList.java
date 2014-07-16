package Widget;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicListUI;

import org.apache.http.auth.AUTH;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import Model.Song;

public class PlayList extends JList<PlayListItem>{

	private DefaultListModel<PlayListItem> dataListModel;
	private int curIndex = 0;
	private int nextIndex; //下一播放的文件
	
	public PlayList(){
		super();
		dataListModel = new DefaultListModel<PlayListItem>();
		setCellRenderer(new PlayListCellRenderer());
		setUI(new CustomerUI());
		setModel(dataListModel);
		setOpaque(false);
//		setBackground(Color.decode("#282828"));
		
		this.loadPlayList();
//		init();
	}
	
	private void init(){
		PlayListItem s1 = new PlayListItem("红豆", "F:\\music\\红豆.mp3");
		PlayListItem s2 = new PlayListItem("我们的歌","http://m5.file.xiami.com/906/906/4567/55552_3848085_l.mp3?auth_key=901abf685e747155403e46def3b97663-1405296000-0-null");
		PlayListItem s3 = new PlayListItem("红豆", "F:\\music\\红豆2.mp3");
		dataListModel.addElement(s1);
		dataListModel.addElement(s2);
		dataListModel.addElement(s3);
	}
	
	public synchronized void append(String title, String path) {
		dataListModel.addElement(new PlayListItem(title, path));
		savePlayList();
	}
	public synchronized void append(PlayListItem item) {
		dataListModel.addElement(item);
		savePlayList();
	}
	
	public int getCount(){
		return dataListModel.getSize();
	}
	
	/**
	 * 获取指定的列表项。
	 * @param index 列表项的索引。
	 * @return 列表项。
	 */
	public synchronized PlayListItem getPlayListItem(int index) {
		return (PlayListItem) dataListModel.get(index);
	}

	/**
	 * 从列表中删除指定项。
	 * @param index 将要删除的列表项的索引。
	 */
	public synchronized void removeItem(int index) {
		if(index < 0 || index >= dataListModel.getSize())
			return;
		dataListModel.remove(index);
		if(index == curIndex)
			curIndex = -1;

		if(index >= dataListModel.getSize())
			index = 0;
		nextIndex = index;
		setSelectedIndex(index);
	}

	/**
	 * 清空列表。
	 */
	public synchronized void clear() {
		nextIndex = 0;
		curIndex = -1;
		dataListModel.clear();
	}

	

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}
	

	/**
	 * 播放当前文件时是否被用户调用 {@link #setNextIndex(int)} 方法中断。
	 * @return 返回<b>true</b>表示播放当前文件时是否被用户中断，否则返回<b>false</b>。
	 */
	public synchronized boolean isInterrupted() {
		return nextIndex != -1;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;  
        //设置透明度为0.4f  
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);  
        g2.setComposite(ac);  
        g2.setColor(Color.white);  
        //绘制一个与输入域等大小的填充矩形框来形成其半透明效果  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}
	
	/**
	 * 自定义ui
	 * @author yinchuandong
	 *
	 */
	public class CustomerUI extends BasicListUI{
		public CustomerUI() {
	        super();
	        cellHeights = new int[2];
	    }

	    public void setCellHeight(int index, int value, int defaultHeight) {
	        for (int i = 0; i < cellHeights.length; i++) {
	            cellHeights[i] = defaultHeight;
	        }
	        cellHeights[index] = value;
	    }

	    public void setCellHeight(int index, int i) {
	        cellHeights[index] = i;
	    }
	}
	
	/**
	 * 保存播放列表
	 */
	public void savePlayList(){
		try{
			PrintWriter writer = new PrintWriter(new File("playlist.txt"));
			JSONArray jsonArr = JSONArray.fromObject("[]");
			for(int i = 0; i < dataListModel.size(); i++){
				PlayListItem item = dataListModel.get(i);
				jsonArr.add(item);
			}
			writer.write(jsonArr.toString());
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载播放列表
	 */
	public void loadPlayList(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("playlist.txt")));
			String buff = null;
			String str = "";
			while((buff = reader.readLine()) != null){
				str += buff;
			}
			reader.close();
			
			JSONArray jsonArr = JSONArray.fromObject(str);
			for(int i = 0; i < jsonArr.size(); i++){
				JSONObject obj = jsonArr.getJSONObject(i);
				String title = obj.getString("title");
				String author = obj.getString("author");
				String path = obj.getString("path");
				String cover = obj.getString("cover");
				boolean local = obj.getBoolean("local");
				dataListModel.addElement(new PlayListItem(title, author, path, cover, local));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
