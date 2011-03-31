/***
 * 	Copyright (c) 2011 WareNinja.com
 * 	Author: yg@wareninja.com
 *  http://www.WareNinja.net - https://github.com/wareninja	
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
*/

package com.wareninja.android.opensource.oauth2login;

import java.util.HashMap;
import java.util.Map;

import com.wareninja.android.opensource.oauth2login.common.GenericDialogListener;
import com.wareninja.android.opensource.oauth2login.common.LOGGING;
import com.wareninja.android.opensource.oauth2login.common.MCONSTANTS;
import com.wareninja.android.opensource.oauth2login.common.NotifierHelper;
import com.wareninja.android.opensource.oauth2login.common.WebService;
import com.wareninja.android.opensource.oauth2login.facebook.FacebookOAuthDialog;
import com.wareninja.android.opensource.oauth2login.foursquare.FsqOAuthDialog;
import com.wareninja.android.opensource.oauth2login.gowalla.GowallaOAuthDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;

public class AppMainExample extends Activity {
    
	protected static final String TAG = "AppMainExample";
	
	public Context mContext;
	public WebService webService;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        setContentView(R.layout.appmainexample);
    }
    
    public void onClick_fsqLogin(View v) {
    	
    	//NotifierHelper.displayToast(mContext, "onClick_fsqLogin", NotifierHelper.SHORT_TOAST);
    	webService = new WebService();
    	
    	String authRequestRedirect = MCONSTANTS.FSQ_APP_OAUTH_BASEURL+MCONSTANTS.FSQ_APP_OAUTH_URL
		        + "?client_id="+MCONSTANTS.FSQ_APP_KEY
		        + "&response_type=code" 
		        + "&display=touch"
		        + "&redirect_uri="+MCONSTANTS.FSQ_APP_CALLBACK_OAUTHCALLBACK;
		if(LOGGING.DEBUG)Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);
		
		CookieSyncManager.createInstance(this);
		new FsqOAuthDialog(mContext, authRequestRedirect
				, new GenericDialogListener() {
			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)Log.d(TAG, "onComplete->"+values);
				// https://YOUR_REGISTERED_REDIRECT_URI/?code=CODE
				// onComplete->Bundle[{state= , code=....}]
				   
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
				
				String tokenResponse = "";
				try{
					webService.setWebServiceUrl(MCONSTANTS.FSQ_APP_OAUTH_BASEURL);
					// Call Foursquare again to get the access token
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("client_id", MCONSTANTS.FSQ_APP_KEY);
					params.put("client_secret", MCONSTANTS.FSQ_APP_SECRET);
					params.put("grant_type", "authorization_code");
					params.put("redirect_uri", MCONSTANTS.FSQ_APP_CALLBACK_OAUTHCALLBACK);
					params.put("code", values.getString("code"));
					if(LOGGING.DEBUG)Log.d(TAG, "params->" + params);
					
					tokenResponse = webService.webGet(MCONSTANTS.FSQ_APP_TOKEN_URL, params);
					if(LOGGING.DEBUG)Log.d(TAG, "tokenResponse->" + tokenResponse);
					
					broadcastLoginResult(MCONSTANTS.COMMUNITY_FOURSQUARE, tokenResponse);
					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					tokenResponse = null;
				}
		    }
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
		    }
			public void onCancel() {
				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
		    }
		}).show();
    }
    
    public void onClick_gowallaLogin(View v) {
    	//NotifierHelper.displayToast(mContext, "onClick_gowallaLogin", NotifierHelper.SHORT_TOAST);
    	
    	webService = new WebService();
    	String authRequestRedirect = "https://gowalla.com/api/oauth/new"
        + "?client_id="+MCONSTANTS.GOWALLA_APP_KEY
        + "&scope=read-write"
        + "&display=touch"
        + "&redirect_uri="+MCONSTANTS.GOWALLA_APP_CALLBACK_OAUTHCALLBACK;
    	
    	CookieSyncManager.createInstance(this);
    	
    	new GowallaOAuthDialog(mContext, authRequestRedirect
				, new GenericDialogListener() {
			public void onComplete(Bundle values) {

				if(LOGGING.DEBUG)Log.d(TAG, "onComplete->"+values);
				// https://YOUR_REGISTERED_REDIRECT_URI/?code=CODE
				// onComplete->Bundle[{state= , code=....}]
				 
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
				
                // call Gowalla again to get Request Token!!!
                String tokenResponse = "";
				try{
					webService.setWebServiceUrl(MCONSTANTS.GOWALLA_APP_OAUTH_BASEURL);
					// Call Foursquare again to get the access token
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("client_id", MCONSTANTS.GOWALLA_APP_KEY);
					params.put("client_secret", MCONSTANTS.GOWALLA_APP_SECRET);
					params.put("grant_type", "authorization_code");
					params.put("redirect_uri", MCONSTANTS.GOWALLA_APP_CALLBACK_OAUTHCALLBACK);
					params.put("code", values.getString("code"));
					if(LOGGING.DEBUG)Log.d(TAG, "params->" + params);
					
					tokenResponse = webService.webInvokeWithJson(MCONSTANTS.GOWALLA_APP_TOKEN_URL
							, webService.getJsonFromParams(params)
							);
					if(LOGGING.DEBUG)Log.d(TAG, "tokenResponse->" + tokenResponse);
					
					broadcastLoginResult(MCONSTANTS.COMMUNITY_GOWALLA, tokenResponse); 
					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);
					
				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					tokenResponse = null;
				}
		    }
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
		    }
			public void onCancel() {
				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
		    }
		}).show();
    	
    }

	public void onClick_facebookLogin(View v) {
		//NotifierHelper.displayToast(mContext, "TODO: onClick_facebookLogin", NotifierHelper.SHORT_TOAST);
		
    	webService = new WebService();
    	
    	String authRequestRedirect = MCONSTANTS.FB_APP_OAUTH_BASEURL+MCONSTANTS.FB_APP_OAUTH_URL
		        + "?client_id="+MCONSTANTS.FB_APP_ID
		        + "&response_type=token" 
		        + "&display=touch"
		        + "&scope=" + TextUtils.join(",", MCONSTANTS.FB_PERMISSIONS)
		        + "&redirect_uri="+MCONSTANTS.FB_APP_CALLBACK_OAUTHCALLBACK
		        ;
		if(LOGGING.DEBUG)Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);
		
		CookieSyncManager.createInstance(this);
		new FacebookOAuthDialog(mContext, authRequestRedirect
				, new GenericDialogListener() {
			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)Log.d(TAG, "onComplete->"+values);
				// https://YOUR_REGISTERED_REDIRECT_URI/?code=CODE
				// onComplete->Bundle[{expires_in=0, access_token=<ACCESS_TOKEN>}]
/*
if user ALLOWs your app -> Bundle[{expires_in=0, access_token=<ACCESS_TOKEN>}]
if user doesNOT ALLOW -> Bundle[{error=access_denied, error_description=The+user+denied+your+request}]
 */
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
				
				String tokenResponse = "";
				try{
					
					tokenResponse = values.toString();
					
					broadcastLoginResult(MCONSTANTS.COMMUNITY_FACEBOOK, tokenResponse);
					//JSONObject tokenJson = new JSONObject(tokenResponse);
					//if(LOGGING.DEBUG)Log.d(TAG, "tokenJson->" + tokenJson);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					tokenResponse = null;
				}
		    }
			public void onError(String e) {
				if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
		    }
			public void onCancel() {
				if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
		    }
		}).show();
	}
	
	private void broadcastLoginResult(int community, String payload) {
		
		String intentAction = "";
		String intentExtra = "";
		try {
			
			if (MCONSTANTS.COMMUNITY_FOURSQUARE == community) {
				intentAction = MCONSTANTS.BCAST_USERLOGIN_FSQ;
				intentExtra = MCONSTANTS.INTENT_EXTRA_USERLOGIN_FSQ;
			}
			else if (MCONSTANTS.COMMUNITY_GOWALLA == community) {
				intentAction = MCONSTANTS.BCAST_USERLOGIN_GOWALLA;
				intentExtra = MCONSTANTS.INTENT_EXTRA_USERLOGIN_GOWALLA;
			}
			else if (MCONSTANTS.COMMUNITY_FACEBOOK == community) {
				intentAction = MCONSTANTS.BCAST_USERLOGIN_FACEBOOK;
				intentExtra = MCONSTANTS.INTENT_EXTRA_USERLOGIN_FACEBOOK;
			}
			
			if(LOGGING.DEBUG)Log.d(TAG, "sending Broadcast! " 
					+ "|intentAction->"+intentAction
					+ "|intentExtra->"+intentExtra
					+ "|payload->"+payload
					);
			
			Intent mIntent = new Intent();
        	mIntent.setAction(intentAction);
        	mIntent.putExtra(intentExtra, payload);
        	this.sendBroadcast(mIntent);
		}
    	catch (Exception ex) {
    		Log.w(TAG, ex.toString());
    	}
    	
	}
	
	/*public void onClick_wareninjaWebsite(View v) {
		Utils.go2AppWebsite(mContext);
	}*/
}