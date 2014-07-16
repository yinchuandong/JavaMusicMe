package Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class TestRemote {
	
	public static void downloadByByte(String path, String url) throws IOException{
		URL uri = new URL(url);
		InputStream inputStream = uri.openStream();
		 // 创建文件对象  
        File file = new File(path);  
        // 创建文件路径  
        if (!file.getParentFile().exists()) {
        	file.getParentFile().mkdirs();
        }
		FileOutputStream outputStream = new FileOutputStream(file);
		byte[] buff = new byte[2048];
		int len = -1;
		while((len = inputStream.read(buff)) != -1){
			outputStream.write(buff, 0, len);
		}
		outputStream.close();
		inputStream.close();
	}
	
    /** 
     * 下载文件保存到本地 
     *  
     * @param path 
     *            文件保存位置 
     * @param url 
     *            文件地址 
     * @throws IOException 
     */  
    public static void downloadFile(String path, String url) throws IOException {  
        DefaultHttpClient client = null;  
        try {  
            // 创建HttpClient对象  
            client = new DefaultHttpClient();  
            // 获得HttpGet对象  
            HttpGet httpGet = new HttpGet(url); 
            // 发送请求获得返回结果  
            HttpResponse response = client.execute(httpGet);  
            // 如果成功  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                byte[] result = EntityUtils.toByteArray(response.getEntity());  
                BufferedOutputStream bw = null;  
                try {  
                    // 创建文件对象  
                    File f = new File(path);  
                    // 创建文件路径  
                    if (!f.getParentFile().exists())  
                        f.getParentFile().mkdirs();  
                    // 写入文件  
                    bw = new BufferedOutputStream(new FileOutputStream(path));  
                    bw.write(result);  
                } catch (Exception e) {  
                } finally {  
                    try {  
                        if (bw != null)  
                            bw.close();  
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }  
                }  
            }
        } catch (ClientProtocolException e) {  
            throw e;  
        } catch (IOException e) {  
            throw e;  
        } finally {  
            try {  
                client.getConnectionManager().shutdown();  
            } catch (Exception e) { 
            	e.printStackTrace();
            }  
        }  
    } 
	
	public static void main(String[] args) throws IOException{
//		String src = "http://img.xiami.net/images/album/img39/2739/145831376589249_4.jpg";
		String src = "http://m5.file.xiami.com/739/2739/1291321529/179924_11350913_l.mp3?auth_key=b66a6dfc6e6f767ce5fa5d44c1d1d8e7-1405468800-0-null";
		downloadByByte("./test2.mp3", src);
//		downloadFile("./test2.mp3", src);
		
	}
}
