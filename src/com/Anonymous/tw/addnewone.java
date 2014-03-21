package com.Anonymous.tw;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class addnewone extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewone);
	
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
	    public boolean onCreateOptionsMenu(Menu menu) {

		  getMenuInflater().inflate(R.menu.addnewone, menu);
	        ActionBar actionBar = getActionBar();  
	        actionBar.setDisplayHomeAsUpEnabled(true);  
//	        MenuItem AddItem = menu.findItem(R.id.menu_next);
	     

	        return super.onCreateOptionsMenu(menu);
	    }
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	      // Handle presses on the action bar items
			 EditText TitleName = (EditText)findViewById(R.id.AnonymousTitle);
			 EditText Content =  (EditText)findViewById(R.id.AnonymousContent);
			 EditText Editor =  (EditText)findViewById(R.id.Editor);
			
	      switch (item.getItemId()) {
	      	  case android.R.id.home:
	            this.finish();
	            return true;
	          case R.id.menu_next:
	        	 
       	         	
	            	if("".equals(TitleName.getText().toString()) || "".equals(Content.getText().toString())){
	            		Log.d("d","不能空白");
	            		Toast.makeText(addnewone.this, "不能空白唷",Toast.LENGTH_SHORT).show();
	            	}
	            		else{
	            			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
	    							"TradeinServices");
	            		System.out.println(TitleName); 
	            		System.out.println(Content);
	            		ParseObject it = new ParseObject("TradeinServices");
	            		it.put("TitleName", TitleName.getText().toString());
						it.put("Content", Content.getText().toString());
						
						it.put("UserName",Editor.getText().toString() ); 
						it.put("ClickNumber", 0);
						it.saveInBackground();
						query.orderByDescending("ClickNumber");
						Toast.makeText(addnewone.this, "新增成功",Toast.LENGTH_SHORT).show();
						FlurryAgent.logEvent("addnewoneFinish");
						   new Thread(new Runnable() {
				               @Override
				               public void run() {
				                   try {
				                       Thread.sleep(100000);
				                   }
				                   catch (Exception e) { }
				                   System.exit(0);
				               }
				           }).start();
						   Intent intent = new Intent(this, MainActivity.class);
//				        	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				        	  startActivity(intent);
//				        	 this.finish();
				   } 
					   
			        	 
	            		

	        	  return true;
	 //         case R.id.menu_settings:
	            //  openSettings();
	             // return true;
	        
	      
	          default:
	              return super.onOptionsItemSelected(item);
	  }
	}
}