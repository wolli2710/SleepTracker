package wv.masterthesis.sleeptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity{

	Button calibrateButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        createButtonHandlers();
    }
    
    private void createButtonHandlers(){
    	calibrateButton = (Button)findViewById(R.id.button1);
    	
    	calibrateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), CalibrateActivity.class);
				startActivity(intent);
			}
		});
    	
    	calibrateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), CalibrateActivity.class);
				startActivity(intent);
			}
		});
    }
}