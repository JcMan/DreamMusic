package dream.app.com.dreammusic.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.util.MusicUtil;

public class LocalMusicAdapter extends BaseAdapter {
    private List<Music> _List;
    private Context mContext;

    public LocalMusicAdapter(Context context,List<Music> list){
        _List = list;
        mContext = context;
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
        LocalHolder holder;
        if(convertView==null){
            holder = new LocalHolder();
            convertView=View.inflate(mContext, R.layout.item_localmusic_list,null);
            holder.mTitle = (TextView) convertView.findViewById(R.id.tv_item_local_title);
            convertView.setTag(holder);
        }else{
            holder = (LocalHolder) convertView.getTag();
        }
        Music music = (Music) _List.get(position);
        if (music.musicName.contains("-")){
            String _S[] = MusicUtil.getMusicName(music.musicName);
            if(music.artist.contains("un"))
                holder.mTitle.setText(music.musicName);
            else
                holder.mTitle.setText(_S[0]+" - "+_S[1]);
        }else{
            if(music.artist.contains("un"))
                holder.mTitle.setText(music.musicName);
            else
                holder.mTitle.setText(music.artist+" - "+music.musicName);
        }
        return convertView;
    }
    class LocalHolder{
        TextView mTitle;
    }
}
