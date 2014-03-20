/*AVDB Copyright*/
package com.Anonymous.tw;
 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {
protected boolean _active = true;
protected int _splashTime = 3000; // time to display the splash screen in ms
String STORE_NAME = "UserID"; 
String UserID = null;
 
private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
}
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.splash);
    Boolean b;
    b=isNetworkAvailable();  //true if connection,  false if not

   if(!b){
       //do accordingly to no-connection
	     Log.d("no network","no network");
    	 Toast.makeText(Splash.this, "無法連接網路，請檢查網路連線", Toast.LENGTH_LONG).show();
//    	 System.exit(0);
    	   new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       Thread.sleep(5000);
                   }
                   catch (Exception e) { }
                   System.exit(0);
               }
           }).start();
 
   }else{
	   
   
    SharedPreferences settings = getSharedPreferences("Preference", 1);
//		clear SharedPreferences @ /data/data/[package name]/shared_prefs/[preferences filename].xml
//    SharedPreferences.Editor editor = settings.edit();
//    editor.clear();
//    editor.commit();
    UserID = settings.getString("UserID", "");
    

	
	    Thread splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                int waited = 0;
	                while (_active && (waited < _splashTime)) {
	                    sleep(100);
	                    if (_active) {
	                        waited += 100;
	                    }
	                }
	            } catch (Exception e) {
	
	            } finally {
	            	  if(UserID == ""){
	            	        Log.d("No UserID, Sign up", UserID);
	            	        AlertDialog.Builder alert = new AlertDialog.Builder(Splash.this);
	            			
	            	        startActivity(new Intent(Splash.this,
	            	                MainActivity.class));
	            	        finish();


	            		    	
	            		}else {
	            			
			                startActivity(new Intent(Splash.this,
			                        MainActivity.class));
			                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			                finish();
			            		}
	            }
	        };
	             };
	        		 
	    splashTread.start();
		}

	}

}