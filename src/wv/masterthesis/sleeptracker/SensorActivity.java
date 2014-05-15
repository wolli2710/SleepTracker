package wv.masterthesis.sleeptracker;

import java.io.IOException;

import org.json.JSONException;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    public static SensorActivity instance;
    private AudioHandler mAudioActivity;
    private JsonHandler jHandler;
    private static boolean isRecording;
    
//    static TextView xCoor;
//    static TextView yCoor;
//    static TextView zCoor;
    
    Button stop_button;
    //Context context;

    private long checkTime;
    private double check_x = 0.0;
    private double check_y = 0.0;
    private double check_z = 0.0;
    private float threshold = 0.12f;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        
        instance = this;
        isRecording = true;
        
        createJsonHandler();
        createAudioHandler();
        createSensorManager();
        createStopButton();
    }

    private void createJsonHandler() {
        jHandler = new JsonHandler();
        jHandler.setApplicationDirectory( Environment.getExternalStorageDirectory() +"/"+ getPackageName() );
    }

    private void createSensorManager() {
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        
        mSensorManager.registerListener(this, 
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_FASTEST);
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
            	isRecording = false;
                try {
					writeJsonToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                setContentView(R.layout.activity_main);
            }
        });
    }
    
    private void writeJsonToFile() throws IOException{
        jHandler.writeDirectory();
        jHandler.writeJsonToFile("");
    }


//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      // Inflate the menu; this adds items to the action bar if it is present.
//      getMenuInflater().inflate(R.menu.sensor, menu);
//      return true;
//  }
    

    //check if acceleration sensor has changed
    //if acceleration in a certain time is bigger than the threshold write to file
    public void onSensorChanged(SensorEvent event){
        
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && isRecording){
            
//            if (System.currentTimeMillis() > checkTime+20){
//                checkTime = System.currentTimeMillis();
                checkTime = event.timestamp;

                if(threshold < Math.abs(check_x - event.values[0])|| threshold < Math.abs(check_y - event.values[1]) || threshold < Math.abs(check_z - event.values[2]) ){
                    check_x = event.values[0];
                    check_y = event.values[1];
                    check_z = event.values[2];
                    
//                    String res =  (System.currentTimeMillis() > checkTime+100) ? "True" : "False";
                    String res =  ( event.timestamp > checkTime+100) ? "True" : "False";
                    
                    Log.wtf("changed", res );
                    
//                    setCurrentAcceleration( event );
                                
                    String jsonArray = "["+event.values[0]+","+event.values[1]+","+event.values[2]+"]";
                    String jsonKey = event.timestamp+"";
                    
                    try {
                        jHandler.appendJsonValue( jsonKey, jsonArray );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }

    //write current acceleration to screen
//    private void setCurrentAcceleration(SensorEvent a) {
//        xCoor.setText("X: "+a.values[0]);
//        yCoor.setText("Y: "+a.values[1]);
//        zCoor.setText("Z: "+a.values[2]);
//    }
        
}
