package com.youbanban.cordova.chooseimages;

import java.util.LinkedList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.youbanban.cordova.chooseimages.imageloader.MainActivity;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;


public class chooseimages extends CordovaPlugin {

	public static Activity activity;
	public static CallbackContext callbackContext;
	public static boolean isOver = false;
	public static int maxSize;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException {
        activity = this.cordova.getActivity();
        callbackContext = callbackContext;
        if(action.equals("setlocation")){

        }else if(action.equals("getCamera")){
        	maxSize = Integer.parseInt(args.getString(0));
           Intent intent = new Intent();
           intent.setClass(activity, MainActivity.class);
           this.cordova.startActivityForResult(this, intent, 1);
           isOver = false;
           while(true){
        	   if(isOver){
        		   isOver = false;
        		   //下面三句为cordova插件回调页面的逻辑代码
                   PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
                   mPlugin.setKeepCallback(true);
                   callbackContext.sendPluginResult(mPlugin);
                   String result = "";
                   for(int i=0;i<MyAdapter.mSelectedImage.size();i++){
                	   result = result+MyAdapter.mSelectedImage.get(i);
                	   if(i < MyAdapter.mSelectedImage.size()-1){
                		   result = result + ",";
                	   }
                   }
       			   MyAdapter.mSelectedImage = new LinkedList<String>();
                   callbackContext.success(result);
               	   return true;
        	   }
           }


        }
        return true;
    }

    //onActivityResult为第二个Activity执行完后的回调接收方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (resultCode) { //resultCode为回传的标记，我在第二个Activity中回传的是RESULT_OK
            case Activity.RESULT_OK:
            	isOver = true;
//                Bundle b=intent.getExtras();  //data为第二个Activity中回传的Intent
//                String str=b.getString("change01");//str即为回传的值
                break;
            default:
               break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if(resultCode==Activity.RESULT_OK){
//            Toast.makeText(activity,"跳转页面的回调",Toast.LENGTH_LONG).show();
//        }
//    }



}
























