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
import android.widget.Toast;

public class chooseimages extends CordovaPlugin {
	public static Activity activity;
	public static CallbackContext callbackContext;
	public static int maxSize;
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
		throws JSONException {
		activity = this.cordova.getActivity();
		this.callbackContext = callbackContext;
		maxSize = args.getInt(0);
		if(action.equals("getPhotos")){
			if (initPermission()) {
				Intent intent = new Intent();
				intent.setClass(activity, MainActivity.class);
				this.cordova.startActivityForResult(this, intent, 1);
			}else{
				getReadPermission(0);
			}
		}
		return true;
	}

	// 权限回调
	public void onRequestPermissionResult(int requestCode,
										  String[] permissions, int[] grantResults) throws JSONException {
		if (cordova.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
			Intent intent = new Intent();
			intent.setClass(activity, MainActivity.class);
			this.cordova.startActivityForResult(this, intent, 1);
		} else {
			callbackContext.success("没有权限");
			Toast.makeText(cordova.getActivity(),"没有文件读取权限",Toast.LENGTH_SHORT);
		}
	}


	protected void getReadPermission(int requestCode) {
		cordova.requestPermission(this, requestCode, Manifest.permission.READ_EXTERNAL_STORAGE);
	}


	private Boolean initPermission(){
		if(cordova.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
			return true;
		}else{
			return false;
		}
	}

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
