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

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wareninja.android.opensource.oauth2login.R;

public class NotifierHelper {
	
	private static final String TAG = "NotifierHelper";
    private static final int NOTIFY_1 = 0x1001;

    public static void clear(Context caller) {
        NotificationManager notifier = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.cancelAll();
    }
    
    // display customized Toast message
    public static int SHORT_TOAST = 0;
    public static int LONG_TOAST = 1;
    public static void displayToast(Context caller, String toastMsg, int toastType){
        
    	try {// try-catch to avoid stupid app crashes
	        LayoutInflater inflater = LayoutInflater.from(caller);
	        
	        View mainLayout = inflater.inflate(R.layout.toast_layout, null);
	        View rootLayout = mainLayout.findViewById(R.id.toast_layout_root);
	       
	        ImageView image = (ImageView) mainLayout.findViewById(R.id.image);
	        image.setImageResource(R.drawable.img_icon_notification);
	        TextView text = (TextView) mainLayout.findViewById(R.id.text);
	        text.setText(toastMsg);
	        
	    	Toast toast = new Toast(caller);
	    	//toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    	toast.setGravity(Gravity.BOTTOM, 0, 0);
	    	if (toastType==SHORT_TOAST)//(isShort)
	    		toast.setDuration(Toast.LENGTH_SHORT);
	    	else
	    		toast.setDuration(Toast.LENGTH_LONG);
	    	toast.setView(rootLayout);
	    	toast.show();
    	}
		catch(Exception ex) {// to avoid stupid app crashes
			Log.w(TAG, ex.toString());
		}
    }
}
