package com.youbanban.cordova.chooseimages;

import java.util.LinkedList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.youbanban.cordova.chooseimages.imageloader.MainActivity;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


public class chooseimages extends CordovaPlugin {

	public static Activity activity;
	public static CallbackContext callbackContext;
	public static boolean isOver = false;
	public static int maxSize;
	
	private static final int REQUEST_EXTERNAL_STORAGE = 1;  
	private static String[] PERMISSIONS_STORAGE = {  
	        Manifest.permission.READ_EXTERNAL_STORAGE,  
	        Manifest.permission.WRITE_EXTERNAL_STORAGE  
	};  

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException {
        activity = this.cordova.getActivity();
        this.callbackContext = callbackContext;
        if(action.equals("setlocation")){

        }else if(action.equals("getPhotos")){
        //0	 Check if we have write permission  
        	int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);  
        	if (permission != PackageManager.PERMISSION_GRANTED) {  
        	   // We don't have permission so prompt the user  
        	   ActivityCompat.requestPermissions(  
        	       activity,  
        	       PERMISSIONS_STORAGE,  
        	       REQUEST_EXTERNAL_STORAGE  
        	     );  
           	   return false;
        	 }else{
        	    	maxSize = args.getInt(0);
        	        Intent intent = new Intent();
        	        intent.setClass(activity, MainActivity.class);
        	        this.cordova.startActivityForResult(this, intent, 1);
        	 }

        }
        return true;
    }

    //onActivityResult为第二个Activity执行完后的回调接收方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (resultCode) { //resultCode为回传的标记，我在第二个Activity中回传的是RESULT_OK
            case Activity.RESULT_OK:
            	JSONArray result = new JSONArray();
 	            for(int i=0;i<MyAdapter.mSelectedImage.size();i++){
 	               result.put(MyAdapter.mSelectedImage.get(i));
 	             }
 	            MyAdapter.mSelectedImage = new LinkedList<String>();
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,result)); 
                break;
            default:
               break;
        }
    }



}
























