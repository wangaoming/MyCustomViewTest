package com.example.myviewgrouptest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalView extends ViewGroup {
//    实现页面的左右滑动 滑到另一个页面
private  int lastX;
private  int lastY;
//相当于子元素
private  int  currentIndex = 0;
private  int childWidth =0;
private Scroller scroller;
//增加速度检测
private VelocityTracker tracker;
private  int lastInterceptX=0;
private  int lastInterCeptY = 0;



    public HorizontalView(Context context) {
        super ( context );
        init();
    }

    private void init() {
        scroller = new Scroller ( getContext () );
        tracker = VelocityTracker.obtain ();
    }
//want to do  intercept 的拦截逻辑

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
//         获取动作 并且干啥指定
        switch (ev.getAction ()){
            case MotionEvent.ACTION_DOWN:
//      如果动画还没有执行完成，则打断
               if(!scroller.isFinished ()){
    scroller.abortAnimation ();
}
                break;
            case MotionEvent.ACTION_MOVE:
                int  deltaX = x-lastInterceptX;
                int deltaY = y-lastInterCeptY;
//                水平方向距离  move中返回true一次，后续的move与up不会收到任何请求
                if(Math.abs (  deltaX)-Math.abs ( deltaY )>0){
                    intercept = true;
                    Log.i ( "wangshu","intercept = true");
                }

                else {
                    Log.i ( "wangshu","intercept = false" );
            }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
//        因为DOWN返回false，所onTouchEvent无法获取Down事件，这里主要负责设置lastX&Y
        lastX = x;
        lastY = y;
        return intercept;
    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {
        tracker.addMovement ( event );

        int x = (int) event.getX ();
        int y = (int) event.getY ();
        switch (event.getAction ()){
            case MotionEvent.ACTION_DOWN:
                if(!scroller.isFinished ()){
                    scroller.abortAnimation ();
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                跟着手指动动
                int deltaX = x-lastX;
                scrollBy ( -deltaX,0 );
                int delatY = y-lastY;
                break;
//               释放手指以后开始自动滑动到目标位置
            case MotionEvent.ACTION_UP:
//                相当于当前View滑动的距离，正为向左，负为向右
                int distance = getScrollX () - currentIndex *childWidth;

//必须滑动的距离大于一半的宽度，否则不会滑动到其他页面
                if (Math.abs ( distance )>childWidth/2){
                    if(distance>0){
                        currentIndex ++;
                    }else{
                        currentIndex --;
                    }
                }
                else
                {
                    tracker.computeCurrentVelocity ( 1000 );
                    float xV = tracker.getXVelocity ();
                    if(Math.abs ( xV )>50){
                        currentIndex --;

                    }else{
                        currentIndex++;
                    }
                }
                currentIndex = currentIndex < 0 ? 0 : currentIndex > getChildCount() - 1 ?
                        getChildCount() - 1 : currentIndex;
                smoothScrollTo(currentIndex * childWidth, 0);
                tracker.clear();
                break;
                default:
                    break;

        }
        lastY = y;
        lastX = x;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //测量所有子元素
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //处理wrap_content的情况
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(childWidth * getChildCount(), childHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount(), heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
    public void smoothScrollTo(int destX, int destY) {
        scroller.startScroll(getScrollX(), getScrollY(), destX - getScrollX(),
                destY - getScrollY(), 1000);
        invalidate();
    }

    public HorizontalView(Context context, AttributeSet attrs) {
        super ( context, attrs );
    }

    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super ( context, attrs, defStyleAttr );
    }

    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super ( context, attrs, defStyleAttr, defStyleRes );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0; //左边的距离
        View child;
        //遍历布局子元素
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                childWidth = width; //赋值给子元素宽度变量
                child.layout(left, 0, left + width, child.getMeasuredHeight());
                left += width;
            }
        }
    }

}
