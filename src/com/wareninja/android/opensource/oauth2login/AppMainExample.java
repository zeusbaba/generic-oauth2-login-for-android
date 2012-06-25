/***
	Copyright (c) 2011-2012 WareNinja.com 
	http://www.WareNinja.com - https://github.com/WareNinja
	
	Author: yg@wareninja.com / twitter: @WareNinja

  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
  >> Summary of the license:
  	You are allowed to re-use this code as you like, no kittens should be harmed though! 
 */


package com.wareninja.android.opensource.oauth2login;

import java.util.HashMap;
import java.util.Map;

import com.wareninja.android.opensource.oauth2login.common.GenericDialogListener;
import com.wareninja.android.opensource.oauth2login.common.LOGGING;
import com.wareninja.android.opensource.oauth2login.common.AppContext;
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
    	
    	String authRequestRedirect = AppContext.FSQ_APP_OAUTH_BASEURL+AppContext.FSQ_APP_OAUTH_URL
		        + "?client_id="+AppContext.FSQ_APP_KEY
		        + "&response_type=code" 
		        + "&display=touch"
		        + "&redirect_uri="+AppContext.FSQ_APP_CALLBACK_OAUTHCALLBACK;
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
					webService.setWebServiceUrl(AppContext.FSQ_APP_OAUTH_BASEURL);
					// Call Foursquare again to get the access token
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("client_id", AppContext.FSQ_APP_KEY);
					params.put("client_secret", AppContext.FSQ_APP_SECRET);
					params.put("grant_type", "authorization_code");
					params.put("redirect_uri", AppContext.FSQ_APP_CALLBACK_OAUTHCALLBACK);
					params.put("code", values.getString("code"));
					if(LOGGING.DEBUG)Log.d(TAG, "params->" + params);
					
					tokenResponse = webService.webGet(AppContext.FSQ_APP_TOKEN_URL, params);
					if(LOGGING.DEBUG)Log.d(TAG, "tokenResponse->" + tokenResponse);
					
					broadcastLoginResult(AppContext.COMMUNITY_FOURSQUARE, tokenResponse);
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
        + "?client_id="+AppContext.GOWALLA_APP_KEY
        + "&scope=read-write"
        + "&display=touch"
        + "&redirect_uri="+AppContext.GOWALLA_APP_CALLBACK_OAUTHCALLBACK;
    	
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
					webService.setWebServiceUrl(AppContext.GOWALLA_APP_OAUTH_BASEURL);
					// Call Foursquare again to get the access token
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("client_id", AppContext.GOWALLA_APP_KEY);
					params.put("client_secret", AppContext.GOWALLA_APP_SECRET);
					params.put("grant_type", "authorization_code");
					params.put("redirect_uri", AppContext.GOWALLA_APP_CALLBACK_OAUTHCALLBACK);
					params.put("code", values.getString("code"));
					if(LOGGING.DEBUG)Log.d(TAG, "params->" + params);
					
					tokenResponse = webService.webInvokeWithJson(AppContext.GOWALLA_APP_TOKEN_URL
							, webService.getJsonFromParams(params)
							);
					if(LOGGING.DEBUG)Log.d(TAG, "tokenResponse->" + tokenResponse);
					
					broadcastLoginResult(AppContext.COMMUNITY_GOWALLA, tokenResponse); 
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
		
		// https://developers.facebook.com/docs/reference/dialogs/oauth/
		
    	webService = new WebService();
    	
    	String authRequestRedirect = AppContext.FB_APP_OAUTH_BASEURL+AppContext.FB_APP_OAUTH_URL
		        + "?client_id="+AppContext.FB_APP_ID
		        + "&response_type=token" 
		        + "&display=touch"
		        + "&scope=" + TextUtils.join(",", AppContext.FB_PERMISSIONS)
		        + "&redirect_uri="+AppContext.FB_APP_CALLBACK_OAUTHCALLBACK
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
					
					broadcastLoginResult(AppContext.COMMUNITY_FACEBOOK, tokenResponse);
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
			
			if (AppContext.COMMUNITY_FOURSQUARE == community) {
				intentAction = AppContext.BCAST_USERLOGIN_FSQ;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_FSQ;
			}
			else if (AppContext.COMMUNITY_GOWALLA == community) {
				intentAction = AppContext.BCAST_USERLOGIN_GOWALLA;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_GOWALLA;
			}
			else if (AppContext.COMMUNITY_FACEBOOK == community) {
				intentAction = AppContext.BCAST_USERLOGIN_FACEBOOK;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_FACEBOOK;
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