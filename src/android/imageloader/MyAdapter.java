package com.youbanban.cordova.chooseimages.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.cordova.LOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youbanban.cordova.chooseimages.YuLanAllActivity;
import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.bean.Images;
import com.youbanban.cordova.chooseimages.utils.CommonAdapter;
import com.youbanban.cordova.chooseimages.utils.Util;
import com.youbanban.app.R;

public class MyAdapter extends CommonAdapter<String>
{

	public Context mContext;
	public Activity mActivity;
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();
	public static List<Bitmap> lsitBitmap = new ArrayList<Bitmap>();
	public static List<Images> list = new ArrayList<Images>();
	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	List<String> mDatas;
	public MyAdapter(Activity activity, Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		this.mContext = context;
		mActivity = activity;
		this.mDirPath = dirPath;
		this.mDatas = mDatas;
	}

	@Override
	public void convert(final com.youbanban.cordova.chooseimages.utils.ViewHolder helper, final String item)
	{

		//设置no_pic
//		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		mImageView.setScaleType(ScaleType.CENTER_CROP);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		final TextView tv_item_select = helper.getView(R.id.tv_item_select);
		final ImageView mSelectAR = helper.getView(R.id.id_item_ar);
		final RelativeLayout rl_check_button = helper.getView(R.id.rl_check_button);
		if(item.indexOf("youbanban.ar") != -1){
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
		//设置ImageView的点击事件
		rl_check_button.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{

				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
					
//					for(int i =0;i<list.size();i++){
//						if(list.get(i).getPath().equals((mDirPath + "/" + item))){
//							int num = list.get(i).getNum();
//							list.remove(i);
//							for(int j =0;j<list.size();j++){
//								if(list.get(j).getNum()>num){
//									list.get(j).setNum(list.get(j).getNum()-1);
//								}
//							}
//
//						}
//					}
					initList();
					notifyDataSetChanged();
//					MainActivity.mImageCount.setText(MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
				} else
				// 未选择该图片
				{
					if(MyAdapter.mSelectedImage.size() < chooseimages.maxSize ){
						mSelectedImage.add(mDirPath + "/" + item);
						initList();
						notifyDataSetChanged();
//						Images image = new Images((mDirPath + "/" + item).toString(), list.size()+1);
//						list.add(image);
					}
				}

				MainActivity.tv_ok.setText("确认 "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
				MainActivity.mImageCount.setText("预览"+"("+mSelectedImage.size() + ")");


			}
		});



		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
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
		// TODO Auto-generated method stub
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
