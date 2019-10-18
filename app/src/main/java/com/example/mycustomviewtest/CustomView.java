package com.example.mycustomviewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {
    private  int lastX;
    private  int  lastY;
    public CustomView(Context context) {
        super ( context );
    }

    public CustomView(Context context, AttributeSet attrs) {
        super ( context, attrs );
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super ( context, attrs, defStyleAttr );
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super ( context, attrs, defStyleAttr, defStyleRes );
    }
//    View的滑动


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//       1.获取到手指处的横坐标和横坐标
        int x = (int) event.getX ();
        int y= (int) event.getY ();


        switch (event.getAction ()){
            case MotionEvent.ACTION_DOWN:
                lastX  =x;
                lastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
//                计算移动距离
                int offsetX= x-lastX;
                int offsetY = y-lastY;
//               调用layout方法来重新放置它的位置
                layout ( getLeft ()+offsetX,getTop ()+offsetY,
                getRight ()+offsetX,getBottom ()+offsetY);
                break;

        }
        return true;
    }
}
