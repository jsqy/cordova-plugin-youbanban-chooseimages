package com.youbanban.cordova.chooseimages.imageloader;

import java.util.LinkedList;
import java.util.List;

import org.apache.cordova.LOG;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.youbanban.cordova.chooseimages.chooseimages;
import com.youbanban.cordova.chooseimages.utils.CommonAdapter;
import com.youbanban.app.R;

public class MyAdapter extends CommonAdapter<String>
{

	private Context context;
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		context = context;
		this.mDirPath = dirPath;
	}

	@Override
	public void convert(final com.youbanban.cordova.chooseimages.utils.ViewHolder helper, final String item)
	{

		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
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
		if(item.indexOf("youbanban.ar") != -1){
			mSelectAR.setImageResource(R.drawable.ar_title);
		}

//		for(int i = 0;i<mSelectedImage.size();i++){
//			if (mSelectedImage.get(i).equals(mDirPath + "/" + item))
//			{
//				mSelect.setImageResource(R.drawable.yellow_background);
//				tv_item_select.setText(i+"");
//				mImageView.setColorFilter(Color.parseColor("#77000000"));
//			}
//		}
//

		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
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
//					MainActivity.mImageCount.setText(MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
				} else
				// 未选择该图片
				{
					if(MyAdapter.mSelectedImage.size() < chooseimages.maxSize ){
//						for(int i = 0;i<mSelectedImage.size();i++){
//							Toast.makeText(mContext, mSelectedImage.get(i), 2000).show();
//						}
						mSelectedImage.add(mDirPath + "/" + item);
//						notifyDataSetChanged();

						mSelect.setImageResource(R.drawable.pictures_selected);
//						mSelect.setImageResource(R.drawable.yellow_background);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
//						MainActivity.mImageCount.setText(MyAdapter.mSelectedImage.size()+"/"+chooseimages.maxSize);
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
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
}
