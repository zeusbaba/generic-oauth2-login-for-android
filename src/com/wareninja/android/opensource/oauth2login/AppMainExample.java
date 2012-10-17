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
import com.wareninja.android.opensource.twilio_connect.TwilioAuthDialog;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;

public class AppMainExample extends Activity {
    
	protected static final String TAG = "AppMainExample";
	
	public Context mContext;
	public Activity mActivity;
	public WebService webService;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        mActivity = this;
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
					
					broadcastLoginResult(AppContext.COMMUNITY.FOURSQUARE, tokenResponse);
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
					
					broadcastLoginResult(AppContext.COMMUNITY.GOWALLA, tokenResponse); 
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

    public void onClick_twilioConnect(View v) {
		//NotifierHelper.displayToast(mContext, "onClick_twilioConnect", NotifierHelper.SHORT_TOAST);
		
		// https://www.twilio.com/docs/connect
		
    	webService = new WebService();
    	
    	String authRequestRedirect = AppContext.TWILIO_APP_OAUTH_BASEURL
				+AppContext.TWILIO_APP_ID
				+ "/signin";// begin with signin page first
		if(LOGGING.DEBUG)Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);
		
		CookieSyncManager.createInstance(this);
		new TwilioAuthDialog(mContext, authRequestRedirect
				, new GenericDialogListener() {
			public void onComplete(Bundle values) {
				if(LOGGING.DEBUG)Log.d(TAG, "onComplete->"+values);
// http://www.example.com/twilio/authorize?AccountSid=AC12345
//   Your Connect App's Authorize URL       Customer's SID
/*
if user ALLOWs your app -> Bundle[{AccountSid=<user_AccountSid>}]
if user doesNOT ALLOW -> Bundle[{error=blablabla}]
*/
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
				
				String tokenResponse = "";
				try{
					
					tokenResponse = values.toString();
					
					broadcastLoginResult(AppContext.COMMUNITY.TWILIO, tokenResponse);
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
					
					broadcastLoginResult(AppContext.COMMUNITY.FACEBOOK, tokenResponse);
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
	
	// Facebook SSO impl
	// NOTE: remember to study & digest this guide beforehand!! ->  https://developers.facebook.com/docs/mobile/android/sso/
	public void onClick_facebookConnect(View v) {
		startFacebookSingleSignOn();
	}
	
	
	private void broadcastLoginResult(AppContext.COMMUNITY community, String payload) {
		
		String intentAction = "";
		String intentExtra = "";
		try {
			
			if (AppContext.COMMUNITY.FOURSQUARE == community) {
				intentAction = AppContext.BCAST_USERLOGIN_FSQ;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_FSQ;
			}
			else if (AppContext.COMMUNITY.GOWALLA == community) {
				intentAction = AppContext.BCAST_USERLOGIN_GOWALLA;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_GOWALLA;
			}
			else if (AppContext.COMMUNITY.FACEBOOK == community) {
				intentAction = AppContext.BCAST_USERLOGIN_FACEBOOK;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_FACEBOOK;
			}
			else if (AppContext.COMMUNITY.TWILIO == community) {
				intentAction = AppContext.BCAST_USERLOGIN_TWILIO;
				intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_TWILIO;
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
	
	
	
    // --- Facebook SSO ---
	private boolean singleSignOnStarted = false;
    private Activity mAuthActivity;
    private String[] mAuthPermissions;
    private int mAuthActivityCode;
    public static final String FB_APP_SIGNATURE =
            "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310"
            + "b3009060355040613025553310b30090603550408130243413112301006035504"
            + "07130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204"
            + "d6f62696c653111300f060355040b130846616365626f6f6b311d301b06035504"
            + "03131446616365626f6f6b20436f72706f726174696f6e3020170d30393038333"
            + "13231353231365a180f32303530303932353231353231365a307a310b30090603"
            + "55040613025553310b30090603550408130243413112301006035504071309506"
            + "16c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c"
            + "653111300f060355040b130846616365626f6f6b311d301b06035504031314466"
            + "16365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d01"
            + "0101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fa"
            + "b00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7"
            + "e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581cc"
            + "fef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603"
            + "f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d010104050"
            + "0038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c"
            + "4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a6"
            + "73149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2"
            + "571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd"
            + "928a2";
    /**
     * Internal method to handle single sign-on backend for authorize().
     *
     * @param activity
     *            The Android Activity that will parent the ProxyAuth Activity.
     * @param applicationId
     *            The Facebook application identifier.
     * @param permissions
     *            A list of permissions required for this application. If you do
     *            not require any permissions, pass an empty String array.
     * @param activityCode
     *            Activity code to uniquely identify the result Intent in the
     *            callback.
     */
    public boolean startFacebookSingleSignOn() {
    	int activityCode = AppContext.FACEBOOK_SSO_ACTIVITY_CODE;
    	String applicationId = AppContext.FB_APP_ID;
    	String[] permissions = AppContext.FB_PERMISSIONS;
    	
        boolean didSucceed = true;
        Intent intent = new Intent();

        intent.setClassName("com.facebook.katana",
                "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", applicationId);
        if (permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }
        
        // Verify that the application whose package name is
        // com.facebook.katana.ProxyAuth
        // has the expected FB app signature.
        if (!validateActivityIntent(mActivity, intent)) {
        	if(AppContext.DEBUG)Log.d(TAG, "SSO activity is not valid or doesnt exist!");
            return false;
        }

        
        mAuthActivity = mActivity;
        mAuthPermissions = permissions;
        mAuthActivityCode = activityCode;
        try {
        	mActivity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
            Log.w(TAG, "SSO activity not found! " + e.toString());
        }

        return didSucceed;
    }
    /**
     * Helper to validate an activity intent by resolving and checking the
     * provider's package signature.
     *
     * @param context
     * @param intent
     * @return true if the service intent resolution happens successfully and the
     * 	signatures match.
     */
    private boolean validateActivityIntent(Context context, Intent intent) {
        ResolveInfo resolveInfo =
            context.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }

        return validateAppSignatureForPackage(
            context,
            resolveInfo.activityInfo.packageName);
    }


    /**
     * Helper to validate a service intent by resolving and checking the
     * provider's package signature.
     *
     * @param context
     * @param intent
     * @return true if the service intent resolution happens successfully and the
     * 	signatures match.
     */
    private boolean validateServiceIntent(Context context, Intent intent) {
        ResolveInfo resolveInfo =
            context.getPackageManager().resolveService(intent, 0);
        if (resolveInfo == null) {
            return false;
        }

        return validateAppSignatureForPackage(
            context,
            resolveInfo.serviceInfo.packageName);
    }

    /**
     * Query the signature for the application that would be invoked by the
     * given intent and verify that it matches the FB application's signature.
     *
     * @param context
     * @param packageName
     * @return true if the app's signature matches the expected signature.
     */
    private boolean validateAppSignatureForPackage(Context context,
        String packageName) {

        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            return false;
        }

        for (Signature signature : packageInfo.signatures) {
            if (signature.toCharsString().equals(FB_APP_SIGNATURE)) {
                return true;
            }
        }
        return false;
    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String SINGLE_SIGN_ON_DISABLED = "service_disabled";
		boolean isSsoSuccess = false;
		//Bundle extras = null;
		if (requestCode == AppContext.FACEBOOK_SSO_ACTIVITY_CODE) {
			//extras = (data!=null)? data.getExtras() : new Bundle();
			//if (AppContext.DEBUG)Log.d(TAG, "FACEBOOK_SSO_ACTIVITY extras->" + extras);
			
			// Successfully redirected.
            if (resultCode == Activity.RESULT_OK) {

	                // Check OAuth 2.0/2.10 error code.
	                String error = data.getStringExtra("error");
	                if (error == null) {
	                    error = data.getStringExtra("error_type");
	                }
	                
	                // A Facebook error occurred.
	                if (error != null) {
	                    if (error.equals(SINGLE_SIGN_ON_DISABLED)
	                            || error.equals("AndroidAuthKillSwitchException")) {
	                    	if(AppContext.DEBUG)Log.d(TAG, "Facebook-authorize: Hosted auth currently disabled. Retrying dialog auth...");
	                        
	                        
	                    } else if (error.equals("access_denied")
	                            || error.equals("OAuthAccessDeniedException")) {
	                    	if(AppContext.DEBUG)Log.d(TAG, "Facebook-authorize: Login canceled by user.");
	                        //mAuthDialogListener.onCancel();
	                    } else {
	                        String description = data.getStringExtra("error_description");
	                        if (description != null) {
	                            error = error + ":" + description;
	                        }
	                        if(AppContext.DEBUG)Log.d(TAG, "Facebook-authorize: Login failed - " + error);
	                    }
	                // No errors.
	                } else {
	                	isSsoSuccess = true;
	                }
	            // An error occurred before we could be redirected.
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	
	                // An Android error occured.
	                if (data != null) {
	                	
	                	if(AppContext.DEBUG)Log.d(TAG, "Facebook-authorize: Login failed - " 
	                			+ data.getStringExtra("error")
	                			+ " |Ê" + data.getStringExtra("failing_url")
	                			);
	                // User pressed the 'back' button.
	                } else {
	                	if(AppContext.DEBUG)Log.d(TAG, "Facebook-authorize: Login canceled by user.");
	                	//singleSignOnStarted=false;// to force sso again
	                }
	            }
            
            if (!isSsoSuccess) {
            	
            	singleSignOnStarted=false;// to force sso again
            }
            else {// SSO success!
            	
            	Bundle respValues = new Bundle();
            	respValues.putString("access_token", data.getStringExtra("access_token"));
            	respValues.putString("expires_in", data.getStringExtra("expires_in"));
              
            	String tokenResponse = "";
				try{
					
					tokenResponse = respValues.toString();
					
					//broadcastLoginResult(AppContext.COMMUNITY_FACEBOOK, tokenResponse);
					broadcastLoginResult(AppContext.COMMUNITY.FACEBOOK, tokenResponse);

				}
				catch (Exception ex1){
					Log.w(TAG, ex1.toString());
					tokenResponse = null;
				}
            }
	    }
    }
    // ---
	
	
}