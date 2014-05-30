package wv.masterthesis.sleeptracker;

import java.io.IOException;
import org.json.JSONException;
import android.media.MediaRecorder;
import android.util.Log;


public class AudioHandler {
	
	public static AudioHandler instance = null;
	private MediaRecorder mRecorder = null;
	private JsonHandler jHandler = null;
	
	private static Thread audioThread;
	private static int frequencyOffset;
	private static boolean isRecording;
	private static String fileName = "/dev/null";
	
	public static AudioHandler getAudioActivity(){
		Log.d("AudioActivity public static AudioActivity", (instance == null)+"" );
		if(instance == null){
			instance = new AudioHandler();
		}
		return instance;
	}
	
	private AudioHandler(){
		setApplicationDirectory(System.currentTimeMillis()/1000+".3gp");
		jHandler = new JsonHandler();
		jHandler.createFile("_audio");
        mRecorder = new MediaRecorder();
        
        try{
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        mRecorder.setOutputFile(fileName);
	        
	        mRecorder.prepare();
	        mRecorder.start();
	   } catch (IOException e){
		   e.printStackTrace();
	   } catch (IllegalStateException e) {
    	   e.printStackTrace();
       }
        
       getAmplitude();
       createAudioThread();
	}

	private void createAudioThread() {
		try {
			jHandler.appendCurrentUserToJson();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		audioThread = new Thread(new Runnable(){
			@Override
			public void run() {
		        try {
		            while(isRecording){
		            	int currentAmplitude = getAmplitude();
//		            	if( currentAmplitude > getFrequencyOffset() ){
		            		try {
		            			long currentTimeStamp = System.currentTimeMillis();
								jHandler.appendJsonValue(currentTimeStamp+"",currentAmplitude+"");
							} catch (JSONException e) {
								e.printStackTrace();
							}
		            		//Log.i("TAG", "onRun() amplitude: "+currentAmplitude+"" );
		            		//Log.i("TAG", "onRun() amplitude size: "+(currentAmplitude > getFrequencyOffset())+"" );
//		            	}
		                
		            	Thread.sleep(8);
		            }
		            Thread.sleep(1000);
			    } catch (InterruptedException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			    }
			}
		});
		audioThread.start();
	}

    public int getAmplitude() {
        if (mRecorder != null){
        	return  mRecorder.getMaxAmplitude();
        }else{
        	Log.d("AudioActivity", "no MediaRecorder found!!");
            return 0;
        }
    }
        
    public void stopRecording(){
		setIsRecording(false);
		mRecorder.stop();
		jHandler.stopRecording();
		try {
			writeJsonToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		instance = null;
		mRecorder = null;
		audioThread = null;
	}
	
	public void startRecording(){
		setIsRecording(true);
	}

	private int getFrequencyOffset() {
		return frequencyOffset;
	}

	public void setFrequencyOffset(int fo) {
		frequencyOffset = fo;
	}
	
	private void setIsRecording(boolean value) {
		isRecording = value;
	}
	
    private void writeJsonToFile() throws IOException{
        jHandler.writeDirectory();
        
    }
    
    private void setApplicationDirectory(String _fileName){
    	fileName = JsonHandler.applicationDirectory + "/" + _fileName;
    }
    
}