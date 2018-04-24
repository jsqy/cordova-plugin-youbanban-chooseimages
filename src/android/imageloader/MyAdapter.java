package com.youbanban.cordova.chooseimages.imageloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.YuLanAllActivity;
import com.youbanban.cordova.chooseimages.bean.Images;
import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.utils.CommonAdapter;
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
		helper.setImageResource(R.id.id_item_select,R.drawable.picture_unselected);
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
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		mImageView.setScaleType(ScaleType.CENTER_CROP);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		final TextView tv_item_select = helper.getView(R.id.tv_item_select);
		final ImageView mSelectAR = helper.getView(R.id.id_item_ar);
		final RelativeLayout rl_check_button = helper.getView(R.id.rl_check_button);
		if(item.indexOf(".ybb.ar") != -1){
			mSelectAR.setImageResource(R.drawable.ar_title);
		}
		mImageView.setColorFilter(null);
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int result = -1;
				for(int i =  0;i<mDatas.size();i++){
					if(mDatas.get(i).equals(item)){
						result = i;
					}
				}
				Intent intent = new Intent();
				intent.putExtra("index", result+"");
				intent.setClass(mActivity,YuLanAllActivity.class);
				mActivity.startActivityForResult(intent, 3);
			}
		});
		rl_check_button.setOnClickListener(new OnClickListener(){
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v){
				if (mSelectedImage.contains(mDirPath + "/" + item)){
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
					initList();
					notifyDataSetChanged();
				} else{
					if(MyAdapter.mSelectedImage.size() < chooseimages.maxSize ){
						mSelectedImage.add(mDirPath + "/" + item);
						initList();
						notifyDataSetChanged();
					}
				}
				MainActivity.tv_ok.setText("确认 "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
				MainActivity.mImageCount.setText("预览"+"("+mSelectedImage.size() + ")");
			}
		});

		if (mSelectedImage.contains(mDirPath + "/" + item)){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getPath().equals((mDirPath + "/" + item))){
					mSelect.setImageResource(R.drawable.yellow_background);
					tv_item_select.setText(list.get(i).getNum()+"");
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
			}
		}else{
			mSelect.setImageResource(R.drawable.picture_unselected);
			tv_item_select.setText("");
			mImageView.setColorFilter(null);
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
