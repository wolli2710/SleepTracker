package wv.masterthesis.sleeptracker;

import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CalibrateActivity extends Activity implements SensorEventListener {

	public SensorManager mSensorManager;
	private int delayMillis = 4000;
	private JsonHandler jHandler = null;
	private float accelerationValue = 0;
	Thread mAccelerationThread = null;
	
    private boolean isCalibrating;
    public float maxValue = 0;
	
//  static TextView xCoor;
//  static TextView yCoor;
//  static TextView zCoor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		jHandler = new JsonHandler();
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		createSensorManager();
		calibrate();
		calculateAccelerationThreshold();
	}
	
    private void createSensorManager() { 
        mSensorManager.registerListener(this, 
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_FASTEST);
    }
    
	@Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && isCalibrating) {
        	setMaxValue(event);
        }
    }
    
    private void setMaxValue(SensorEvent event){
    	float tempMaxValue = (event.values[0] > event.values[1])?event.values[0]:event.values[1];
    	tempMaxValue = (event.values[2] > tempMaxValue)?event.values[2]:maxValue;
    	if(tempMaxValue > maxValue){
    		maxValue = tempMaxValue;
    	}
    }
	
	private void calculateAccelerationThreshold(){
		setIsCalibrating(true);
		Runnable runnable = new Runnable(){
			public void run(){
				while(getIsCalibrating()){
					accelerationValue = getMaxValue(maxValue);
				}
			}
		};
		mAccelerationThread = new Thread(runnable);
		mAccelerationThread.start();
	}
	
	private float getMaxValue(float newValue){
		return (newValue > accelerationValue)?newValue:accelerationValue;
	}
	
	private void calibrate(){
		new android.os.Handler().postDelayed(
			new Runnable(){
				public void run(){
					try {
						setIsCalibrating(false);
						jHandler.writeDirectory();
						
						//TODO!!!!!!!!!!!!!!!!!
						jHandler.appendSettings("accelerationThreashold", Float.toString(accelerationValue)+"");
						jHandler.writeSettingsToFile(JsonHandler.settingsFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
					startActivity(intent);
				}
			}, delayMillis);
	}
	
	public boolean getIsCalibrating(){
		return isCalibrating;
	}
	
	public void setIsCalibrating(boolean isC){
		isCalibrating = isC;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
