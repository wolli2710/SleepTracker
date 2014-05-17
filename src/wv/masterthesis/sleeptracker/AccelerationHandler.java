package wv.masterthesis.sleeptracker;

import org.json.JSONException;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelerationHandler implements SensorEventListener{

    public static SensorManager mSensorManager;
    JsonHandler jHandler = null;
    Context context;
	
	public float threshold = 0.12f;
    private boolean isRecording;
    private boolean isCalibrating;
    
    public double check_x = 0.0;
    public double check_y = 0.0;
    public double check_z = 0.0;
    
    public float maxValue = 0;
	
    AccelerationHandler(Context ctx, JsonHandler jH){
        createSensorManager();
        context = ctx;
        jHandler = jH;
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
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && isCalibrating) {
        	setMaxValue(event);
        }
    }
    
    private void createSensorManager() { 
        mSensorManager.registerListener(this, 
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    private void setMaxValue(SensorEvent event){
    	Log.wtf("calibrating", event.values[0]+"" );
    	float tempMaxValue = (event.values[0] > event.values[1])?event.values[0]:event.values[1];
    	tempMaxValue = (event.values[2] > tempMaxValue)?event.values[2]:maxValue;
    	if(tempMaxValue > maxValue){
    		maxValue = tempMaxValue;
    	}
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

	public boolean getIsCalibrating(){
		return isCalibrating;
	}
	
	public void setIsRecording(boolean isR){
		isRecording = isR;
	}
	
	public void setIsCalibrating(boolean isC){
		isCalibrating = isC;
	}
    
//		write current acceleration to screen
//      private void setCurrentAcceleration(SensorEvent a) {
//      	xCoor.setText("X: "+a.values[0]);
//      	yCoor.setText("Y: "+a.values[1]);
//      	zCoor.setText("Z: "+a.values[2]);
//      }
	
}
