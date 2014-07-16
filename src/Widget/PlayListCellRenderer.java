package Widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import Widget.PlayList.CustomerUI;

/**
 * 个性化显示播放列表项
 */
@SuppressWarnings("serial")
class PlayListCellRenderer extends JLabel implements ListCellRenderer<Object> {
	private Font selFont;
	private Color selectedBgColor,selectedFontColor, foregroundColor, curColor;

	public PlayListCellRenderer() {
		super();
		setOpaque(true);
//		String fontName = getFont().getName();
		String fontName = "Microsoft Yahei";
		int fontSize = getFont().getSize();
		selFont = new Font(fontName, Font.BOLD, fontSize );
		selectedBgColor = Color.decode("#75A101");
		selectedFontColor = Color.WHITE;
		foregroundColor = Color.WHITE;
		curColor = Color.decode("#75A101");
		
	}

	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		PlayListItem item = (PlayListItem) value;
		int curIndex = ((PlayList) list).getCurIndex();
		
		((CustomerUI)list.getUI()).setCellHeight(index, 30,30);

		// 设置单元格： 序号+标题+艺术家
		StringBuilder sbuf = new StringBuilder();
		sbuf.append(index + 1);
		sbuf.append(". ");
		sbuf.append(item.toString());

		setText(sbuf.toString());

		if (index == curIndex && !isSelected) {
			setForeground(curColor);
			setBackground(Color.decode("#292929"));
			setFont(selFont);
		} else if (index == curIndex && isSelected) {
			setForeground(selectedFontColor);
			setBackground(selectedBgColor);
			setFont(selFont);
		} else if (isSelected){
			setForeground(selectedFontColor);
			setBackground(selectedBgColor);
			setFont(selFont);
		}else {
			setForeground(foregroundColor);
			setBackground(Color.decode("#343434"));//#282828
			setFont(selFont);
		}

		return this;
	}
}
