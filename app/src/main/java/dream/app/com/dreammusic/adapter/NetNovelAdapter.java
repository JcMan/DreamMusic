package dream.app.com.dreammusic.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.ui.activity.BaseActivity;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NetNovelAdapter extends BaseAdapter {

    private Context mContext;
    private List<NovelEntry> _List;

    public NetNovelAdapter(Context context,List<NovelEntry> list){
        mContext = context;
        _List = list;
    }
    @Override
    public int getCount() {
        return _List.size();
    }

    @Override
    public Object getItem(int position) {
        return _List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_list_novel,null);
            holder = new Holder();
            holder.iv_page = (ImageView) convertView.findViewById(R.id.iv_item_list_novel);
            holder.tv_bookname = (TextView) convertView.findViewById(R.id.tv_item_novel_name);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_item_novel_author);
            holder.tv_introduction = (TextView) convertView.findViewById(R.id.tv_item_novel_introduction);
            convertView.setTag(holder);;
        }else
        holder = (Holder) convertView.getTag();
        NovelEntry entry = _List.get(position);
        FinalBitmap finalBitmap = FinalBitmap.create(mContext);
        finalBitmap.display(holder.iv_page,entry.getmImgUrl());
        holder.tv_bookname.setText(entry.getmBookName());
        holder.tv_author.setText(entry.getmAuthor());
        holder.tv_introduction.setText(entry.getmIntroduction());
        return convertView;
    }

    class Holder{
        ImageView iv_page;
        TextView tv_bookname;
        TextView tv_author;
        TextView tv_introduction;
    }
}
