package com.youbanban.cordova.chooseimages;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.bumptech.glide.Glide;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.YuLanActivity.ImageAdapter;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListView;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListViewAdapterAll;
import com.youbanban.cordova.chooseimages.imageloader.MainActivity;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class YuLanAllActivity extends Activity implements OnClickListener {

	private RelativeLayout rl_back;
	private RelativeLayout rl_delete;
	private ImageView img_xuanze;
	private Button btn_ok;

	HorizontalListView hListView;
	HorizontalListViewAdapterAll hListViewAdapter;
	ImageView previewImg;
	View olderSelectView = null;

	public static int indexNum = 0;
	private int isSelectNum = 0;
	ViewPager mViewPager;
	List<ImageView> imageViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		indexNum = Integer.parseInt(intent.getStringExtra("index"));
		// showMsg("第二页传过来的第几个是-->"+indexBig);
		setContentView(R.layout.chooseimage_yulan_all);
		initView();
		initViewPager();
		initTest();
		
		initIsNotCheck();
		initCheck();
		initBtnOK();
	}

	private void initView() {
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		img_xuanze = (ImageView) findViewById(R.id.img_xuanze);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		
		rl_back.setOnClickListener(this);
		rl_delete.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	private void initCheck() {
		int result = 0;
		for (int i = 0; i < MyAdapter.list.size(); i++) {
			if(MyAdapter.list.get(i).getPath().equals(MainActivity.mImgDir.getAbsoluteFile() + "/"
								+ MainActivity.mImgs.get(indexNum))){
				MyAdapter.list.get(i).setIsCheck(1);
				initIsCheck();
				result = 1;
			}
		}
		if(result == 0){
			initIsNotCheck();
		}

	}

	private void initIsCheck() {
		img_xuanze.setImageDrawable(getResources().getDrawable(
				R.drawable.pictures_selected));
	}

	public void initIsNotCheck() {
		img_xuanze.setImageDrawable(getResources().getDrawable(
				R.drawable.picture_unselected));
	}
	
	private void initBtnOK(){
		isSelectNum = 0;
		for(int i =0;i<MyAdapter.list.size();i++){
			if(MyAdapter.list.get(i).getIsDelete() != 1){
				isSelectNum = isSelectNum+1;
			}
		}
		btn_ok.setText("完成"+isSelectNum+"/"+chooseimages.maxSize);
	}

	private void initTest() {

		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		if (MyAdapter.list.size() > 0) {
			hListView.setVisibility(View.VISIBLE);
		} else {
			hListView.setVisibility(View.INVISIBLE);
		}
		String[] titles = { "01", "02", "03", "04", "5", "6" };
		final int[] ids = { R.drawable.ar_title, R.drawable.ar_title,
				R.drawable.ar_title, R.drawable.ar_title, R.drawable.ar_title,
				R.drawable.ar_title };
		hListViewAdapter = new HorizontalListViewAdapterAll(
				getApplicationContext(), titles, ids);
		hListViewAdapter.setSelectIndex(0);
		hListView.setAdapter(hListViewAdapter);
		mViewPager.setCurrentItem(indexNum);
		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
		
				for(int i = 0;i<MainActivity.mImgs.size();i++){
					if(MyAdapter.list.get(position).getPath().equals(MainActivity.mImgDir.getAbsoluteFile() + "/"
							+ MainActivity.mImgs.get(i))){
						indexNum = i;
					}
				}
				mViewPager.setCurrentItem(indexNum);
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();
				initBtnOK();
			}
		});
	}

	private void initViewPager() {
		// if(){
		//
		// }
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new ImageAdapter(this));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// // TODO Auto-generated method stub
				indexNum = arg0;
				initCheck();
				// initIsDeleteImage();
				// hListViewAdapter.setSelectIndex(indexNum);
				hListViewAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_ok:
			MyAdapter.initList();
			Intent mIntentOk = new Intent();
			mIntentOk.putExtra("isOk", "1");
	        // 设置结果，并进行传送
	        YuLanAllActivity.this.setResult(3, mIntentOk);
	        YuLanAllActivity.this.finish();
			break;
		case R.id.rl_back:
			MyAdapter.initList();
			Intent mIntent = new Intent();
	        mIntent.putExtra("isOk", "0");
	        // 设置结果，并进行传送
	        YuLanAllActivity.this.setResult(3, mIntent);
	        YuLanAllActivity.this.finish();
			break;
		case R.id.rl_delete:
			int result = 0;
			for (int i = 0; i < MyAdapter.list.size(); i++) {
				if(MyAdapter.list.get(i).getPath().equals(MainActivity.mImgDir.getAbsoluteFile() + "/"
									+ MainActivity.mImgs.get(indexNum))){
					result = 1;
					for(int j = 0;j<MyAdapter.mSelectedImage.size();j++){
						if(MyAdapter.list.get(i).getPath().equals(MyAdapter.mSelectedImage.get(j))){
							MyAdapter.mSelectedImage.remove(j);
						}
					}
				}
			}
			// 选中-->未选中
			if(result == 1){
				MyAdapter.initList();
				initCheck();
				hListViewAdapter.notifyDataSetChanged();
				if(MyAdapter.list.size()<=0){
					hListView.setVisibility(View.INVISIBLE);
				}else{
					hListView.setVisibility(View.VISIBLE);
				}
			}else{ // 未选中-->选中
				if(MyAdapter.list.size() < chooseimages.maxSize){
					hListView.setVisibility(View.VISIBLE);
					MyAdapter.mSelectedImage.add(MainActivity.mImgDir.getAbsoluteFile() + "/"
										+ MainActivity.mImgs.get(indexNum));
					MyAdapter.initList();
					initCheck();
					hListViewAdapter.notifyDataSetChanged();
				}
			}
			initBtnOK();
			break;
		default:
			break;
		}

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
    	MyAdapter.initList();
		Intent mIntent = new Intent();
        mIntent.putExtra("isOk", "0");
        // 设置结果，并进行传送
        YuLanAllActivity.this.setResult(3, mIntent);
        YuLanAllActivity.this.finish();
    }


	public void showMsg(String msg) {
		Toast.makeText(YuLanAllActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	class ImageAdapter extends PagerAdapter {
		Context context;
		int[] images;

		public void init() {
			imageViews = new ArrayList<ImageView>();
			for (int i = 0; i < MainActivity.mImgs.size(); i++) {
				ImageView image = new ImageView(context);
				image.setScaleType(ScaleType.FIT_XY);
				// 使图片实现可以放大缩小的功能
				Glide.with(context)
						.load(MainActivity.mImgDir.getAbsoluteFile() + "/"
								+ MainActivity.mImgs.get(i)).centerCrop()
						.placeholder(Color.WHITE).crossFade().into(image);
				PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
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
