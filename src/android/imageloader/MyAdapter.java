package com.youbanban.cordova.chooseimages.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.bean.Images;
import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.utils.CommonAdapter;
import com.youbanban.cordova.chooseimages.utils.RoundImageView;
import com.youbanban.cordova.chooseimages.utils.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyAdapter extends CommonAdapter<String>{
	public Context mContext;
	public Activity mActivity;
	public static List<String> mSelectedImage = new LinkedList<String>();
	public static List<Images> list = new ArrayList<Images>();
	private String mDirPath;
	List<String> mDatas;
	public MyAdapter(Activity activity, Context context, List<String> mDatas, int itemLayoutId,String dirPath){
		super(context, mDatas, itemLayoutId);
		this.mContext = context;
		mActivity = activity;
		this.mDirPath = dirPath;
		this.mDatas = mDatas;
	}

	@Override
	public void convert(final com.youbanban.cordova.chooseimages.utils.ViewHolder helper, final String item){
		helper.setImageResource(R.id.id_item_select,R.drawable.pictures_selected);
		int o=Util.readPictureDegree(mDirPath + "/" + item);
		if(o==0){
			helper.setImageByUrl(R.id.id_item_image,mDirPath + "/" + item);
		}else {
			try {
				Bitmap bitmap = Util.compressPixel(mDirPath + "/" + item);
				Bitmap bm=Util.rotateBitmap(bitmap,o);
				helper.setImageBitmap(R.id.id_item_image, bm);
			} catch (Exception e) {
			}
		}
		final RoundImageView mImageView = helper.getView(R.id.id_item_image);
		mImageView.setScaleType(ScaleType.CENTER_CROP);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		mImageView.setColorFilter(null);
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mSelectedImage.contains(mDirPath + "/" + item)){
					mSelectedImage.remove(mDirPath + "/" + item);
					initList();
					notifyDataSetChanged();
				}else {
					if(MyAdapter.mSelectedImage.size() < chooseimages.maxSize ) {
						mSelectedImage.add(mDirPath + "/" + item);
						initList();
						notifyDataSetChanged();
					}else if(MyAdapter.mSelectedImage.size() >= chooseimages.maxSize){
						notifyDataSetChanged();
						Toast.makeText(mContext,"亲，最多只能添加"+chooseimages.maxSize+"张照片哦~",Toast.LENGTH_SHORT).show();
					}
				}
				MainActivity.tv_ok.setText("确认( "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize+" )");
			}
		});

		 if(MyAdapter.mSelectedImage.size() == chooseimages.maxSize) {
			 if(!mSelectedImage.contains(mDirPath + "/" + item)) {
				 mImageView.setColorFilter(Color.parseColor("#77000000"));
			 }
		 }
			if (mSelectedImage.contains(mDirPath + "/" + item)){
			mSelect.setVisibility(View.VISIBLE);
		}else{
			mSelect.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public static void initList(){
		list = new ArrayList<Images>();
		for(int i = 0;i<mSelectedImage.size();i++){
			Images image = new Images(mSelectedImage.get(i), i+1);
			list.add(image);
		}
	}
}
