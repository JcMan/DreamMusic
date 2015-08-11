package dream.app.com.dreammusic.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
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
    private Bitmap loadingBitmap;

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
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_list_novel,null);
            holder = new Holder();
            holder.iv_page = (ImageView) convertView.findViewById(R.id.iv_item_list_novel);
            holder.tv_bookname = (TextView) convertView.findViewById(R.id.tv_item_novel_name);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_item_novel_author);
            holder.tv_introduction = (TextView) convertView.findViewById(R.id.tv_item_novel_introduction);
            convertView.setTag(holder);
        }else
            holder = (Holder) convertView.getTag();
        NovelEntry entry = _List.get(position);
        FinalBitmap finalBitmap = FinalBitmap.create(mContext);
        loadingBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_novel);
        finalBitmap.display(holder.iv_page, entry.getmImgUrl(), loadingBitmap, loadingBitmap);
        holder.tv_bookname.setText(entry.getmBookName());
        holder.tv_author.setText(entry.getmAuthor());
        String intro = entry.getmIntroduction();
        if(intro.length()<3)
            intro = "本书暂无简介";
        holder.tv_introduction.setText(intro);
        return convertView;
    }

    class Holder{
        ImageView iv_page;
        TextView tv_bookname;
        TextView tv_author;
        TextView tv_introduction;
    }
}
