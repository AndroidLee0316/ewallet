package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class CircleImageView extends AppCompatImageView {
    private Path path = new Path ();
    private int margin=8;
    private Paint paint;
    private int x;
    private int y;
    private float radius;

    public CircleImageView(Context context) {
        super (context);
        init (context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        init (context);


    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context);

    }

    void init(Context context){
        paint=new Paint (Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth (margin);
        paint.setStyle (Paint.Style.STROKE);
        paint.setColor (Color.parseColor ("#99ffffff"));
    }

    void initData() {
        if (getWidth ()>0) {
            x = getWidth () / 2;
            y = getHeight () / 2;
            radius = x >= y ? y : x;
            radius = radius - margin;
            path.reset ();
            path.moveTo (x, y);
            path.addCircle (x, y, radius, Path.Direction.CW);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);
//        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle (x,y,radius+margin/2,paint);
//        canvas.save ();
//        canvas.clipPath (path);
        super.onDraw (canvas);
//        canvas.restore ();
    }
}
