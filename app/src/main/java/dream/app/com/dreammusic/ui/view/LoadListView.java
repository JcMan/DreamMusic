package dream.app.com.dreammusic.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/17.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener{

    private View footer;
    int totalCount;
    int lastVisibleItem ;
    boolean isLoading;

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout,null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(totalCount == lastVisibleItem&&scrollState==SCROLL_STATE_IDLE)
        {
            if(!isLoading){
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
                listener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem+visibleItemCount;
        this.totalCount = totalItemCount;
    }

    public void setLoadListener(ILoadListener listener){
        this.listener = listener;
    }

    public void loadComplete(){
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
    }
    ILoadListener listener;
    public interface ILoadListener{
        void onLoad();
    }
}
