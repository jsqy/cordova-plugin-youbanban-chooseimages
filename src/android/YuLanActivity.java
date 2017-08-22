package com.youbanban.cordova.chooseimages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import com.bumptech.glide.Glide;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListView;
import com.youbanban.cordova.chooseimages.imageloader.HorizontalListViewAdapter;
import com.youbanban.cordova.chooseimages.imageloader.MainActivity;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;
import com.youbanban.cordova.chooseimages.utils.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class YuLanActivity extends Activity{

	private int[] imgs = { R.drawable.array_left, R.drawable.array_left,
			R.drawable.array_left, R.drawable.array_left, R.drawable.array_left };

	private ViewFlipper viewFlipper;
	private Activity mActivity;

	private RelativeLayout rl_back;
	private RelativeLayout rl_delete;
	private LinearLayout ll_yulan_suo;
	private ImageView img_index;
	private ImageView img_xuanze;
	private Button btn_ok;
	private int num = 0;
	private int indexNum = 0;

	private float mPosX;
	private float mPosY;
	private float mCurrentPosX;
	private float mCurrentPosY;

	HorizontalListView hListView;
	HorizontalListViewAdapter hListViewAdapter;
	ImageView previewImg;
	View olderSelectView = null;

	private int isOver = 1;
	private int isSelectNum = 0;


	ViewPager mViewPager;
	List<PhotoView> imageViews;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseimage_yulan);
		mActivity = this;
		initView();
		initBitmap();
//		initBigImage();
		// initFlipper();
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
				// TODO Auto-generated method stub
				indexNum = arg0;
				initIsDeleteImage();
				hListViewAdapter.setSelectIndex(indexNum);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void initBitmap() {
		for (int i = 0; i < MyAdapter.list.size(); i++) {
//			Bitmap bitmap = getCompressedBitmap(MyAdapter.list.get(i).getPath(), 100, 100);
//			Bitmap bitmapBig = getCompressedBitmap(MyAdapter.list.get(i).getPath(), 800, 600);
//			MyAdapter.list.get(i).setBitmap(bitmap);
//			MyAdapter.list.get(i).setBitmapBig(bitmapBig);
			MyAdapter.list.get(i).setIsDelete(0);
		}
	}

	private void initTest() {

		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		String[] titles = { "01", "02", "03", "04", "5", "6" };
		final int[] ids = { R.drawable.ar_title, R.drawable.ar_title,
				R.drawable.ar_title, R.drawable.ar_title, R.drawable.ar_title,
				R.drawable.ar_title };
		hListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), titles, ids);
		hListViewAdapter.setSelectIndex(0);
		hListView.setAdapter(hListViewAdapter);

		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// if(olderSelectView == null){
				// olderSelectView = view;
				// }else{
				// olderSelectView.setSelected(false);
				// olderSelectView = null;
				// }
				mViewPager.setCurrentItem(position);
				indexNum = position;
//				initIndexImage();
				initIsDeleteImage();
				// olderSelectView = view;
				// view.setSelected(true);
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
		btn_ok.setText("完成"+isSelectNum+"/"+chooseimages.maxSize);
	}

	private void initView() {
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		img_xuanze = (ImageView) findViewById(R.id.img_xuanze);
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}




	private void initIsDeleteImage(){

		if(MyAdapter.list.get(indexNum).getIsDelete() == 1){
			img_xuanze.setImageDrawable(getResources().getDrawable(R.drawable.picture_unselected));
		}else{
			img_xuanze.setImageDrawable(getResources().getDrawable(R.drawable.pictures_selected));
		}
		initBtnOK();
	}

	private void initOnClick() {

		btn_ok.setOnClickListener(new OnClickListener() {

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
		        mIntent.putExtra("isOk", "1");
		        // 设置结果，并进行传送
		        YuLanActivity.this.setResult(2, mIntent);
				YuLanActivity.this.finish();
			}
		});

//		img_index.setOnTouchListener(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				switch (event.getAction()) {
//				// 按下
//				case MotionEvent.ACTION_DOWN:
//					mPosX = event.getX();
//					mPosY = event.getY();
//					break;
//				// 移动
//				case MotionEvent.ACTION_MOVE:
//
//
//					break;
//				// 拿起
//				case MotionEvent.ACTION_UP:
//					mCurrentPosX = event.getX();
//					mCurrentPosY = event.getY();
//					if (mCurrentPosX - mPosX > 100){
//						// 右
//						if(indexNum >0){
//							indexNum = (indexNum -1);
//							img_index.setImageBitmap(MyAdapter.list.get(indexNum).getBitmapBig());
//							hListViewAdapter.setSelectIndex(indexNum);
//							hListViewAdapter.notifyDataSetChanged();
//						}
//						initIsDeleteImage();
//					}
//
//					else if (mCurrentPosX - mPosX < -100){
//						// 左
//						if(indexNum <MyAdapter.list.size()-1){
//							indexNum = (indexNum +1);
//							img_index.setImageBitmap(MyAdapter.list.get(indexNum).getBitmapBig());
//							hListViewAdapter.setSelectIndex(indexNum);
//							hListViewAdapter.notifyDataSetChanged();
//						}
//						initIsDeleteImage();
//					}
////					else if (mCurrentPosY - mPosY > 20
////							&& Math.abs(mCurrentPosX - mPosX) < 10)
////						showMsg("向下边");
////					else if (mCurrentPosY - mPosY < -20
////							&& Math.abs(mCurrentPosX - mPosX) < 10)
////						showMsg("向上边");
//					break;
//				default:
//					break;
//				}
//				return true;
//			}
//		});

		// img_index.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// showMsg("点击的是图片");
		// }
		// });

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
//        super.onBackPressed();
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

	private void initFlipper() {

		viewFlipper.removeAllViews();
		for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) { // 添加图片源
			Log.e("xulei", "08");
			ImageView iv = new ImageView(this);

			Bitmap bitmap = getLoacalBitmap(MyAdapter.mSelectedImage.get(i)); // 从本地取图片(在cdcard中获取)
																				// //
			iv.setImageBitmap(bitmap); // 设置Bitmap

			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

	}

	/**
	 * 加载本地图片
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.RGB_565;
			Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
			return bitmap; // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}








	private void showMsg(String msg) {
		Toast.makeText(mActivity, msg, 2000).show();
	}

	/**
	 * 通过图片的绝对路径来获取对应的压缩后的Bitmap对象
	 */
	public static Bitmap getCompressedBitmap(String filePath, int requireWidth,
			int requireHeight) {
		// 第一次解析将inJustDecodeBounds设置为true,用以获取图片大小,并且不需要将Bitmap对象加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options); // 第一次解析
		// 计算inSampleSize的值,并且赋值给Options.inSampleSize
		options.inSampleSize = calculateInSampleSize(options, requireWidth,
				requireHeight);
		// 使用获取到的inSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算压缩的inSampleSize的值,该值会在宽高上都进行压缩(也就是压缩前后比例是inSampleSize的平方倍)
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int requireWidth, int requireHeight) {
		// 获取源图片的实际的宽度和高度
		int realWidth = options.outWidth;
		int realHeight = options.outHeight;

		int inSampleSize = 1;
		if (realWidth > requireWidth || realHeight > requireHeight) {
			// 计算出实际的宽高与目标宽高的比例
			int widthRatio = Math.round((float) realWidth
					/ (float) requireWidth);
			int heightRatio = Math.round((float) realHeight
					/ (float) requireHeight);
			// 选择宽高比例最小的值赋值给inSampleSize,这样可以保证最终图片的宽高一定会大于或等于目标的宽高
			inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	     class ImageAdapter extends PagerAdapter {
		         Context context;
		         int[] images;

		         public void init() {
		             imageViews = new ArrayList<PhotoView>();
		             for (int i = 0; i < MyAdapter.list.size(); i++) {
//		                 ImageView image = new ImageView(context);
		                 PhotoView image = new PhotoView(context);
		                 image.setScaleType(ScaleType.FIT_XY);
		                 Glide.with(YuLanActivity.this).load(MyAdapter.list.get(i).getPath()).into(image);
//		                 Glide.with(context).load(MyAdapter.list.get(i).getPath()).centerCrop()
//		 	            .placeholder(Color.WHITE).crossFade()
//		 	            .into(image);
//		                 Glide.with(context).
		                 //使图片实现可以放大缩小的功能
//		                 PhotoViewAttacher mAttacher=new PhotoViewAttacher(image);


		                 imageViews.add(image);


		             }
		 //            for (int i : images) {
		 //                ImageView image = new ImageView(context);
		 //                image.setImageResource(i);
		 //                imageViews.add(image);
		 //             }
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
//		             showMsg(position+"");
//		             indexNum = position;
//		             initIsDeleteImage();
//		             hListViewAdapter.setSelectIndex(indexNum);
//		             hListViewAdapter.notifyDataSetChanged();
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
