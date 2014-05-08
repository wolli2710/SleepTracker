package wv.masterthesis.sleeptracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;


public class NetworkHandler {

	public static NetworkHandler instance = null;
	private Context context;
	private String path;
	private Thread networkThread;
	
	public static NetworkHandler getNetworkActivity(Context ctx){
		Log.d("NetworkActivity public static NetworkActivity", (instance == null)+"" );
		if(instance == null){
			instance = new NetworkHandler(ctx);
		}
		return instance;
	}
	
	NetworkHandler(Context ctx){
		context = ctx;
		path = Environment.getExternalStorageDirectory() +"/"+ context.getPackageName();
		
 
		//checks and JSON files that are not in the database yet.
		networkThread = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					if( isOnline() ){
						try {
							sendFiles();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Log.wtf("thread", "is not online" );
					}
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		networkThread.start();
	}
	
	
	private boolean isOnline() {
		try
        {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm != null){
			    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			    for (int i = 0; i < netInfo.length; i++) {
	              if (netInfo[i].getState() == NetworkInfo.State.CONNECTED) {
	                 return true;
	              }
	           }
			}
        }
		catch(Exception ex)
		{
		}
		return false;   
	}
	
	
	private void sendFiles() throws Exception{
		File f = new File(path);
		File files[] = f.listFiles();
		if( files.length > 0 ){
			for(File file: files){
								
				if(!file.getName().contains("__")){		
					int statusCode = sendJson(readFile(file.getAbsolutePath())).getStatusLine().getStatusCode();
					if( statusCode == 200 ){
						
						//file.delete();
					} else {
						
						file.renameTo( new File(file.getPath(), "__"+file.getName() ) );
						//TODO send notification to server!!!
					}
					
				}
			}
		}
	}
	
	
	String readFile(String path) throws IOException 
	{
		String line;
		String json = "";
		InputStream fileInput = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInput, Charset.forName("UTF-8")));
		while((line=br.readLine()) != null){
			json+=line;
		}
		br.close();
		return json;
	}

	
	private HttpResponse sendJson(String currentJson) throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://10.0.0.6:3000/api/v1/snooze_apis");
		
		StringEntity params = new StringEntity("data="+currentJson );
		httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
        httpPost.setEntity(params);
			    	    
	    return httpClient.execute(httpPost);
	}
	
}
