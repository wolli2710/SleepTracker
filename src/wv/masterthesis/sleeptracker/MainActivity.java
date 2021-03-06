package wv.masterthesis.sleeptracker;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{

	Button startButton;
	Button settingsButton;
	Button clearButton;

	JsonHandler jHandler;
	final Context context = this;
	
	TextView currentUserData;
	JSONObject userData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createButtonHandlers();
		jHandler = new JsonHandler();
		initializeApplicationDirectory();
		showCurrentUserData();
	}

	private void showCurrentUserData() {
		userData = jHandler.getCurrentUserData();
		if(userData != null){
			currentUserData = (TextView)findViewById(R.id.currentUserData);
			String userDataString = "";
			
			try {
				userDataString += "Weight:\t\t"+ userData.getString("weight").toString()+"\n";
				userDataString += "Height:\t\t"+ userData.getString("height").toString() +"\n";
				userDataString += "Age:\t\t\t\t"+ userData.getString("age").toString() +"\n";
				userDataString += "Gender:\t"+ userData.getString("gender").toString() +"\n";
				currentUserData.setText( userDataString );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void createButtonHandlers() {
		startButton = (Button)findViewById(R.id.button1);
		settingsButton = (Button)findViewById(R.id.button_setUserData);
		clearButton = (Button)findViewById(R.id.button_clear);
		
		createSettingsButtonListener();
		createStartButtonListener();
		createClearButtonListener();
	}

	private void createClearButtonListener() {
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jHandler.resetUserData();
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivity(intent);
			}
		});
	}

	private void createStartButtonListener() {
		startButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(jHandler.getCurrentUserData() != null){
					Intent intent = new Intent(v.getContext(), SensorActivity.class);
					startActivity(intent);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder( context );
					builder.setMessage("You forgot to set the User Data")
							.setTitle("Data Missing")
							.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
					new Throwable("forgot to set User Data!");
				}
			}
		});
	}

	private void createSettingsButtonListener() {
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initializeApplicationDirectory(){
		JsonHandler.applicationDirectory = Environment.getExternalStorageDirectory() +"/"+ getPackageName();
		try {
			jHandler.writeDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}