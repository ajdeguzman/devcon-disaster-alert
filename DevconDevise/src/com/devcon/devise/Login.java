package com.devcon.devise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends Activity {
	EditText txtUsername, txtPword;
	String username, password;
	final String APPLICATION_ID = "GBdi0gjuAzS7MilcllE4vgMpEaJ8NdFCGsLMIJci";
	final String CLIENT_KEY = "R3ww5CExVqUo9LCo13d4dO2mhNA1RHicuTcGpnLf";
	private Dialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtUsername = (EditText)findViewById(R.id.txtusername);
		txtPword = (EditText)findViewById(R.id.txtpassword);
		getActionBar().hide();
		
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
	}
	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
 	}
	public void showProgressBar(String msg){
		   progressDialog = ProgressDialog.show(this, "", msg, true);
	}
	public void dismissProgressBar(){
		   if(progressDialog != null && progressDialog.isShowing())
		         progressDialog.dismiss();
	}
	private void showAlert(String msg){
		 AlertDialog.Builder bldr = new AlertDialog.Builder(this);
			bldr.setTitle("Disaster Alert");
			bldr.setMessage(msg);
			bldr.setCancelable(true);
			bldr.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			final AlertDialog alt = bldr.create();
			alt.show();
	}
	public void clickLogin(View v){
		username = txtUsername.getText().toString();
		password = txtPword.getText().toString();
		if(!isEmpty(txtUsername) && !isEmpty(txtPword)){
			showProgressBar("Logging in...");
			ParseUser.logInInBackground(username, password, new LogInCallback(){
				@Override
				public void done(ParseUser user, ParseException arg1) {
					if(user != null){
						Intent i = new Intent(Login.this, MainActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("position", 0);
						i.putExtras(bundle);
						startActivity(i);
						finish();
					}else{
						showAlert("Please enter the email or phone number and password you use to log in");
					}
					dismissProgressBar();
				}
			});
		}
	}
}
