package wv.masterthesis.sleeptracker;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;

class JsonHandler{

	static String applicationDirectory; 
	String storage_state = Environment.getExternalStorageState();
	JSONObject currentRecord = new JSONObject();
	

	public void setApplicationDirectory(String directoryName){
		applicationDirectory = directoryName;
	}

	void writeDirectory() throws IOException
	{
		if( checkStorageState() ){
			File newDir = new File( applicationDirectory );
			newDir.mkdir();
		} else {
		    //new AlertDialog.Builder(this).setTitle("No External Storage Device found").setMessage("Please insert external Storage Device").setNeutralButton("Close", null).show();
			Log.d("JsonHandler writeDirectory", "No External Storage Device found");
		}
	}
	
	public boolean checkStorageState()
	{
		return (Environment.MEDIA_MOUNTED.equals(storage_state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storage_state)) ? true : false;
	}
	
	void appendJsonValue(String currentKey, String currentValue) throws JSONException
	{
		//long currentTimeStamp = System.currentTimeMillis() / 1000L;
		currentRecord.put( currentKey, currentValue);
	}
	
	void writeJsonToFile(String prefix) throws IOException
	{
		String fileName = prefix+String.valueOf( System.currentTimeMillis() / 1000L );
		if( new File(applicationDirectory).exists() ){
			File file = new File(applicationDirectory +"/"+ fileName);
			file.createNewFile();
			if(file.exists()){
				FileWriter fw = new FileWriter(file);
				fw.write(currentRecord.toString());
				fw.close();	
			}else {
				//new AlertDialog.Builder(this).setTitle("No External Storage Device found").setMessage("fuuuuuuuuuuuu").setNeutralButton("Close", null).show();
				Log.d("JsonHandler writeJsonToFile", "No External Storage Device found");
			}
		}
	}
}