package com.youbanban.cordova.chooseimages.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.youbanban.app.R;

public class HorizontalListViewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context){
		this.mContext = context;
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
		holder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
		holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
		holder.rl_mengceng = (RelativeLayout) convertView.findViewById(R.id.rl_mengceng);
		convertView.setTag(holder);
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		for(int i = 0;i<MyAdapter.list.size();i++){
			if(MyAdapter.list.get(i).getNum() == position+1){
				holder.mImage.setPadding(5, 5, 5, 5);
				Glide.with(mContext).load(MyAdapter.list.get(i).getPath()).centerCrop()
					.placeholder(Color.BLACK).crossFade()
					.into(holder.mImage);if(position == selectIndex){
					holder.mImage.setBackgroundColor(Color.WHITE);
				}else{
					holder.mImage.setBackgroundColor(Color.parseColor("#00000000"));
				}
				if(MyAdapter.list.get(i).getIsDelete() == 1){
					holder.rl_mengceng.setVisibility(View.VISIBLE);
				}else{
					holder.rl_mengceng.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		private ImageView mImage;
		private RelativeLayout rl_mengceng;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}