package wv.masterthesis.sleeptracker;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{

	Button start_button;
	NetworkHandler networkHandler; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		start_button = (Button)findViewById(R.id.button1);
		start_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SensorActivity.class);
				startActivity(intent);
				setContentView(R.layout.activity_sensor);

			}
		});
		
		//TODO: make singletion
		//networkHandler = new NetworkActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}