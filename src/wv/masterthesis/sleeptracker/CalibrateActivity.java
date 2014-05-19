package wv.masterthesis.sleeptracker;

import java.io.IOException;
import android.os.Bundle;
import android.util.Log;
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
	
    private boolean isCalibrating;
    private boolean initCalibration;
    private float maxValue = 0;
	
    private double check_x = 0.0;
    private double check_y = 0.0;
    private double check_z = 0.0;
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		jHandler = new JsonHandler();
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		createSensorManager();
		calibrate();
		setIsCalibrating(true);
		setInitCalibration(true);
	}
	
    private void createSensorManager() { 
        mSensorManager.registerListener(this, 
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_FASTEST);
    }
    
	@Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && initCalibration) {
        	check_x = Math.abs(event.values[0]);
        	check_y = Math.abs(event.values[1]);
        	check_z = Math.abs(event.values[2]);
        	setInitCalibration(false);
        	Log.i("initx", check_x+"");
        	Log.i("inity", check_y+"");
        	Log.i("initz", check_z+"");
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && isCalibrating) {
        	if(SensorActivity.threshold < Math.abs(check_x - event.values[0]) || SensorActivity.threshold < Math.abs(check_y - event.values[1]) || SensorActivity.threshold < Math.abs(check_z - event.values[2]) ){
        		setMaxValue(event);
            }
        }
    }
    
    private void setMaxValue(SensorEvent event){
    	float tempMaxValue = (float) ((Math.abs(check_x - event.values[0]) > Math.abs(check_y - event.values[1]) )? Math.abs(check_x - event.values[0]):Math.abs(check_x - event.values[1]));
    	tempMaxValue = (float) (( Math.abs(check_z - event.values[2]) > tempMaxValue)? Math.abs(check_z - event.values[2]): tempMaxValue);
    	if(tempMaxValue > maxValue){
    		maxValue = tempMaxValue;
    	}
    }
		
	private void calibrate(){
		new android.os.Handler().postDelayed(
			new Runnable(){
				public void run(){
					try {
						setIsCalibrating(false);
						jHandler.writeDirectory();

						jHandler.appendSettings("accelerationThreashold", Float.toString(maxValue)+"");
						SensorActivity.threshold = maxValue;
						jHandler.writeSettingsToFile(JsonHandler.settingsFile);
						Log.i("currentThreshold", maxValue+"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
					startActivity(intent);
				}
			}, delayMillis);
	}
		
	public void setIsCalibrating(boolean isC){
		isCalibrating = isC;
	}
	
	public void setInitCalibration(boolean initC){
		initCalibration = initC;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
