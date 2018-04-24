package com.youbanban.cordova.chooseimages.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.youbanban.cordova.chooseimages.utils.ImageLoader.Type;

public class ViewHolder{
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private ViewHolder(Context context, ViewGroup parent, int layoutId,int position){
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}

	// 拿到一个ViewHolder对象
	public static ViewHolder get(Context context, View convertView,ViewGroup parent, int layoutId, int position){
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder(context, parent, layoutId, position);
		} else{
			holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
		}
		return holder;
	}

	public View getConvertView(){
		return mConvertView;
	}

	//通过控件的Id获取对于的控件，如果没有则加入views
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	//为TextView设置字符串
	public ViewHolder setText(int viewId, String text){
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	public ViewHolder setImageResource(int viewId, int drawableId){
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	public ViewHolder setImageBitmap(int viewId, Bitmap bm){
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	public ViewHolder setImageByUrl(int viewId, String url){
		ImageLoader.getInstance(3,Type.LIFO).loadImage(url, (ImageView) getView(viewId));
		return this;
	}

	public int getPosition(){
		return mPosition;
	}
}
