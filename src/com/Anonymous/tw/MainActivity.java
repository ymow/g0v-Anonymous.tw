package com.Anonymous.tw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;
 

public class MainActivity extends Activity {
 
		// Declare Variables
		ListView listview;
		List<ParseObject> ob;
		ProgressDialog mProgressDialog;
		ArrayAdapter<String> adapter;
		ArrayList<String>  RestaurantRank = new ArrayList<String>();
		
		  private AdView adView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Get the view from listview_main.xml
			setContentView(R.layout.listview_main);
		    Boolean b;
		    b=isNetworkAvailable();  //true if connection,  false if not

			 if(!b){
			       //do accordingly to no-connection
				     Log.d("no network","no network");
			    	 Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
//			    	 System.exit(0);
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

			
			// Save the current Installation to Parse.
			ParseInstallation.getCurrentInstallation().saveInBackground();
			// When users indicate they are Giants fans, we subscribe them to that channel.
			PushService.setDefaultPushCallback(this, MainActivity.class);
//			subscribe Channel
//			PushService.subscribe(this, "AVDbRank", MainActivity.class);
			ParseAnalytics.trackAppOpened(getIntent());
//			Sent Push notification to channel
//			ParsePush push = new ParsePush();
//			push.setChannel("AVDbRank");
//			push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//			push.sendInBackground();

	       // Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();
		 }
		}

		// RemoteDataTask AsyncTask
		private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Create a progressdialog
				mProgressDialog = new ProgressDialog(MainActivity.this);
				// Set progressdialog title
				mProgressDialog.setTitle("Trade in Services");
				// Set progressdialog message
				mProgressDialog.setMessage("Loading...");
				mProgressDialog.setIndeterminate(false);
				// Show progressdialog
				mProgressDialog.show();
			 
			}

			@Override
			protected Void doInBackground(Void... params) {
				// Locate the class table named "Country" in Parse.com
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"TradeinServices");
				//add new 
//				ParseObject Restaurant = new ParseObject("EatSomething");
//				addGirl.put("GirlName", "XXX");
//				addGirl.put("VideoName", "Where should we go for lunch?");
//				addGirl.put("ClickNumber", "0");
//				addGirl.saveInBackground();
//				query.orderByDescending("_created_at");
				//Ranking
				query.orderByDescending("ClickNumber");
				
				try {
					ob = query.find();
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate the listview in listview_main.xml
				listview = (ListView) findViewById(R.id.listview);
							// Pass the results into an ArrayAdapter
				adapter = new ArrayAdapter<String>(MainActivity.this,
						R.layout.customfont);

				int i=0;
	 
		 
				// Retrieve object "name" from Parse.com database
				for (ParseObject Rank : ob) {
					i++;
					
					adapter.add(i+"."+(String) Rank.get("TitleName"));
				 Log.d("TradeinServices",(String) Rank.get("TitleName"));
				 
				//GirlList.add((String) AVgirl.get("GirlName"));
				}
			 
				
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);
				// Close the progressdialog
				mProgressDialog.dismiss();
 
				// Capture button clicks on ListView items
				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
	 		 
						// Send single item click data to SingleItemView Class
						Intent i = new Intent(MainActivity.this,SingleItemView.class);

						// Pass data "name" followed by the position
						Log.d("TitleName", ob.get(position).getString("TitleName").toString());
						Log.d("UserName", ob.get(position).getString("UserName").toString());
						Log.d("Content", ob.get(position).getString("Content").toString());
						i.putExtra("TitleName", ob.get(position).getString("TitleName").toString());
						i.putExtra("UserName", ob.get(position).getString("UserName").toString());
						i.putExtra("Content", ob.get(position).getString("Content").toString());
						 	
						FlurryAgent.logEvent("ListView Click");
						ParseObject Object = ob.get(position);
						String PlusAdd = Object.getObjectId();
	
				        ParseQuery<ParseObject> query = ParseQuery.getQuery("TradeinServices");
				        query.getInBackground(PlusAdd, new GetCallback<ParseObject>() {
				          public void done(ParseObject object, ParseException e) {
				            if (e == null) {

				             int s = object.getInt("ClickNumber");
				             System.out.println("ClickNumber = " + s + "+1");
					 
				             
								adapter. notifyDataSetChanged();
				             object.put("ClickNumber",  s+1);
				             object.saveInBackground();
				             
				            } else {
				              
				            }
				          }
				        });
				        
						// Open SingleItemView.java Activity

						startActivity(i);
					}
				});
			}


		}
		 @Override
		  public void onPause() {
		    super.onPause();

		  }
			@Override
			protected void onStart()
			{
				super.onStart();
				FlurryAgent.onStartSession(this, "Q7W68BY9D995ZWS4WT6F");
			}
			 
			@Override
			protected void onStop()
			{
				super.onStop();		
				FlurryAgent.onEndSession(this);
			}
		  @Override
		  public void onResume() {
		    super.onResume();
 
		    Boolean b;
		    b=isNetworkAvailable();  //true if connection,  false if not

		   if(!b){
		       //do accordingly to no-connection
			     Log.d("no network","no network");
		    	 Toast.makeText(MainActivity.this, "無法連接網路，請檢查網路連線", Toast.LENGTH_LONG).show();
//		    	 System.exit(0);
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
		 
		   }else{}
		  }

		  @Override
		  public void onDestroy() {
		 
		    super.onDestroy();
		  }
//			//check internet connetion
		  private boolean isNetworkAvailable() {
			    ConnectivityManager connectivityManager 
			          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
			}

		  @Override
		    public boolean onCreateOptionsMenu(Menu menu) {

			  getMenuInflater().inflate(R.menu.activity_main, menu);
		        ActionBar actionBar = getActionBar();  
		        actionBar.setDisplayHomeAsUpEnabled(false);  
		        MenuItem AddItem = menu.findItem(R.id.menu_add);
		     

		        return super.onCreateOptionsMenu(menu);
		    }
		  @Override
		  public boolean onOptionsItemSelected(MenuItem item) {
		      // Handle presses on the action bar items
		      switch (item.getItemId()) {
		          case R.id.menu_add:

						FlurryAgent.logEvent("addnewone");
		        	  Intent intent = new Intent(this, addnewone.class);
//		        	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	  startActivity(intent);
		        	 this.finish();
		        	  return true;
		 //         case R.id.menu_settings:
		            //  openSettings();
		             // return true;
		          default:
		              return super.onOptionsItemSelected(item);
		      }
		  }


		public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
//	            startActivity(new Intent(SingleItemView.this,
//	                    MainActivity.class));
	            finish();
	            System.exit(0);
//	            ConfirmExit();//按返回鍵，則執行退出確認
	            return true;   
	        }   
	        return super.onKeyDown(keyCode, event);   
	    }

	}