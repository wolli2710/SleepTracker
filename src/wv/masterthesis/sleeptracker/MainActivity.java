package wv.masterthesis.sleeptracker;


import org.json.JSONException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity{

	Button start_button;
	ImageButton settings_button;
	NetworkHandler networkHandler;
	JsonHandler jHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createButtonHandlers();
		jHandler = new JsonHandler();
		initializeApplicationDirectory();
		
		AccelerationHandler.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		try {
			initializeSettings();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO: make singleton
		//networkHandler = new NetworkActivity();
	}

	private void createButtonHandlers() {
		start_button = (Button)findViewById(R.id.button1);
		settings_button = (ImageButton)findViewById(R.id.imageButton1);
		
		settings_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		start_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SensorActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initializeApplicationDirectory(){
		JsonHandler.applicationDirectory = Environment.getExternalStorageDirectory() +"/"+ getPackageName();
	}
	
	private void initializeSettings() throws JSONException{
		//jHandler.loadSettingsFromFile();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}