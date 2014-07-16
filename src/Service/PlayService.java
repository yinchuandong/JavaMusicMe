package Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

import jmp123.decoder.Header;
import jmp123.decoder.ID3Tag;
import jmp123.output.Audio;

import main.PlayBack;

import Model.Song;
import Ui.IndexFrame;
import Widget.PlayList;
import Widget.PlayListItem;

public class PlayService extends Thread{

	public interface Mode{
		public static int SEQUENCE = 1;
		public static int CIRCLE = 2;
		public static int RANDOM = 3;
	}
	private PlayList playList;
	private PlayBack player;
	private Audio audio;
	private IndexFrame frame;
	
	private boolean interrupt = false;
	private boolean isCustomSelected = false;
	private int curIndex = 0;
	private Header header;
	private ID3Tag id3Tag;
	
	private int elapse = 0;
	
	public PlayService(JFrame frame){
		this.frame = (IndexFrame)frame;
		this.audio = new Audio();
		player = new PlayBack(this.audio);
	}
	
	public void setPlayList(PlayList playList){
		this.playList = playList;
	}
	
	public synchronized void startPlay(int index){
		this.curIndex = index;
		this.isCustomSelected = true;
		playList.setCurIndex(curIndex);
		player.stop();
	}
	
	/**
	 * 播放上一首
	 */
	public synchronized void playNext(){
		this.curIndex ++;
		curIndex %= playList.getCount();
		playList.setCurIndex(curIndex);
		player.stop();
	}
	
	/**
	 * 播放下一首
	 */
	public synchronized void playPre(){
		this.curIndex = (this.curIndex - 1 < 0) ? 0 : (this.curIndex - 1);
		playList.setCurIndex(curIndex);
		player.stop();
	}
	
	public synchronized void pause(){
		player.pause();
	}
	
	/**
	 * 终止此播放线程。
	 */
	public synchronized void interrupt() {
		interrupt = true;
		super.interrupt();
		player.stop();
	}
	
	/**
	 * 设置播放的进度 
	 * @param elapse :the time how long song had already played
	 */
	public void setElapse(int elapse){
		this.elapse = elapse;
	}
	
	public void seek(int fromPos){
		if (player != null) {
			player.seek(fromPos);
		}
	}
	
	public void setVolume(float volume){
		volume = volume <= -80f ? -80f : volume;
		volume = volume >= 6f ? 6f : volume;
		audio.setVolume(volume);
	}
	
	@Override
	public void run(){
		while(!interrupt){
			if (playList == null || playList.getCount() == 0) {
				interrupt = true;
				break;
			}
			playList.setSelectedIndex(curIndex);
			PlayListItem item = playList.getPlayListItem(curIndex);
			try {
				if (player.open(item.getPath(), null)) {
					//初始化改歌曲的信息
					frame.getMainPanel().initSong(item, id3Tag);
					
					header = player.getHeader();
					id3Tag = player.getID3Tag();
					int totalSec = (int)header.getDuration();
					frame.initProgress(totalSec);
					player.start(false, elapse);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				player.close();
				if (!isCustomSelected) {
					this.playNext();
				}else{
					isCustomSelected = false;
				}
			}
		}
	}
	
	
	
	public PlayBack getPlayer() {
		return player;
	}

	public void setPlayer(PlayBack player) {
		this.player = player;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ID3Tag getId3Tag() {
		return id3Tag;
	}

	public void setId3Tag(ID3Tag id3Tag) {
		this.id3Tag = id3Tag;
	}

	public boolean isInterrupt() {
		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	public static void main(String[] args){
		PlayService service = new PlayService(null);
		service.start();
	}
	
}
