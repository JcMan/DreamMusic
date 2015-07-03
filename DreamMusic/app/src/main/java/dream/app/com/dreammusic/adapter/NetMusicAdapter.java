package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NetMusic;

/**
 * Created by Administrator on 2015/7/3.
 */
public class NetMusicAdapter extends BaseAdapter {
    private List<NetMusic> mList;
    private Context mContext;
    public NetMusicAdapter(Context context,List<NetMusic> list){
        mContext = context;
        mList = list;
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
        Holder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_netmusic,null);
            holder = new Holder();
            holder.icon = (ImageView) convertView.findViewById(R.id.item_netmusic_icon);
            holder.title = (TextView) convertView.findViewById(R.id.item_netmusic_title);
            holder.author = (TextView) convertView.findViewById(R.id.item_netmusic_author);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.icon.setImageResource(R.drawable.singer);
        holder.title.setText("幻听");
        holder.author.setText("许嵩");
        return convertView;
    }

    class Holder{
        ImageView icon;
        TextView title;
        TextView author;
    }

}
