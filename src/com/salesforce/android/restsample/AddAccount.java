package com.salesforce.android.restsample;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAccount extends Activity {
	public static final String TAG = "AccountAdder";
	private EditText mAccountNameEditText;
	private EditText mAccountBillingStreetEditText;
	private EditText mAccountBillingCityEditText;
	private EditText mAccountBillingStateEditText;
	private Button mAccountSaveButton;
	private GlobalState globalState;
	private String errorMsg;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        
        globalState = (GlobalState) getApplication();
        
        setContentView(R.layout.new_item);
  	  
		mAccountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
		mAccountBillingStreetEditText = (EditText) findViewById(R.id.accountBillingStreetEditText);
		mAccountBillingCityEditText = (EditText) findViewById(R.id.accountBillingCityEditText);
		mAccountBillingStateEditText = (EditText) findViewById(R.id.accountBillingStateEditText);
		mAccountSaveButton = (Button) findViewById(R.id.accountSaveButton);

		mAccountSaveButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        onSaveButtonClicked();
		    }
		});
		
    }
	
	private void onSaveButtonClicked() {
     		
        try {
    		
        	OAuthTokens myTokens = globalState.getAccessTokens();

    		String url = myTokens.get_instance_url() + "/services/data/v20.0/sobjects/Account/";
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
        	HttpResponse resp = client.execute(post);
	        	
			String result = EntityUtils.toString(resp.getEntity()); 
        	Log.d(TAG, result);
			JSONObject res = (JSONObject) new JSONTokener(result).nextValue();
			String id = res.getString("id");
			Log.d(TAG, "Salesforce id:"+id);

        	if (resp.getStatusLine().getStatusCode() == 400)
        	{
        		JSONArray value = (JSONArray)new JSONTokener(result).nextValue();
        		JSONObject object = (JSONObject)value.get(0);
        		String errorCode = object.getString("errorCode");
        		if (errorCode != null)
        		{
            		errorMsg = object.getString("message");
            		showDialog(1);
            		return;
        		}
        	}
        	
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
	
	protected Dialog onCreateDialog(int id) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Account could not be created - " + errorMsg)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                
		           }
		       });
		AlertDialog alert = builder.create();
		return alert;
	}

	protected void onPrepareDialog(int id, Dialog dlg, Bundle args) {
		((AlertDialog)dlg).setMessage("Account could not be created - " + errorMsg);
	}
}
