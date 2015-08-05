package dream.app.com.dreammusic.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.tool.logger.Logger;

import java.util.List;

import dream.app.com.dreammusic.R;

/**
 * Created by JcMan on 2015/7/30.
 */
public class MyViewPagerIndicator extends LinearLayout {

    //标题正常时候的颜色
    private static final int COLOR_TEXT_NORMAL = Color.parseColor("#8B8E91");
    //标题被选中时候的颜色
    private static final int COLOR_TEXT_SELECT = Color.parseColor("#2E94FD");
    //指示器的颜色
    private static final int COLOR_INDICATOR = Color.parseColor("#2E94FD");
    //默认可见的标题的数量
    private static final int DEFAULT_VISIBLE_TAB_COUNT = 4;
    private int mTextNormalColor = COLOR_TEXT_NORMAL;
    private int mTextSelectColor = COLOR_TEXT_SELECT;
    private int mIndicatorColor = COLOR_INDICATOR;
    private int mVisibleTabCount = DEFAULT_VISIBLE_TAB_COUNT;
    //指示器的上顶点，左顶点，宽度，高度
    private int mTop,mLeft,mWidth,mHeight;
    //标题的内容
    private List<String> mTitles;
    //与之绑定的ViewPager
    private ViewPager mViewPager;
    //绘制指示器的画笔
    private Paint mPaint;

    public MyViewPagerIndicator(Context context) {
        this(context, null);
    }
    public MyViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPagerIndicator);
        mTextNormalColor = a.getColor(R.styleable.MyViewPagerIndicator_title_normal_color,COLOR_TEXT_NORMAL);
        mTextSelectColor = a.getColor(R.styleable.MyViewPagerIndicator_title_select_color,COLOR_TEXT_SELECT);
        mIndicatorColor = a.getColor(R.styleable.MyViewPagerIndicator_indicator_color,COLOR_INDICATOR);
        mHeight = (int)a.getDimension(R.styleable.MyViewPagerIndicator_indicator_height,2);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

    }

    //绘制指示器
    @Override
    protected void dispatchDraw(Canvas canvas){
        Rect rect = new Rect(mLeft,mTop-mHeight,mLeft+mWidth,mTop);
        canvas.drawRect(rect, mPaint);
        super.dispatchDraw(canvas);
    }

    //初始化指示器
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取指示器的宽度
        mWidth = (int)(w/mVisibleTabCount);
        //获取指示器的顶点
        mTop = getMeasuredHeight();
    }
    //设置可见的标题的数量
    public void setVisibleTabCount(int count){
        mVisibleTabCount = count;
    }
    //设置标题的内容
    public void setTitles(List<String> titles){
        //如果标题不为空
        if(titles!=null&&titles.size()>0){
            mTitles = titles;
            for (String title:mTitles){
                addView(createTitleView(title));
            }
            setTitleClickEvent();
        }
    }
    //设置标题的点击事件
    private void setTitleClickEvent(){
        int count = getChildCount();
        for(int i=0;i<count;i++){
            final int j = i;
            View v = getChildAt(i);
            v.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
    //生成标题的TextView
    private View createTitleView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTabCount ;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(mTextNormalColor);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(lp);
        return tv;

    }
    //得到屏幕的宽度
    public int getScreenWidth(){
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //得到屏幕的高度
    public int getScreenHeight(){
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //对外的ViewPager的回调接口
    public interface PagerChangeListener{
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int state);
    }
    //设置对外的ViewPager的回调接口
    private PagerChangeListener onPagerChangeListener;

    public void setViewPager(ViewPager viewPager,int pos){
        mViewPager = viewPager;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //LinearLayout滚动
                scroll(position, positionOffset);
            }
            @Override
            public void onPageSelected(int position){
                //重置标题颜色
                retTitleViewColor();
                //设置标题被选中弄的颜色
                setTitleSelectColor(position);
                //回调
                if (onPagerChangeListener != null) {
                    onPagerChangeListener.onPageSelected(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // 回调
                if (onPagerChangeListener != null){
                    onPagerChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        // 设置标题被选中的颜色
        setTitleSelectColor(pos);
    }
    //Linearlayout滚动,指示器滚动
    private void scroll(int position, float positionOffset) {
        //当标题移动到最后一个的时候Linearlayout开始滚动
        if(positionOffset>0&&position>=(mVisibleTabCount-1)&&getChildCount()>mVisibleTabCount){
            if(mVisibleTabCount!=1){
                this.scrollTo((position - (mVisibleTabCount - 1)) * mWidth
                        + (int) (mWidth * positionOffset), 0);
            }else{
                this.scrollTo(
                        position * mWidth + (int) (mWidth* positionOffset), 0);
            }
        }
        mLeft = (int) ((position + positionOffset) * mWidth);
        invalidate();
    }

    //设置被选中的标题的颜色
    private void setTitleSelectColor(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView){
            ((TextView) view).setTextColor(COLOR_TEXT_SELECT);
        }
    }
    //重置标题的颜色
    private void retTitleViewColor() {
        for (int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if (view instanceof TextView){
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }
}
