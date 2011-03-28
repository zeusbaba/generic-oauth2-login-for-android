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

package com.wareninja.android.opensource.oauth2login.common;

import android.os.Bundle;


public abstract class GenericDialogListener {

    
    /**
     * Called when a dialog completes.
     * Executed by the thread that initiated the dialog.
     * @param values
     *            Key-value string pairs extracted from the response.
     */
    public void onComplete(Bundle values) {
    }

    /**
     * Called when a dialog has an error.
     * Executed by the thread that initiated the dialog.
     */
    public void onError(String e) {
    }

    /**
     * Called when a dialog is canceled by the user.
     * Executed by the thread that initiated the dialog.
     */
    public void onCancel() {
    }
    
}
