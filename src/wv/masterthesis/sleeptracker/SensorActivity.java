package wv.masterthesis.sleeptracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
//import android.widget.TextView;

public class SensorActivity extends Activity{

	private AccelerationHandler mAccelerationHandler;
    private AudioHandler mAudioActivity;
    private static JsonHandler jHandler;
            
    Button stop_button;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        createJsonHandler();
        createAudioHandler();
        createStopButton();
        
        mAccelerationHandler = new AccelerationHandler(getApplicationContext(), jHandler);
        mAccelerationHandler.setIsRecording(true);
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
            	mAccelerationHandler.setIsRecording(false);
                
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                setContentView(R.layout.activity_main);
            }
        });
    }
    


//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//      // Inflate the menu; this adds items to the action bar if it is present.
//      getMenuInflater().inflate(R.menu.sensor, menu);
//      return true;
//  }
            
}
