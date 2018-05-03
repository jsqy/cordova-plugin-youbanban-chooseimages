package com.youbanban.cordova.chooseimages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListView;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListViewAdapter;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class YuLanActivity extends Activity {
	private RelativeLayout rl_back;
	private RelativeLayout rl_delete;
	private TextView tv_ok;
	private int indexNum = 0;
	HorizontalListView hListView;
	HorizontalListViewAdapter hListViewAdapter;
	private int isOver = 1;
	private int isSelectNum = 0;
	ViewPager mViewPager;
	List<ImageView> imageViews;
	private TextView tv_xuanze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseimage_yulan);
		initView();
		initBitmap();
		initOnClick();
		initTest();
		initBtnOK();
		initViewPager();
	}

	private void initViewPager(){
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new ImageAdapter(this));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				indexNum = arg0;
				initIsDeleteImage();
				hListViewAdapter.setSelectIndex(indexNum);
				hListViewAdapter.notifyDataSetChanged();
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private void initBitmap() {
		for (int i = 0; i < MyAdapter.list.size(); i++) {
			MyAdapter.list.get(i).setIsDelete(0);
		}
	}

	private void initTest() {
		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		hListViewAdapter = new HorizontalListViewAdapter(
			getApplicationContext());
		hListViewAdapter.setSelectIndex(0);
		hListView.setAdapter(hListViewAdapter);
		hListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				mViewPager.setCurrentItem(position);
				indexNum = position;
				initIsDeleteImage();
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();

			}
		});
	}

	private void initBtnOK(){
		isSelectNum = 0;
		for(int i =0;i<MyAdapter.list.size();i++){
			if(MyAdapter.list.get(i).getIsDelete() != 1){
				isSelectNum = isSelectNum+1;
			}
		}
		tv_ok.setText("完成( "+isSelectNum+"/"+chooseimages.maxSize+" )");
	}

	private void initView() {
		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_xuanze= (TextView) findViewById(R.id.tv_xuanze);
	}

	private void initIsDeleteImage(){
		if(MyAdapter.list.get(indexNum).getIsDelete() == 1){
			tv_xuanze.setText("恢复");
		}else{
			tv_xuanze.setText("删除");
		}
		initBtnOK();
	}

	private void initOnClick() {
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for(int i = 0;i<MyAdapter.list.size();i++){
					if(MyAdapter.list.get(i).getIsDelete() == 1){
						for(int j =0;j<MyAdapter.mSelectedImage.size();j++){
							if(MyAdapter.list.get(i).getPath().equals(MyAdapter.mSelectedImage.get(j))){
								MyAdapter.mSelectedImage.remove(j);
							}
						}
					}
				}
				MyAdapter.initList();
				Intent mIntent = new Intent();
				mIntent.putExtra("isOk", "1");
				// 设置结果，并进行传送
				YuLanActivity.this.setResult(2, mIntent);
				YuLanActivity.this.finish();
			}
		});

		rl_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isOver == 1) {
					for(int i = 0;i<MyAdapter.list.size();i++){
						if(MyAdapter.list.get(i).getNum() == (indexNum+1)){
							if(MyAdapter.list.get(i).getIsDelete() == 0){
								MyAdapter.list.get(i).setIsDelete(1);
							}else{
								MyAdapter.list.get(i).setIsDelete(0);
							}
						}
					}
					initIsDeleteImage();
					hListViewAdapter.notifyDataSetChanged();
				}
			}
		});

		rl_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i = 0;i<MyAdapter.list.size();i++){
					if(MyAdapter.list.get(i).getIsDelete() == 1){
						for(int j =0;j<MyAdapter.mSelectedImage.size();j++){
							if(MyAdapter.list.get(i).getPath().equals(MyAdapter.mSelectedImage.get(j))){
								MyAdapter.mSelectedImage.remove(j);
							}
						}
					}
				}
				MyAdapter.initList();
				Intent mIntent = new Intent();
				mIntent.putExtra("isOk", "0");
				// 设置结果，并进行传送
				YuLanActivity.this.setResult(2, mIntent);
				YuLanActivity.this.finish();
			}
		});
	}

	/**
	 * 监听Back键按下事件,方法1:
	 * 注意:
	 * super.onBackPressed()会自动调用finish()方法,关闭
	 * 当前Activity.
	 * 若要屏蔽Back键盘,注释该行代码即可
	 */
	@Override
	public void onBackPressed() {
		for(int i = 0;i<MyAdapter.list.size();i++){
			if(MyAdapter.list.get(i).getIsDelete() == 1){
				for(int j =0;j<MyAdapter.mSelectedImage.size();j++){
					if(MyAdapter.list.get(i).getPath().equals(MyAdapter.mSelectedImage.get(j))){
						MyAdapter.mSelectedImage.remove(j);
					}
				}
			}
		}
		MyAdapter.initList();
		Intent mIntent = new Intent();
		mIntent.putExtra("isOk", "0");
		// 设置结果，并进行传送
		YuLanActivity.this.setResult(2, mIntent);
		YuLanActivity.this.finish();
	}

	class ImageAdapter extends PagerAdapter {
		Context context;

		public void init() {
			imageViews = new ArrayList<ImageView>();
			for (int i = 0; i < MyAdapter.list.size(); i++) {
				ImageView image = new ImageView(context);
				image.setScaleType(ScaleType.FIT_XY);
				//使图片实现可以放大缩小的功能
				Glide.with(context).load(MyAdapter.list.get(i).getPath()).centerCrop()
					.placeholder(Color.WHITE).crossFade()
					.into(image);
				imageViews.add(image);
			}
		}
		public ImageAdapter(Context context) {
			this.context = context;
			init();
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}
		@Override
		public int getCount() {
			return imageViews.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
