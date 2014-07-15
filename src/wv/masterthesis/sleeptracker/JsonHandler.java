package wv.masterthesis.sleeptracker;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;
import android.os.Environment;
import android.util.Log;
import org.json.JSONException;

class JsonHandler{

	public static String applicationDirectory;
	String storage_state = Environment.getExternalStorageState();
	private static JSONObject currentUserData = null;
	InputStream is = null;
	String fileName = null;
	File file = null;
	
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
		JSONObject currentRecord = new JSONObject();
		currentRecord.put( currentKey, currentValue);
		writeToFile(currentRecord);
	}
	
	void createFile(String prefix){
		fileName = prefix+String.valueOf( System.currentTimeMillis() / 1000L );
		file = new File(applicationDirectory +"/"+ fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	void stopRecording(){
		fileName = null;
		file = null;
	}

	void appendCurrentUserToJson() throws JSONException
	{
		JSONObject currentRecord = new JSONObject();
		currentRecord.put( "userData", currentUserData);
		writeToFile(currentRecord);
	}
	
//	String readStringFromFile(String fileName){
//		String result = new Scanner(fileName).useDelimiter("\\A").next();
//		return result;
//	}
	
	private void writeToFile(JSONObject record) {
		if( new File(applicationDirectory).exists() ){
			if(file.exists()){
				try {
					FileWriter fw = new FileWriter(file, true);
					//fw.write(record.toString()+",");
					fw.append(record.toString()+",\n");
					fw.close();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Log.d("JsonHandler writeJsonToFile", "No External Storage Device found");
			}
		}
	}
		
	/*
	 * Handle User Data
	 */
	public void saveUserData(JSONObject jObject ){
		currentUserData = jObject;
	}
	
	public void resetUserData(){
		currentUserData = null;
	}
	
	public JSONObject getCurrentUserData(){
		return currentUserData;
	}
	
}