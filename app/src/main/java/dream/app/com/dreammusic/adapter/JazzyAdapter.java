package dream.app.com.dreammusic.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dream.app.com.dreammusic.ui.view.jazzy.JazzyViewPager;
import dream.app.com.dreammusic.ui.view.jazzy.OutlineContainer;

public class JazzyAdapter extends PagerAdapter {

    private Context mContext;
    private List<View> mList;
    private JazzyViewPager mJazzy;
    public JazzyAdapter(Context context,List<View> list,JazzyViewPager jazzy) {
        mContext = context;
        mList = list;
        mJazzy = jazzy;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ((JazzyViewPager)container).addView(mList.get(position), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mJazzy.setObjectForPosition(mList.get(position), position);
        return mList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(mJazzy.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }
}
