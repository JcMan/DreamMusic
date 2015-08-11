package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.ChapterEntry;

/**
 * Created by Administrator on 2015/8/10.
 */
public class ChapterListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChapterEntry> mList;
    public ChapterListAdapter(Context context,List<ChapterEntry> list){
        mList = list;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder ;
        if(convertView==null){
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.item_list_novelchapter,null);
            holder.tv_chapter = (TextView) convertView.findViewById(R.id.tv_item_novelchapter);
            convertView.setTag(holder);
        }else
            holder = (Holder) convertView.getTag();
        ChapterEntry entry = mList.get(position);
        holder.tv_chapter.setText(entry.getmChapterName());
        return convertView;
    }
    class Holder{
        TextView tv_chapter;
    }
}
