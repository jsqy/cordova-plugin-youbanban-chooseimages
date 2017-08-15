package com.youbanban.cordova.chooseimages;  
  
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.imageloader.MyAdapter;

import android.app.Activity;  
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;  
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
import android.widget.TextView;
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
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseimage_yulan);
        mActivity = this;
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        gestureDetector = new GestureDetector(this);

        initFlipper();

        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(MyAdapter.mSelectedImage.size() == 1){
					MyAdapter.mSelectedImage.remove(num);
					YuLanActivity.this.finish();
				}else{
					MyAdapter.mSelectedImage.remove(num);
					initFlipper();
				}


			}
		});

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				YuLanActivity.this.finish();
			}
		});
    }

    private void initFlipper(){
    	viewFlipper.removeAllViews();
    	for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) { // 添加图片源
            ImageView iv = new ImageView(this);
            Bitmap bitmap = getLoacalBitmap(MyAdapter.mSelectedImage.get(i)); //从本地取图片(在cdcard中获取)  //
            iv .setImageBitmap(bitmap); //设置Bitmap

            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }
    	  viewFlipper.setAutoStart(true); // 设置自动播放功能（点击事件，前自动播放）
          viewFlipper.setFlipInterval(3000);
          if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
              viewFlipper.startFlipping();
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
              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

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
