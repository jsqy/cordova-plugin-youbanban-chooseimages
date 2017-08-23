package com.youbanban.cordova.chooseimages.imageloader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.cordova.PluginResult;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.bean.ImageFloder;
import com.youbanban.cordova.chooseimages.imageloader.ListImageDirPopupWindow.OnImageDirSelected;
import com.youbanban.cordova.chooseimages.utils.Util;
import com.youbanban.app.R;

public class MainActivity extends Activity implements OnImageDirSelected
{
	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	public static File mImgDir;
	/**
	 * 所有的图片
	 */
	public static List<String> mImgs;

	private GridView mGirdView;
	private MyAdapter mAdapter;
	public static TextView tv_ok;
	private TextView tv_back;
	private TextView tv_title;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private RelativeLayout rl_choose_dir;
	public static TextView mImageCount;
	private RelativeLayout rl_yulan;
	int totalCount = 0;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}
//		mImgDir = new File(mImageFloders.get(0).getDir());
//		mImgs = Arrays.asList(mImgDir.list());

		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
		{
		@Override
		public boolean accept(File dir, String filename)
		{
			if (filename.endsWith(".jpg") || filename.endsWith(".png")
					|| filename.endsWith(".jpeg"))
				return true;
			return false;
		}
	}));

//		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
//		{
//			@Override
//			public boolean accept(File dir, String filename)
//			{
//				if (filename.endsWith(".jpg") || filename.endsWith(".png")
//						|| filename.endsWith(".jpeg"))
//					return true;
//				return false;
//			}
//		}));

//		mChooseDir.setText(mImgDir.getName());
		tv_title.setText("相册:"+mImgDir.getName());
		mChooseDir.setText("切换相册");
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
//		Collections.sort(mImgs, new FileComparator());
		Collections.reverse(mImgs);
		mAdapter = new MyAdapter(MainActivity.this,getApplicationContext(), mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText("预览"+"("+MyAdapter.mSelectedImage.size() + ")");
//		mImageCount.setText(MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
		MainActivity.tv_ok.setText("确认 "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
	};


	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseimage_main);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;

		initView();
		getImages();
		initEvent();

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = MainActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
		rl_yulan = (RelativeLayout) findViewById(R.id.rl_total_count);
		rl_choose_dir = (RelativeLayout) findViewById(R.id.rl_choose_dir);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);

		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);

	}

	private void initEvent()
	{

		rl_yulan.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(MyAdapter.mSelectedImage.size() >0){
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, com.youbanban.cordova.chooseimages.YuLanActivity.class);
					startActivityForResult(intent, 2);
				}
			}
		});

		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		rl_choose_dir.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
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

//				PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,"中间消息");
//				pluginResult.setKeepCallback(true);
//				chooseimages.callbackContext.sendPluginResult(pluginResult);

		        Intent mIntent = new Intent();
				MainActivity.this.setResult(RESULT_OK, mIntent);
				MainActivity.this.finish();
			}
		});

		tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,"中间消息");
//				pluginResult.setKeepCallback(true);
//				chooseimages.callbackContext.sendPluginResult(pluginResult);

		        Intent mIntent = new Intent();
				MainActivity.this.setResult(RESULT_OK, mIntent);
				MyAdapter.mSelectedImage = new LinkedList<String>();
				MainActivity.this.finish();
			}
		});


	}

	 // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
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
        		mImageCount.setText("预览"+"("+MyAdapter.mSelectedImage.size() + ")");
        		MainActivity.tv_ok.setText("确认 "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
        	}
            break;
        case 3:
    		if(data.getStringExtra("isOk").equals("1")){
    			Intent mIntent = new Intent();
				MainActivity.this.setResult(RESULT_OK, mIntent);
				MainActivity.this.finish();
        	}else{
        		mAdapter.notifyDataSetChanged();
        		mImageCount.setText("预览"+"("+MyAdapter.mSelectedImage.size() + ")");
        		MainActivity.tv_ok.setText("确认 "+MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
        	}
            break;
        default:
            break;
        }
    }

    private void showMsg(String msg){
    	Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		Collections.reverse(mImgs);
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(MainActivity.this,getApplicationContext(), mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
//		mImageCount.setText(floder.getCount() + "张");
		String name = floder.getName();
		name = name.substring(1, name.length());
		tv_title.setText("相册:"+name);
		mListImageDirPopupWindow.dismiss();

	}

}
