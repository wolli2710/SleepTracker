package wv.masterthesis.sleeptracker;

import java.io.IOException;

import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
//import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener{

    public SensorManager mSensorManager;
    JsonHandler jHandler = null;
    private AudioHandler mAudioActivity;      
    Button stop_button;
    
	public static float threshold = 0.12f;
    private boolean isRecording;

    public double check_x = 0.0;
    public double check_y = 0.0;
    public double check_z = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        createJsonHandler();
        createAudioHandler();
        createStopButton();
        createSensorManager();
        setIsRecording(true);
    }
    
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	//check if acceleration sensor has changed
    //if acceleration in a certain time is bigger than the threshold write to file
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && isRecording){
            if(threshold < Math.abs(check_x - event.values[0])|| threshold < Math.abs(check_y - event.values[1]) || threshold < Math.abs(check_z - event.values[2]) ){
                writeValueToJson(event);
            }
        }
    }
    
    private void createSensorManager() { 
        mSensorManager.registerListener(this, 
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_FASTEST);
    }
    
	public void writeValueToJson(SensorEvent event) {
		check_x = event.values[0];
		check_y = event.values[1];
		check_z = event.values[2];
		
		String jsonArray = "["+event.values[0]+","+event.values[1]+","+event.values[2]+"]";
		String jsonKey = event.timestamp+"";
		
		try {
		    jHandler.appendJsonValue( jsonKey, jsonArray );
		} catch (JSONException e) {
		    e.printStackTrace();
		}
	}

    private void createJsonHandler() {
        jHandler = new JsonHandler();
    }

    private void createAudioHandler() {
        mAudioActivity = AudioHandler.getAudioActivity();
        mAudioActivity.setFrequencyOffset(1000);
        mAudioActivity.startRecording();
    }

    private void createStopButton() {
        stop_button = (Button)findViewById(R.id.button1);
        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mAudioActivity.stopRecording();
            	try {
            		jHandler.writeDirectory();
					jHandler.writeJsonToFile("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	setIsRecording(false);
                
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                setContentView(R.layout.activity_main);
            }
        });
    }

	public void setIsRecording(boolean isR){
		isRecording = isR;
	}
	
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      // Inflate the menu; this adds items to the action bar if it is present.
//      getMenuInflater().inflate(R.menu.sensor, menu);
//      return true;
//  }
            
}
