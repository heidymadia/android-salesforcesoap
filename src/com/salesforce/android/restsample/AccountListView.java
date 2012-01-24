package com.salesforce.android.restsample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AccountListView extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  GlobalState gs = (GlobalState) getApplication();
	  setContentView(R.layout.newlist);
	  getAccountData();
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, gs.getAccountNames()));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    	GlobalState gs = (GlobalState) getApplication();
	    	gs.setSelectedAccount(gs.getAccounts()[position]);
	    	launchAccountDetail();
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	  Button newBtn = (Button)findViewById(R.id.btnFromDate);
	  newBtn.setOnClickListener(new OnClickListener() {

		//@Override
		public void onClick(View v) {
	    	GlobalState gs = (GlobalState) getApplication();
	    	gs.setSelectedAccount(gs.getAccounts()[0]);
			launchAddAccount();
		}
		  
	  });
	}
	
	protected void launchAddAccount() {
		Intent i = new Intent(this, AddAccount.class);
		startActivity(i);
	}
	protected void launchAccountDetail() {
        Intent i = new Intent(this, AccountDetail.class);
        startActivity(i);
    }

	private void getAccountData() {
		GlobalState globalState = (GlobalState) getApplication();
		OAuthTokens myTokens = globalState.getAccessTokens();
		
		DefaultHttpClient client = new DefaultHttpClient(); 
		String url = myTokens.get_instance_url() + "/services/data/v20.0/query/?q=";
		String soqlQuery = "Select Id, Name, BillingStreet, BillingCity, BillingState From Account limit 20";
		
		try
		{
			url += URLEncoder.encode(soqlQuery, "UTF-8");
		}
		catch(UnsupportedEncodingException e){}
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("Authorization", "OAuth " + myTokens.get_access_token());
		
		try {
			HttpResponse response = client.execute(getRequest);
			
			String result = EntityUtils.toString(response.getEntity()); 
	
			JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
			JSONArray records = object.getJSONArray("records");
			
			globalState.setAccountNames(new String[records.length()]);
			globalState.setAccounts(new JSONObject[records.length()]);
			
			for (int i=0;i<records.length();i++) {
				JSONObject record = (JSONObject) records.get(i);
				String accountName = record.getString("Name");
				globalState.getAccountNames()[i] = accountName;
				globalState.getAccounts()[i] = record;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}
}
