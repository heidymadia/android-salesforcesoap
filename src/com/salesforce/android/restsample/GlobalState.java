package com.salesforce.android.restsample;

import org.json.JSONObject;

import android.app.Application;

public class GlobalState extends Application {

	private String[] accountNames;
	private JSONObject selectedAccount;
	private JSONObject[] accounts;
	private OAuthTokens accessTokens;
	
	public OAuthTokens getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(OAuthTokens accessTokens) {
		this.accessTokens = accessTokens;
	}

	public JSONObject getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(JSONObject selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public JSONObject[] getAccounts() {
		return accounts;
	}

	public void setAccounts(JSONObject[] accounts) {
		this.accounts = accounts;
	}

	public String[] getAccountNames() {
		return accountNames;
	}
	
	public void setAccountNames(String[] theaccounts) {
		accountNames = theaccounts;
	}
}