package dream.app.com.dreammusic.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.BgEntry;

public class GridBgAdapter extends BaseAdapter {

	private List<BgEntry> bgList;
	private Activity mActivity;
	private Resources resources;
	private String mDeafultPath;
	
	public GridBgAdapter(List<BgEntry> list,String defaultpath,Activity activity) {
		this.bgList = list;
		this.resources = activity.getResources();
		mActivity = activity;
		mDeafultPath = defaultpath;
		
	}

    public void setDeafultPath(String path){
        mDeafultPath = path;
    }

	@Override
	public int getCount() {
		return bgList.size();
	}

	@Override
	public BgEntry getItem(int arg0) {
		return bgList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mActivity.getLayoutInflater().inflate(
					R.layout.gird_item_chbg, null);
			viewHolder.backgroundIv = (ImageView) convertView
					.findViewById(R.id.gridview_item_iv);
			viewHolder.checkedIv = (ImageView) convertView
					.findViewById(R.id.gridview_item_checked_iv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.backgroundIv.setBackgroundDrawable(new BitmapDrawable(
				resources, getItem(position).bitmap));
		if (getItem(position).path.equals(mDeafultPath)) {
			viewHolder.checkedIv.setVisibility(View.VISIBLE);
		} else {
			viewHolder.checkedIv.setVisibility(View.GONE);
		}

		return convertView;
	}
	private class ViewHolder {
		ImageView checkedIv, backgroundIv;
	}
}
