package com.youbanban.cordova.chooseimages.imageloader;

import java.util.List;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.youbanban.cordova.chooseimages.bean.ImageFloder;
import com.youbanban.cordova.chooseimages.utils.BasePopupWindowForListView;
import com.youbanban.cordova.chooseimages.utils.CommonAdapter;
import com.youbanban.cordova.chooseimages.utils.Util;
import com.youbanban.cordova.chooseimages.utils.ViewHolder;
import com.youbanban.app.R;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>{
	private ListView mListDir;

	public ListImageDirPopupWindow(int width, int height,List<ImageFloder> datas, View convertView){
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews(){
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,R.layout.list_dir_item){
			@Override
			public void convert(ViewHolder helper, ImageFloder item){
				helper.setText(R.id.id_dir_item_name, item.getName());
				int o= Util.readPictureDegree(item.getFirstImagePath());
				if(o==0){
					helper.setImageByUrl(R.id.id_dir_item_image,item.getFirstImagePath());
				}else {
					try {
						Bitmap bitmap = Util.compressPixel(item.getFirstImagePath());
						Bitmap bm=Util.rotateBitmap(bitmap,o);
						helper.setImageBitmap(R.id.id_dir_item_image, bm);
					} catch (Exception e) {
					}
				}
				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
			}
		});
	}

	public interface OnImageDirSelected{
		void selected(ImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected){
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents(){
		mListDir.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				if (mImageDirSelected != null){
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init(){
	}
	@Override
	protected void beforeInitWeNeedSomeParams(Object... params){
	}
}
