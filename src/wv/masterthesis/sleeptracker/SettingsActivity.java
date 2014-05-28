package wv.masterthesis.sleeptracker;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity{

	Button okButton;
	RadioGroup genderGroup;
	RadioButton genderRadioButton;
	EditText age;
	EditText weight;
	EditText height;
	JsonHandler jHandler = new JsonHandler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createButtonHandlers();
    }
    
    private void createButtonHandlers(){
    	okButton = (Button)findViewById(R.id.button1);
    	genderGroup = (RadioGroup)findViewById(R.id.radioGender);
    	age = (EditText)findViewById(R.id.ageTextEdit);
    	weight = (EditText)findViewById(R.id.weightTextEdit);
    	height = (EditText)findViewById(R.id.heightTextEdit);
    	
    	okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int selectedGenderID = genderGroup.getCheckedRadioButtonId();
				genderRadioButton = (RadioButton)findViewById(selectedGenderID); 
				
				JSONObject currentUserData = new JSONObject();
				try {
					currentUserData.put("age", ( !age.getText().toString().matches("") ) ? age.getText().toString()  : "empty" );
					currentUserData.put("gender", ( genderRadioButton != null ) ? (String)genderRadioButton.getText() : "empty" );
					currentUserData.put("weight", ( !weight.getText().toString().matches("") ) ? weight.getText().toString()  : "empty" );
					currentUserData.put("height", ( !height.getText().toString().matches("") ) ? height.getText().toString()  : "empty" );
					jHandler.saveUserData( currentUserData );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivity(intent);
			}
		});
    	
//    	okButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(v.getContext(), CalibrateActivity.class);
//				startActivity(intent);
//			}
//		});
    	
//    	female.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(female.isChecked()){
//					female.setChecked(false);
//				}
//			}
//		});
//    	
//    	male.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(male.isChecked()){
//					male.setChecked(false);
//				}
//			}
//		});
    }
}