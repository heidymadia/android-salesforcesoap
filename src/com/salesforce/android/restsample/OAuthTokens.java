/* Simple 'DAO' wrapper class that is used to store all the OAuth values returned by Salesforce via the redirect URI callback
 */
package com.salesforce.android.restsample;

import java.util.Calendar;

public class OAuthTokens {
	    	private String _access_token;
	    	private String _refresh_token;
	    	private String _instance_url;
	    	private String _id;
	    	private Calendar _issued_at;
	    	private String _signature;
			public String get_access_token() {
				return _access_token;
			}
			public void set_access_token(String _access_token) {
				this._access_token = _access_token;
			}
			public String get_refresh_token() {
				return _refresh_token;
			}
			public void set_refresh_token(String _refresh_token) {
				this._refresh_token = _refresh_token;
			}
			public String get_instance_url() {
				return _instance_url;
			}
			public void set_instance_url(String _instance_url) {
				this._instance_url = _instance_url;
			}
			public String get_id() {
				return _id;
			}
			public void set_id(String _id) {
				this._id = _id;
			}
			public Calendar get_issued_at() {
				return _issued_at;
			}
			public void set_issued_at(Long issued_at) {
				this._issued_at = Calendar.getInstance();
				this._issued_at.setTimeInMillis(issued_at);
			}
			public String get_signature() {
				return _signature;
			}
			public void set_signature(String _signature) {
				this._signature = _signature;
			}
	      
}