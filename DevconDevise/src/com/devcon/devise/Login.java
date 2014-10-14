package com.devcon.devise;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends Activity {
	EditText txtusername, txtpassword;
	String username, password;
	final String APPLICATION_ID = "GBdi0gjuAzS7MilcllE4vgMpEaJ8NdFCGsLMIJci";
	final String CLIENT_KEY = "R3ww5CExVqUo9LCo13d4dO2mhNA1RHicuTcGpnLf";
	private Dialog progressDialog;
	
	Boolean isInternetPresent = false;
	ConnectionDetector cd =  new ConnectionDetector(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtusername = (EditText)findViewById(R.id.txtusername);
		txtpassword = (EditText)findViewById(R.id.txtpassword);
		getActionBar().hide();
		
        onCreateParse();
        
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			checkCurrentUser();
		}
		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
	}
	public void onCreateParse() { 
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
	}
	private boolean isEmpty(EditText txt) {
 	    if (txt.getText().toString().trim().length() > 0) {
 	        return false;
 	    } else {
 	        return true;
 	    }
 	}
	private void checkCurrentUser(){
		if(ParseUser.getCurrentUser() != null){
			showHome();
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
	public void attemptLogin(){
		username = txtusername.getText().toString();
		password = txtpassword.getText().toString();
		if(isEmpty(txtusername)){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtusername).startAnimation(shake);
		}
		if(isEmpty(txtpassword)){
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
 	        findViewById(R.id.txtpassword).startAnimation(shake);
		}
		if(!isEmpty(txtusername) && !isEmpty(txtpassword)){
			showProgressBar("Logging in...");
			login(username.toLowerCase(Locale.getDefault()), password);
		}
	}
	private void showHome(){
		Intent i = new Intent(Login.this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", 0);
		i.putExtras(bundle);
		startActivity(i);
		finish();
	}
	private void login(String lowerCase, String password) {
		ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if(user != null && e == null){
					showHome();
				}else{
					showAlert("Please enter username and password you use to log in");
				}
				dismissProgressBar();
			}
		});

	}
	public void clickLogin(View v){
		if (isInternetPresent) {
			attemptLogin();
		}else {
			showAlert("Please check your internet connection and try again.");
		}
	}
}
