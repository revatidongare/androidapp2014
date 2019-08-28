package s.o.s;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SosActivity extends Activity implements OnClickListener {

	int i;
	Button btnEm;
	Button btnSave;
	Button btnEdit;
	EditText etPhone;
	EditText etPhone2;
	String phone[];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnEm = (Button) findViewById(R.id.btnEmeregency);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etPhone2 = (EditText) findViewById(R.id.etphone2);
        
		btnEm.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnEdit.setOnClickListener(this);

		loadsaveddata();

	}

	public void loadsaveddata() {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean checkBoxValue = sharedPreferences.getBoolean("Check_Save_Value",
				true);
		String number = sharedPreferences.getString("Stored_Number", "Enter number");
		
		if (checkBoxValue){
			btnEm.setEnabled(false);
		}else{
		 btnEm.setEnabled(true);
        etPhone.setText(number);
		etPhone.setEnabled(false);
		etPhone2.setEnabled(false);
		btnSave.setEnabled(false);
		}

	}

	public void onClick(View v) {

		if (v.getId() == R.id.btnEmeregency) 
		{

			int mcc = 0;
			int mnc = 0;

			Toast.makeText(getApplicationContext(), "Emergency",
					Toast.LENGTH_SHORT).show();

			final TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
				final GsmCellLocation location = (GsmCellLocation) telephony
						.getCellLocation();
				String networkOperator = telephony.getNetworkOperator();
				Log.d("network operator is", "" + networkOperator);

				if (networkOperator != null) {

					Log.d("in network", "not null");
					mcc = Integer.parseInt(networkOperator.substring(0, 3));
					mnc = Integer.parseInt(networkOperator.substring(3));
				}

				if (location != null) {
					Log.d("in location", "not null");
					SmsManager sms = SmsManager.getDefault();
					if (etPhone.isEnabled()) {
						Toast.makeText(getApplicationContext(),
								"Please save number", Toast.LENGTH_SHORT)
								.show();
					} else {

						String phone[] = {etPhone.getText().toString(),etPhone2.getText().toString()};
						for(i=0;i<phone.length;i++){
						sms.sendTextMessage(phone[i], null,
								"i'm in troble plssss help mee " + "\nmobile country code (MCC) : "
										+ mcc + "\nmobile network code(MNC) : " + mnc + "\nlocation area code(LAC): "
										+ location.getLac() + "\nCELLID: "
										+ location.getCid(), null, null);
						Toast.makeText(getApplicationContext(),
								"sending message to " + phone[i],
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			}
		}

		if (v.getId() == R.id.btnSave) {

			String phone[] = {etPhone.getText().toString(),etPhone2.getText().toString()};
			for(i=0;i<phone.length;i++){
				
			if (phone[0].equals("")&& phone[1].equals("")) {
				Toast.makeText(getApplicationContext(), "Please Enter number",
						Toast.LENGTH_SHORT).show();
			} else {
				
				etPhone.setEnabled(false);
				etPhone2.setEnabled(false);
				btnSave.setEnabled(false);
				btnEm.setEnabled(true);
				
				String phone1[] = {etPhone.getText().toString(),etPhone2.getText().toString()};
				savePreferences("Check_Save_Value", btnSave.isEnabled());
				savePreferences("Stored_Number",phone1[i] );
				Toast.makeText(getApplicationContext(), "Number saved",
						Toast.LENGTH_SHORT).show();
			}

		}
			}

		if (v.getId() == R.id.btnEdit) {

			boolean checkSAVE = btnSave.isEnabled();
			String phone[] = {etPhone.getText().toString(),etPhone2.getText().toString()};
			for(i=0;i<phone.length;i++){
			if (checkSAVE) {

				Toast.makeText(getApplicationContext(),
						"Please Enter number and save", Toast.LENGTH_SHORT)
						.show();
			} else {
				btnSave.setEnabled(true);
				etPhone2.setEnabled(true);
				etPhone.setEnabled(true);
				Toast.makeText(getApplicationContext(),
						"Please Enter number and save", Toast.LENGTH_SHORT)
						.show();
			}
			}
		}}

	

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SosActivity.this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}
    }

