package com.youbanban.cordova.chooseimages.imageloader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.youbanban.app.R;
import com.youbanban.cordova.chooseimages.YuLanAllActivity;

public class HorizontalListViewAdapterAll extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private int selectIndex = -1;

	public HorizontalListViewAdapterAll(Context context){
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
			convertView = mInflater.inflate(R.layout.horizontal_list_item_all, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.rl_mengceng = (RelativeLayout) convertView.findViewById(R.id.rl_mengceng);
			holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
			convertView.setTag(holder);
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
		return convertView;
	}

	private static class ViewHolder {
		private ImageView mImage;
		private RelativeLayout rl_mengceng;
		private RelativeLayout rl_item;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}