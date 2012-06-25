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


package com.wareninja.android.opensource.oauth2login.common;


public class AppContext {

	public static final boolean DEBUG = LOGGING.DEBUG;// enable/disable logging

	public static final String WARENINJAAPPS_MARKET_URL = "market://search?q=wareninja";
	public static final String AMAZON_WARENINJAAPPS_MARKET_URL = 
		//"http://www.amazon.com/s/ref=bl_sr_mobile-apps?_encoding=UTF8&node=2350149011&field-brandtextbin=WareNinja";
			"http://www.amazon.com/gp/mas/dl/android?p=com.wareninja.android.loco&showAll=1";
	
	public static final String APPWEBSITE_URL = "http://www.WareNinja.net";
	
	public static final String GRAPH_BASE_URL = "http://graph.facebook.com/";
	public static final String GRAPH_BASE_URL_SSL = "https://graph.facebook.com/";
	
	public static final int ACTIONID_LOGIN_FACEBOOK = 0;
	public static final String ACTIONNAME_LOGIN_FACEBOOK = "LOGIN_FACEBOOK";
	public static final int ACTIONID_LOGIN_GOWALLA = 1;
	public static final String ACTIONNAME_LOGIN_GOWALLA = "LOGIN_GOWALLA";
	public static final int ACTIONID_LOGIN_FOURSQUARE = 2;
	public static final String ACTIONNAME_LOGIN_FOURSQUARE = "LOGIN_FOURSQUARE";
	
	// REGISTER to these bcast identifiers and you will get login response together with its payload as json (INTENT_EXTRA_...)!!!
	public static final String INTENT_EXTRA_USERLOGIN_FSQ = "USERLOGIN_FSQ";
    public static final String BCAST_USERLOGIN_FSQ = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_FSQ";
    public static final String INTENT_EXTRA_USERLOGIN_FACEBOOK = "USERLOGIN_FACEBOOK";
    public static final String BCAST_USERLOGIN_FACEBOOK = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_FACEBOOK";
    public static final String INTENT_EXTRA_USERLOGIN_GOWALLA = "USERLOGIN_GOWALLA";
    public static final String BCAST_USERLOGIN_GOWALLA = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_GOWALLA";
    
    public static final String INTENT_EXTRA_USERLOGOUT_FSQ = "USERLOGOUT_FSQ";
    public static final String BCAST_USERLOGOUT_FSQ = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGOUT_FSQ";
    public static final String INTENT_EXTRA_USERLOGOUT_FACEBOOK = "USERLOGOUT_FACEBOOK";
    public static final String BCAST_USERLOGOUT_FACEBOOK = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGOUT_FACEBOOK";
    public static final String INTENT_EXTRA_USERLOGOUT_GOWALLA = "USERLOGOUT_GOWALLA";
    public static final String BCAST_USERLOGOUT_GOWALLA = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGOUT_GOWALLA";
	
	// --- DIALOG CONSTANTS ---
	// PDIALOG -> ProgressDialog
	public static final int LOGIN_PDIALOG_FACEBOOK = 100;
	public static final int LOGIN_PDIALOG_FSQ = 101;
	public static final int LOGIN_PDIALOG_GOWALLA = 102;
	public static final int LOGOUT_PDIALOG_FACEBOOK = 200;
	public static final int LOGOUT_PDIALOG_FSQ = 201;
	public static final int LOGOUT_PDIALOG_GOWALLA = 202;
	
	
	//public static final String APP_CACHEDIR = ".WareNinja_OpenSource_appCache";
	
	// -->> REPLACE THESE VALUES WITH YOUR OWN APP!!!! <<--
	// NOTE: below values are using Test App for WareNinja.net ONLY!
	
	// Gowalla App params
	public static final String GOWALLA_APP_KEY = "<YOURAPP_atGOWALLA_API_KEY>";  
	public static final String GOWALLA_APP_SECRET = "<YOURAPP_atGOWALLA_API_SECRET>";
	public static final String GOWALLA_APP_CALLBACK_OAUTHCALLBACK = "http://WareNinja_OpenSource";// YOURAPP_REDIRECT_URI
	public static final String GOWALLA_APP_REDIRECT_SIGNIN = "https://gowalla.com/signin";
	public static final String GOWALLA_APP_OAUTH_BASEURL = "https://api.gowalla.com";
	public static final String GOWALLA_APP_TOKEN_URL = "/api/oauth/token";

	// Foursquare App params
	public static final String FSQ_APP_KEY = "<YOURAPP_atFOURSQUARE_CLIENT_ID>";  
	public static final String FSQ_APP_SECRET = "<YOURAPP_atFOURSQUARE_CLIENT_SECRET>";
	public static final String FSQ_APP_CALLBACK_OAUTHCALLBACK = "http://WareNinja_OpenSource";// YOURAPP_REDIRECT_URI
	public static final String FSQ_APP_REDIRECT_SIGNIN = "https://m.foursquare.com/mobile/login";
	public static final String FSQ_APP_OAUTH_BASEURL = "https://foursquare.com";
	public static final String FSQ_APP_OAUTH_URL = "/oauth2/authenticate";
	public static final String FSQ_APP_TOKEN_URL = "/oauth2/access_token";
	
	// Facebook App Params
	public static final String FB_APP_ID = "<YOURAPP_atFACEBOOK_APP_ID>";  
	public static final String[] FB_PERMISSIONS = new String[] {
		// NOTE: remember to extend these permissions as per your need!!!!
		"publish_stream", "read_stream"
		}; 
	public static final String FB_APP_CALLBACK_OAUTHCALLBACK = "fbconnect://success";// YOURAPP_REDIRECT_URI
	public static final String FB_APP_REDIRECT_SIGNIN = "https://m.facebook.com";
	public static final String FB_APP_OAUTH_BASEURL = "https://m.facebook.com";
	public static final String FB_APP_OAUTH_URL = "/dialog/oauth/";
	
	public static final String FACEBOOK_USER_ME = "_FACEBOOK_USER_ME";
	public static final String FOURSQUARE_USER_ME = "_FOURSQUARE_USER_ME";
	public static final String GOWALLA_USER_ME = "_GOWALLA_USER_ME";
	
	public static final String[] COMMUNITY = {
        "FOURSQUARE", "FACEBOOK", "GOWALLA"
        };
	public static final int COMMUNITY_FOURSQUARE = 0;
	public static final int COMMUNITY_FACEBOOK = 1;
	public static final int COMMUNITY_GOWALLA = 2;

	

}
