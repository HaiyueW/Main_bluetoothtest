package com.ece4600.mainapp;

import com.ece4600.mainapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener{
	EditText txtuser;
	EditText txtpass;
	Button login;
	Button cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtuser = (EditText)findViewById(R.id.txtuser);
		txtpass = (EditText)findViewById(R.id.txtpass);
		login = (Button)findViewById(R.id.login);
		cancel = (Button)findViewById(R.id.cancel);
		login.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

@Override
public void onClick(View v) {
	String user = txtuser.getText().toString();
	String pass = txtpass.getText().toString();
	
	switch(v.getId()){
	case R.id.login:
		if(user.equals("user") && pass.equals("pass")){
			Intent i = new Intent(Login.this,Bluetooth.class);
			startActivity(i);
			Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("user", txtuser.getText().toString());
			editor.putString("pass", txtpass.getText().toString());
			editor.commit();
		}
		else{
			Message(v);
		}
		break;
	case R.id.cancel:
		txtuser.setText("");
		txtpass.setText("");
		break;
	default:
		break;
	}
	
}

public void Message(View v){
	AlertDialog.Builder alertDialogHint = new AlertDialog.Builder(this);
	alertDialogHint.setMessage("Wrong Username/Password");
	alertDialogHint.setNeutralButton("OK",
	new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent i = new Intent(Login.this,Login.class);
			startActivity(i);
			
		}
	});
	AlertDialog alertDialog = alertDialogHint.create();
	alertDialog.show();

}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
