package Widget;

public class PlayListItem {
	private String title;
	private String path;
	private String author;
	private String cover;
	private boolean isLocal;

	public PlayListItem(String title, String path) {
		this.title = title;
		this.path = path;
	}
	
	public PlayListItem(String title, String author, String path, String cover, boolean isLoacal){
		this.title = title;
		this.author = author;
		this.path = path;
		this.cover = cover;
		this.isLocal = isLoacal;
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	@Override
	public String toString() {
		if (author == null || author.equals("")) {
			return title;
		}else{
			return title + "-" + author; // 返回在列表内显示的字符串
		}
	}

}
