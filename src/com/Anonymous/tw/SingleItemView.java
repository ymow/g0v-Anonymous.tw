package com.Anonymous.tw;

 

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
 
 
public class SingleItemView extends Activity {
	private static final String  BrowserBug = null;
	// Declare Variables
 
      String UserName,TitleName,Content,PlusAdd;
 
	  private ShareActionProvider mShareActionProvider;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.singleitemview);
		Intent i = getIntent();
		  // get action bar   
        ActionBar actionBar = getActionBar();
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		 getActionBar().setDisplayUseLogoEnabled(true);
	    
		// Retrieve data from MainActivity on item click event

 
			
		// Get the name
		TitleName = i.getStringExtra("TitleName");
		UserName = i.getStringExtra("UserName");
		Content = i.getStringExtra("Content");
		
		// Locate the TextView in singleitemview.xml
	TextView txtname1 = (TextView) findViewById(R.id.TitleName);
	TextView txtname2 = (TextView) findViewById(R.id.UserName);
	TextView txtname3 = (TextView) findViewById(R.id.Content);
	
		// Load the text into the TextView
	txtname1.setText(TitleName);
//	txtname2.setText(UserName);
	txtname3.setText(Content);
	 getActionBar().setTitle(UserName);
	 

	           
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
	    // Inflate menu resource file.
	    getMenuInflater().inflate(R.menu.singleitemview, menu);
 
	    
	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_share);
 
//	    // Fetch and store ShareActionProvider
	    mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();
	    mShareActionProvider.setShareIntent(getDefaultShareIntent());
	    return true;
	}

	private Intent getDefaultShareIntent() {
		// TODO Auto-generated method stub
		  String playStoreLink = "https://play.google.com/store/apps/details?id=" +
  		        getPackageName();
  		String yourShareText = UserName + "在立法院分享了"+TitleName+"，與我們一起跟民主奮戰，立刻下載與安裝："+TitleName  + playStoreLink;

		 Intent intent = new Intent(Intent.ACTION_SEND);
//	        intent.setComponent(new ComponentName("jp.naver.line.android",
//	                "com.facebook.katana"));
	        intent.setType("text/plain"); 
	        intent.putExtra(Intent.EXTRA_SUBJECT, "匿名者");
	        intent.putExtra(Intent.EXTRA_TEXT, yourShareText);
	        return intent;
	     
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent);
	    }
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     
         
	        case android.R.id.home:
	        	startActivity(new Intent(SingleItemView.this,MainActivity.class));
	            
	        	this.finish();
	           
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {//???????????????
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
            startActivity(new Intent(SingleItemView.this,
                    MainActivity.class));
            finish();
//            ConfirmExit();//????????????????????????????????????
            return true;   
        }   
        return super.onKeyDown(keyCode, event);   
    }
}