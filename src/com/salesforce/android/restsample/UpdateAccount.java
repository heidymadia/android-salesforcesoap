package com.salesforce.android.restsample;

import java.io.IOException;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateAccount extends Activity {
	public static final String TAG = "AccountUpdt";
	private EditText mAccountNameEditText;
	private EditText mAccountBillingStreetEditText;
	private EditText mAccountBillingCityEditText;
	private EditText mAccountBillingStateEditText;
	private Button mAccountSaveButton;
	private GlobalState globalState;
	private JSONObject acct;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        
        globalState = (GlobalState) getApplication();
  	  	acct = globalState.getSelectedAccount();
        
        setContentView(R.layout.update_item);
  	  
		mAccountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
		mAccountBillingStreetEditText = (EditText) findViewById(R.id.accountBillingStreetEditText);
		mAccountBillingCityEditText = (EditText) findViewById(R.id.accountBillingCityEditText);
		mAccountBillingStateEditText = (EditText) findViewById(R.id.accountBillingStateEditText);
		mAccountSaveButton = (Button) findViewById(R.id.accountSaveButton);

		try {
			mAccountNameEditText.setText(acct.getString("Name"));
			mAccountBillingStreetEditText.setText(acct.getString("BillingStreet")!= "null"?acct.getString("BillingStreet"):"");
			mAccountBillingCityEditText.setText(acct.getString("BillingCity")!= "null"?acct.getString("BillingCity"):"");
			mAccountBillingStateEditText.setText(acct.getString("BillingState")!= "null"?acct.getString("BillingState"):"");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mAccountSaveButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        onSaveButtonClicked();
		    }
		});
		
    }
	
	private void onSaveButtonClicked() {
    		
        try {
    		OAuthTokens myTokens =globalState.getAccessTokens();
    		
    		String url = myTokens.get_instance_url() + "/services/data/v20.0/sobjects/Account/"+acct.getString("Id")+"?_HttpMethod=PATCH";
			HttpPost post = new HttpPost(url);
				
			JSONObject data = new JSONObject();

			data.put("BillingStreet", mAccountBillingStreetEditText.getText().toString());
			data.put("BillingCity", mAccountBillingCityEditText.getText().toString());
			data.put("BillingState", mAccountBillingStateEditText.getText().toString());
			data.put("Name", mAccountNameEditText.getText().toString());
				
			StringEntity se = new StringEntity(data.toString());
			post.setEntity(se);
			post.setHeader("Authorization", "OAuth " + myTokens.get_access_token());
			post.setHeader("Content-type", "application/json");
				
			DefaultHttpClient client = new DefaultHttpClient();
        	client.execute(post);
            
        	Intent i = new Intent(this, AccountListView.class);
            startActivity(i);
	        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        finish();
    }
}
