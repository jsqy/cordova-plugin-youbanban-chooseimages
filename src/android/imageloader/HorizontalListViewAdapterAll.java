package com.youbanban.cordova.chooseimages.imageloader;



import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.bumptech.glide.Glide;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.YuLanActivity;
import com.youbanban.cordova.chooseimages.YuLanAllActivity;
import com.youbanban.cordova.chooseimages.utils.BitmapUtil;
import com.youbanban.cordova.chooseimages.utils.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class HorizontalListViewAdapterAll extends BaseAdapter{
	private int[] mIconIDs;
	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapterAll(Context context, String[] titles, int[] ids){
		this.mContext = context;
		this.mIconIDs = ids;
		this.mTitles = titles;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return MyAdapter.mSelectedImage.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
//		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item_all, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.rl_mengceng = (RelativeLayout) convertView.findViewById(R.id.rl_mengceng);
			holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
//			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder)convertView.getTag();
//		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		// 有图片的情况
		if(MyAdapter.list.size()>0){
			holder.rl_item.setVisibility(View.VISIBLE);
			for(int i = 0;i<MyAdapter.list.size();i++){
				if(MyAdapter.list.get(i).getNum() == position+1){
					Glide.with(mContext).load(MyAdapter.list.get(i).getPath()).centerCrop()
		            .placeholder(Color.BLACK).crossFade()
		            .into(holder.mImage);
//					holder.mImage.setImageBitmap(MyAdapter.list.get(i).getBitmap());
					//在ListView中加载列表图片

//					Glide.with(mContext).load(MyAdapter.list.get(i).getPath()).into(holder.mImage);
					if(MyAdapter.list.get(i).getPath().equals(MainActivity.mImgDir.getAbsoluteFile() + "/"+MainActivity.mImgs.get(YuLanAllActivity.indexNum))){
						holder.mImage.setBackgroundColor(Color.rgb(255, 228, 98));
						holder.mImage.setPadding(5, 5, 5, 5);
					}else{
						holder.mImage.setBackgroundColor(Color.BLACK);
					}
					
					if(MyAdapter.list.get(i).getIsDelete() == 1){
						holder.rl_mengceng.setVisibility(View.VISIBLE);
					}else{
						holder.rl_mengceng.setVisibility(View.GONE);
					}

				}
			}
		}else{
			holder.rl_item.setVisibility(View.INVISIBLE);
		}
		// 没有图片的情况

		
//		holder.mImage.setImageBitmap(bitmap);
		
		return convertView;
	}
	



	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
		private RelativeLayout rl_mengceng;
		private RelativeLayout rl_item;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}