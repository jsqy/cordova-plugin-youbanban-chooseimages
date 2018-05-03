package com.youbanban.cordova.chooseimages.imageloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.YuLanActivity;
import com.youbanban.cordova.chooseimages.bean.ImageFloder;
import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.imageloader.ListImageDirPopupWindow.OnImageDirSelected;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements OnImageDirSelected{
	private ProgressDialog mProgressDialog;
	private int mPicsSize;// 存储文件夹中的图片数量
	public static File mImgDir;// 图片数量最多的文件夹
	public static List<String> mImgs;//所有的图片
	private GridView mGirdView;
	private MyAdapter mAdapter;
	public static TextView tv_ok;
	private TextView tv_back;
	private TextView tv_title;
	private HashSet<String> mDirPaths = new HashSet<String>();// 临时的辅助类，用于防止同一个文件夹的多次扫描
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();// 扫描拿到所有的图片文件夹
	private RelativeLayout mBottomLy;
	private RelativeLayout rl_choose_dir;
	public static TextView mImageCount;
	int totalCount = 0;
	private int mScreenHeight;
	private ListImageDirPopupWindow mListImageDirPopupWindow;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			mProgressDialog.dismiss();
			data2View();// 为View绑定数据
			initListDirPopupWindw();// 初始化展示文件夹的popupWindw
		}
	};

	private void data2View(){
		if (mImgDir == null){
			Toast.makeText(getApplicationContext(), "图片加载失败!",
				Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename){
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
					|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		Collections.sort(mImgs,new FileComparator());
		tv_title.setText("相册:"+mImgDir.getName());
		mAdapter = new MyAdapter(MainActivity.this,getApplicationContext(), mImgs,
			R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		tv_ok.setText("确认( "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize+" )");
	};

	private void initListDirPopupWindw(){
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
			LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
			mImageFloders, LayoutInflater.from(getApplicationContext())
			.inflate(R.layout.list_dir, null));
		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(){// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseimage_main);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		initView();
		getImages();
		initEvent();
	}

	private void getImages(){
		if (!Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		String firstImage = null;
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = MainActivity.this.getContentResolver();
		Cursor mCursor = mContentResolver.query(mImageUri, null,
			MediaStore.Images.Media.MIME_TYPE + "=? or "+ MediaStore.Images.Media.MIME_TYPE + "=?",
			new String[] { "image/jpeg", "image/png" },
			MediaStore.Images.Media.DATE_MODIFIED);
		while (mCursor.moveToNext()){
			String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
			if (firstImage == null)
				firstImage = path;
			File parentFile = new File(path).getParentFile();
			if (parentFile == null)
				continue;
			String dirPath = parentFile.getAbsolutePath();
			ImageFloder imageFloder = null;
			// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
			if (mDirPaths.contains(dirPath)){
				continue;
			} else{
				mDirPaths.add(dirPath);
				imageFloder = new ImageFloder();
				imageFloder.setDir(dirPath);
				imageFloder.setFirstImagePath(path);
			}
			int picSize = parentFile.list(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String filename){
					if (filename.endsWith(".jpg")
						|| filename.endsWith(".png")
						|| filename.endsWith(".jpeg")
						||filename.equals("bmp"))
						return true;
					return false;
				}
			}).length;
			totalCount += picSize;
			imageFloder.setCount(picSize);
			mImageFloders.add(imageFloder);
			if (picSize > mPicsSize){
				mPicsSize = picSize;
				mImgDir = parentFile;
			}
		}
		mCursor.close();
		mDirPaths = null;
		mHandler.sendEmptyMessage(0x110);
	}

	private void initView(){
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		//mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
		rl_choose_dir = (RelativeLayout) findViewById(R.id.rl_choose_dir);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
	}

	private void initEvent(){
		mImageCount.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(MyAdapter.mSelectedImage.size() >0){
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, YuLanActivity.class);
					startActivityForResult(intent, 2);
				}
			}
		});

		rl_choose_dir.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mListImageDirPopupWindow
					.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});

		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				MainActivity.this.setResult(RESULT_OK, mIntent);
				MainActivity.this.finish();
			}
		});

		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				MainActivity.this.setResult(RESULT_OK, mIntent);
				MyAdapter.mSelectedImage = new LinkedList<String>();
				MainActivity.this.finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				break;
			case 2:
				if(data.getStringExtra("isOk").equals("1")){
					Intent mIntent = new Intent();
					MainActivity.this.setResult(RESULT_OK, mIntent);
					MainActivity.this.finish();
				}else{
					mAdapter.notifyDataSetChanged();
					tv_ok.setText("确认( "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize+" )");
				}
				break;
			case 3:
				if(data.getStringExtra("isOk").equals("1")){
					Intent mIntent = new Intent();
					MainActivity.this.setResult(RESULT_OK, mIntent);
					MainActivity.this.finish();
				}else{
					mAdapter.notifyDataSetChanged();
					tv_ok.setText("确认( "+MyAdapter.mSelectedImage.size()+"/"+ chooseimages.maxSize+ ")");
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void selected(ImageFloder floder){
		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String filename){
				if (filename.endsWith(".jpg") || filename.endsWith(".png")|| filename.endsWith(".jpeg")) {
					return true;
				}else {
					return false;
				}
			}
		}));
		Collections.sort(mImgs,new FileComparator());
		// 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		mAdapter = new MyAdapter(MainActivity.this,getApplicationContext(), mImgs,
			R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		String name = floder.getName();
		name = name.substring(1, name.length());
		if(name.length()>12){
			name=name.substring(0,12)+" ... ";
		}
		tv_title.setText("相册:"+name);
		mListImageDirPopupWindow.dismiss();
	}

	public class FileComparator implements Comparator<String> {
		@Override
		public int compare(String lhs, String rhs) {
			try{
				if (new File(mImgDir.getPath()+"/"+lhs).lastModified() < new File(mImgDir.getPath()+"/"+rhs).lastModified())
					return 1;
				else
					return -1;
			}catch (Exception e){
				return -1;
			}
		}
	}
}
