package wv.masterthesis.sleeptracker;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONObject;
import android.os.Environment;
import android.util.Log;
import org.json.JSONException;

class JsonHandler{

	public static String applicationDirectory;
	public static String settingsFile = "configuration.json";
	String storage_state = Environment.getExternalStorageState();
	JSONObject  currentRecord = new JSONObject();
	JSONObject currentSettings = new JSONObject();
	InputStream is = null;
	
	
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
		writeToFile(fileName, currentRecord);
	}
	
	void writeSettingsToFile(String name) throws IOException
	{
		writeToFile(name, currentSettings);
	}
	
	void loadSettingsFromFile() throws JSONException{
		String currentSettingsString = readStringFromFile(applicationDirectory+"/"+settingsFile);
		currentSettings.get(currentSettingsString);
		
		//float accelerationThreshold = (float)currentSettings.getDouble("accelerationThreshold");
		//float audioThreshold = (float)currentSettings.getInt("audioThreshold");
		
	}

	void appendSettings(String key, String value){
		try {
			currentSettings.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	String readStringFromFile(String fileName){
		String result = new Scanner(fileName).useDelimiter("\\A").next();
		return result;
	}
	
	private void writeToFile(String fileName, JSONObject record) throws IOException {
		if( new File(applicationDirectory).exists() ){
			File file = new File(applicationDirectory +"/"+ fileName);
			file.createNewFile();
			if(file.exists()){
				FileWriter fw = new FileWriter(file);
				fw.write(record.toString());
				fw.close();	
			}else {
				Log.d("JsonHandler writeJsonToFile", "No External Storage Device found");
			}
		}
	}

}