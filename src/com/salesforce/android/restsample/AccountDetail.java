package com.salesforce.android.restsample;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AccountDetail extends Activity {
	public static final String TAG = "AccountDetail";
	private TextView mAccountNameEditText;
	private TextView mAccountBillingStreetEditText;
	private TextView mAccountBillingCityEditText;
	private TextView mAccountBillingStateEditText;
	private GlobalState gs;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        
  	  	gs = (GlobalState) getApplication();
  	  	JSONObject acct = gs.getSelectedAccount();
  	  
		mAccountNameEditText = (TextView) findViewById(R.id.accountNameText);
		mAccountBillingStreetEditText = (TextView) findViewById(R.id.billingStreetText);
		mAccountBillingCityEditText = (TextView) findViewById(R.id.billingCityText);
		mAccountBillingStateEditText = (TextView) findViewById(R.id.billingStateText);

		try {
			mAccountNameEditText.setText(acct.getString("Name"));
			mAccountBillingStreetEditText.setText(acct.getString("BillingStreet")!= "null"?acct.getString("BillingStreet"):"");
			mAccountBillingCityEditText.setText(acct.getString("BillingCity")!= "null"?acct.getString("BillingCity"):"");
			mAccountBillingStateEditText.setText(acct.getString("BillingState")!= "null"?acct.getString("BillingState"):"");			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Button updateBtn = (Button)findViewById(R.id.btnUpdate);
		updateBtn.setOnClickListener(new OnClickListener() {	
			//@Override
			public void onClick(View v) {
				launchUpdateAccount();
			}			  
		});
    

		Button deleteBtn = (Button)findViewById(R.id.btnDelete);
		deleteBtn.setOnClickListener(new OnClickListener() {	
			//@Override
			public void onClick(View v) {
				deleteAccount();
			}			  
		});
}

	protected void launchUpdateAccount() {
        Intent i = new Intent(this, UpdateAccount.class);
        startActivity(i);
    }	


	protected void deleteAccount() {
		OAuthTokens myTokens = gs.getAccessTokens();
 		JSONObject acct = gs.getSelectedAccount();
 		
        try {
			String url = myTokens.get_instance_url() + "/services/data/v20.0/sobjects/Account/"+acct.getString("Id");
			HttpDelete del = new HttpDelete(url);

			del.setHeader("Authorization", "OAuth " + myTokens.get_access_token());
			del.setHeader("Content-type", "application/json");

			DefaultHttpClient client = new DefaultHttpClient();
			client.execute(del);

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