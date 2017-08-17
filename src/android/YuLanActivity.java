package com.youbanban.cordova.chooseimages;  
  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.imageloader.MainActivity;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class YuLanActivity extends Activity implements
        android.view.GestureDetector.OnGestureListener {

    private int[] imgs = { R.drawable.array_left, R.drawable.array_left, R.drawable.array_left,
            R.drawable.array_left, R.drawable.array_left };

    private GestureDetector gestureDetector;
    private ViewFlipper viewFlipper;
    private Activity mActivity;

    private TextView tv_delete;
    private TextView tv_back;
    private RelativeLayout rl_back;
    private RelativeLayout rl_delete;
    private int num = 0;

    private int isOver = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseimage_yulan);
        mActivity = this;
        Log.e("xulei","01");
   	 	viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
   	 	Log.e("xulei","02");
   	 	gestureDetector = new GestureDetector(this);
        Log.e("xulei","03");
        initFlipper();
        Log.e("xulei","04");
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
        rl_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				  Log.e("xulei","04");
				if(isOver == 1){
					isOver = 0;
					if(MyAdapter.mSelectedImage.size() == 1){
						MyAdapter.mSelectedImage.remove(num);
						YuLanActivity.this.finish();
					}else{

						MyAdapter.mSelectedImage.remove(num);
						MyAdapter.initList();
						Toast.makeText(mActivity, "删除成功!", 2000).show();
						initFlipper();
						new Handler().postDelayed(new Runnable(){
						    public void run() {
						    isOver =1;
						    }
						 }, 300);
					}
				}

			}
		});
        Log.e("xulei","05");
        tv_back = (TextView) findViewById(R.id.tv_back);

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YuLanActivity.this.finish();
			}
		});
        Log.e("xulei","06");
    }

    private void initFlipper(){

    	  Log.e("xulei","07");

    	viewFlipper.removeAllViews();
    	for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) { // 添加图片源
    		  Log.e("xulei","08");
            ImageView iv = new ImageView(this);

            Bitmap bitmap = getLoacalBitmap(MyAdapter.mSelectedImage.get(i)); //从本地取图片(在cdcard中获取)  //
            iv .setImageBitmap(bitmap); //设置Bitmap

            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }

    }


    /**
    * 加载本地图片
    * @param url
    * @return
    */
    public static Bitmap getLoacalBitmap(String url) {
         try {
              FileInputStream fis = new FileInputStream(url);
              BitmapFactory.Options options = new BitmapFactory.Options();
              options.inPreferredConfig = Config.RGB_565;
              Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
              return bitmap;  ///把流转化为Bitmap图片

           } catch (FileNotFoundException e) {
              e.printStackTrace();
              return null;
         }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        viewFlipper.stopFlipping(); // 点击事件后，停止自动播放
        viewFlipper.setAutoStart(false);
        return gestureDetector.onTouchEvent(event); // 注册手势事件
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
            float arg3) {
        if (e2.getX() - e1.getX() > 120) {
        	if(num == 0){
        		num = MyAdapter.mSelectedImage.size()-1;
        	}else{
        		num = num -1;
        	}

        	// 从左向右滑动（左进右出）
            Animation rInAnim = AnimationUtils.loadAnimation(mActivity,
                    R.anim.push_right_in); // 向右滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(mActivity,
                    R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

            viewFlipper.setInAnimation(rInAnim);
            viewFlipper.setOutAnimation(rOutAnim);
            viewFlipper.showPrevious();
            return true;
        } else if (e2.getX() - e1.getX() < -120) {
        	if(num == MyAdapter.mSelectedImage.size()-1){
        		num = 0;
        	}else{
        		num = num +1;
        	}
        	// 从右向左滑动（右进左出）
            Animation lInAnim = AnimationUtils.loadAnimation(mActivity,
                    R.anim.push_left_in); // 向左滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
            Animation lOutAnim = AnimationUtils.loadAnimation(mActivity,
                    R.anim.push_left_out); // 向左滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

            viewFlipper.setInAnimation(lInAnim);
            viewFlipper.setOutAnimation(lOutAnim);
            viewFlipper.showNext();
            return true;
        }

        return true;
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	System.gc();
    }
  
    @Override  
    public void onLongPress(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
  
    }  
  
    @Override  
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,  
            float arg3) {  
        // TODO Auto-generated method stub  
        return false;  
    }  
  
    @Override  
    public void onShowPress(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
  
    }  
  
    @Override  
    public boolean onSingleTapUp(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        return false;  
    }  
  
}  
