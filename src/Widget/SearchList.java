package Widget;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.DefaultListModel;
import javax.swing.JList;


import Model.Song;

public class SearchList extends JList<PlayListItem>{

	private DefaultListModel<PlayListItem> listModel;
	private int curIndex = 0;
	
	public SearchList(){
		super();
		listModel = new DefaultListModel<PlayListItem>();
		setCellRenderer(new SearchListCellRenderer());
		setModel(listModel);
		setOpaque(false);
		setBackground(Color.decode("#282828"));
//		init();
	}
	
	private void init(){
		PlayListItem s1 = new PlayListItem("红豆", "F:\\music\\红豆.mp3");
		PlayListItem s2 = new PlayListItem("我们的歌","http://m5.file.xiami.com/906/906/4567/55552_3848085_l.mp3?auth_key=1c2dce603f13b7db4c04bac05c2611b1-1405468800-0-null");
		listModel.addElement(s1);
		listModel.addElement(s2);
	}
	
	public void append(PlayListItem playItem){
		listModel.addElement(playItem);
	}
	public void clear(){
		listModel.clear();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;  
        //设置透明度为0.4f  
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);  
        g2.setComposite(ac);  
        g2.setColor(Color.white);  
        //绘制一个与输入域等大小的填充矩形框来形成其半透明效果  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	
}
