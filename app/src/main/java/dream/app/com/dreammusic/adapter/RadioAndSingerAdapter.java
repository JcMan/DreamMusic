package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tool.logger.Logger;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NetMusicEntry;

/**
 * Created by Administrator on 2015/7/3.
 */
public class RadioAndSingerAdapter extends BaseAdapter {
    private List<NetMusicEntry> mList;
    private Context mContext;
    private String types[];
    private Bitmap loadBitmap;
    public RadioAndSingerAdapter(Context context,List<NetMusicEntry> list,String[] types){
        mContext = context;
        mList = list;
        this.types = types;
        loadBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_singer);
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
        Holder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_radio_singer,null);
            holder = new Holder();
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon_radio_singer);
            holder.name = (TextView) convertView.findViewById(R.id.item_title_radio_singer);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        NetMusicEntry entry = mList.get(position);
        FinalBitmap finalBitmap = FinalBitmap.create(mContext);
        finalBitmap.display(holder.icon,entry.getString(types[0]),loadBitmap);
        holder.name.setText(mList.get(position).getString(types[1]));
        return convertView;
    }

    class Holder{
        ImageView icon;
        TextView name;
    }

}
