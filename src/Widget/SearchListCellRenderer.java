package Widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * 个性化显示播放列表项
 */
@SuppressWarnings("serial")
class SearchListCellRenderer extends JLabel implements ListCellRenderer {
	private Font selFont;
	private Font plainFont;
	private Color selectedBgColor, selectedFontColor, foregroundColor, curColor;

	public SearchListCellRenderer() {
		super();
		setOpaque(false);
		String fontName = getFont().getName();
		int fontSize = getFont().getSize();
		selFont = new Font(fontName, Font.BOLD, 5 * fontSize / 4);
		plainFont = new Font(fontName, Font.PLAIN, fontSize);
		selectedBgColor = Color.decode("#75A101");
		selectedFontColor = Color.WHITE;
		foregroundColor = Color.WHITE;
		curColor = Color.decode("#75A101");
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		PlayListItem item = (PlayListItem) value;
		int curIndex = ((SearchList) list).getCurIndex();

		// 设置单元格： 序号+标题+艺术家
		StringBuilder sbuf = new StringBuilder();
		sbuf.append(index + 1);
		sbuf.append(". ");
		sbuf.append(item.toString());

		setText(sbuf.toString());

		if (isSelected) {
			setForeground(curColor);
			setBackground(selectedBgColor);
			setFont(selFont);
		}else {
			setForeground(foregroundColor);
			setBackground(Color.decode("#343434"));
			setFont(selFont);
		}

		return this;
	}
}
