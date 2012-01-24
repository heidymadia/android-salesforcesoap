package com.salesforce.android.restsample;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SfdcRestSample extends Activity {
	
	WebView webview;
	String callbackUrl;	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* As per the OAuth 2.0 User-Agent Flow supported by Salesforce, we pass along the Client Id (aka Consumer Key) as a GET 
         * parameter. We also pass along a special String as the redirect URI so that we can verify it when Salesforce redirects
         * the user back to the mobile device
         */
        String consumerKey = this.getResources().getString(R.string.consumerKey).toString();
        String url = this.getResources().getString(R.string.oAuthUrl).toString();
        callbackUrl = this.getResources().getString(R.string.callbackUrl).toString();
        
        String reqUrl = url + "/services/oauth2/authorize?response_type=token&display=touch&client_id=" + consumerKey + "&redirect_uri=" + callbackUrl;
        
        webview = (WebView) findViewById(R.id.webview);
        
        webview.setWebViewClient(new HelloWebViewClient(this));
        
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(reqUrl);
    }
    
       
    private class HelloWebViewClient extends WebViewClient {
        
    	Activity act;
    	public HelloWebViewClient(Activity myAct) {
    		act = myAct;
    	}
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            
	        Log.d("Sfdc-sample", "Redirect URL: " + url);
	        /* Check to make sure that the redirect URI from Salesforce contains the 'special' text that we passed as the
	         * redirect URI in the initial OAuth request. This confirms that the redirect is indeed from Salesforce.
	         */
	       
	        if (url.startsWith(callbackUrl)) {
        		parseToken(url);
        		Intent i = new Intent(act, AccountListView.class);
        		startActivity(i);
	            return true;
	        } else {
	        	return false;
	        }
        }
                
        /* Per the OAuth 2.0 Use Agent flow supported by Salesforce, the redirect URI will contain the access token (among other
         * other things) after the '#' sign. This method extracts those values and stores them in the GlobalState class. The
         * GolbalState class extends the 'Application' object and can therefore be used to share state between different activities 
         * within the same Android application
         */
    	private void parseToken(String url) {
    		String temp = url.split("#")[1];
    		String[] keypairs = temp.split("&");
    		OAuthTokens myTokens = new OAuthTokens();
    		for (int i=0;i<keypairs.length;i++) {
    			String[] onepair = keypairs[i].split("=");
    			if (onepair[0].equals("access_token")) {
    				myTokens.set_access_token(URLDecoder.decode(onepair[1]));
    			} else if (onepair[0].equals("refresh_token")) {
    				myTokens.set_refresh_token(onepair[1]);
    			} else if (onepair[0].equals("instance_url")) {
    				myTokens.set_instance_url(onepair[1]);
    				myTokens.set_instance_url(myTokens.get_instance_url().replaceAll("%2F", "/"));
    				myTokens.set_instance_url(myTokens.get_instance_url().replaceAll("%3A", ":"));
    			} else if (onepair[0].equals("id")) {
    				myTokens.set_id(onepair[1]);
    			} else if (onepair[0].equals("issued_at")) {
    				myTokens.set_issued_at(Long.valueOf(onepair[1]));
    			} else if (onepair[0].equals("signature")) {
    				myTokens.set_signature(onepair[1]);
    			}
    		}
    		GlobalState gs = (GlobalState) getApplication();
    		gs.setAccessTokens(myTokens);
    	}
    	
    	
    }
    
}