package com.youbanban.cordova.chooseimages.imageloader;



import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.youbanban.app.R;
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

public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context, String[] titles, int[] ids){
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
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.rl_mengceng = (RelativeLayout) convertView.findViewById(R.id.rl_mengceng);
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
		

		for(int i = 0;i<MyAdapter.list.size();i++){
			if(MyAdapter.list.get(i).getNum() == position+1){
				holder.mImage.setPadding(2, 2, 2, 2);
				holder.mImage.setImageBitmap(MyAdapter.list.get(i).getBitmap());
				if(position == selectIndex){
					holder.mImage.setBackgroundColor(Color.YELLOW);
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
		
//		holder.mImage.setImageBitmap(bitmap);
		
		return convertView;
	}
	



	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
		private RelativeLayout rl_mengceng;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}