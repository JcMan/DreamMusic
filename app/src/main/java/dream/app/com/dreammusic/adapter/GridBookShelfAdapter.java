package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NovelEntry;

/**
 * Created by Administrator on 2015/8/12.
 */
public class GridBookShelfAdapter extends BaseAdapter{

    private List<NovelEntry> mList;
    private Context mContext;
    private FinalBitmap finalBitmap;
    private Bitmap loadingBitmap;

    public GridBookShelfAdapter(Context context,List<NovelEntry> list){
        mList = list;
        mContext = context;
        finalBitmap = FinalBitmap.create(mContext);
        loadingBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_novel);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
       Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.item_grid_bookshelf,null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_page_bookshelf);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name_bookshelf);
            convertView.setTag(holder);
        }else
            holder = (Holder) convertView.getTag();
        NovelEntry entry = mList.get(position);
        finalBitmap.display(holder.iv,entry.getmImgUrl(),loadingBitmap,loadingBitmap);
        holder.tv.setText(entry.getmBookName());
        return convertView;
    }
    class Holder {
        ImageView iv;
        TextView tv;
    }
}
