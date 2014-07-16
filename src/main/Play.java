package main;

import java.io.IOException;

/**
 * 控制台命令行播放器。这是一个解码器调用示例。
 */
public class Play {
	/**
	 * 控制台命令行播放器。
	 * 
	 * @param args
	 *            指定的源文件。
	 */
	public static void main(String[] args) {
		args = new String[]{"http://m5.file.xiami.com/906/906/4567/55552_3848085_l.mp3?auth_key=901abf685e747155403e46def3b97663-1405296000-0-null"};
//		args = new String[]{"F:\\music\\红豆.mp3","http://m5.file.xiami.com/906/906/4567/55552_3848085_l.mp3?auth_key=edb55a1997c27a75aa5f34d253eb68ce-1405209600-0-null"};
		if (args.length == 0) {
			System.err.println("Please specify a valid filename.");
			return;
		}

		PlayBack player = new PlayBack(new jmp123.output.Audio());
		//PlayBack player = new PlayBack(null);

		for (String name : args) {
			System.out.println(name);
			try {
				if (player.open(name, null)) { // null:可以不指定歌曲标题
					player.getID3Tag().printTag();
					player.getHeader().printVBRTag();
					player.getHeader().printHeaderInfo();
					player.start(true); // true:在控制台打印播放进度
					player.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}