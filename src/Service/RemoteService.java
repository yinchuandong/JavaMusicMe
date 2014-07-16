package Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.methods.multipart.StringPart;

import Ui.MainPanel;
import Util.HttpUtil;
import Widget.PlayListItem;

public class RemoteService {

	private MainPanel mainPanel;
	private ExecutorService taskpool;
	
	public RemoteService(MainPanel mainPanel){
		this.mainPanel = mainPanel;
		this.taskpool = Executors.newCachedThreadPool();
	}
	
	public void download(PlayListItem item){
		taskpool.execute(new DownLoadThread(item));
	}
	
	
	public void onCoverComplete(PlayListItem item){
		if (this.mainPanel != null) {
			this.mainPanel.onCoverComplete(item);
		}
	}
	
	public void onSongComplete(PlayListItem item){
		if (this.mainPanel != null) {
			this.mainPanel.onSongComplete(item);
		}
	}
	
	public class DownLoadThread extends Thread{

		private PlayListItem item;
		
		public DownLoadThread(PlayListItem item){
			this.item = item;
		}
		
		@Override
		public void run() {
			String author = item.getAuthor();
			String title = item.getTitle();
			String dir = "./cache";
			String localCover = dir + "/cover/" + title + "-" + author + ".jpg";
			String localSong = dir + "/song/" +  title + "-" + author + ".mp3";
			try {
				if (item.getCover() != null && item.getCover().startsWith("http")) {
					HttpUtil.downloadByByte(localCover, item.getCover());
					item.setCover(localCover);
				}
				onCoverComplete(item);
				
				if (item.getCover() != null && item.getPath().startsWith("http")) {
					HttpUtil.downloadByByte(localSong, item.getPath());
					item.setPath(localSong);
					item.setLocal(true);
				}
				onSongComplete(item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		String cover = "http://img.xiami.net/images/album/img39/2739/145831376589249_4.jpg";
		String path = "http://m5.file.xiami.com/739/2739/1291321529/179924_11350913_l.mp3?auth_key=b66a6dfc6e6f767ce5fa5d44c1d1d8e7-1405468800-0-null";
		RemoteService service = new RemoteService(null);
		service.download(new PlayListItem("我们的爱", "fireyuetuan", path, cover, false));
	}
	
}
