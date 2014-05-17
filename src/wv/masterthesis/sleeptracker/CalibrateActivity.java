package wv.masterthesis.sleeptracker;

import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class CalibrateActivity extends Activity {

	private int delayMillis = 4000;
	private JsonHandler jHandler = null;
	private float accelerationValue = 0;
	AccelerationHandler mAccelerationHandler = null;
	Thread mAccelerationThread = null;
	
//  static TextView xCoor;
//  static TextView yCoor;
//  static TextView zCoor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);
		jHandler = new JsonHandler();
		mAccelerationHandler = new AccelerationHandler(getApplicationContext(), jHandler);
	
		calibrate();
		calculateAccelerationThreshold();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.calibrate, menu);
//		return true;
//	}
	
	private void calculateAccelerationThreshold(){
		mAccelerationHandler.setIsCalibrating(true);
		Runnable runnable = new Runnable(){
			public void run(){
				while(mAccelerationHandler.getIsCalibrating()){
					accelerationValue = getMaxValue(mAccelerationHandler.maxValue);
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
						mAccelerationHandler.setIsCalibrating(false);
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

}
