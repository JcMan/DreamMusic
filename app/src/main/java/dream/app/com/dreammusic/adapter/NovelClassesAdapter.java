package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NovelAPI;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelClassesAdapter extends BaseAdapter {

    private Context mContext;
    private List<HashMap<String,String>> _List = new ArrayList<HashMap<String, String>>();

    public NovelClassesAdapter(Context context){
        mContext = context;
        _List = NovelAPI.getClassificationNameAndUrl(context);
    }
    @Override
    public int getCount() {
        return 9;
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
        convertView = View.inflate(mContext, R.layout.item_list_novel_classes,null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_item_list_novel_classes);
        HashMap<String,String> map = _List.get(position);
        tv.setText(map.get("name"));
        return convertView;
    }
}
