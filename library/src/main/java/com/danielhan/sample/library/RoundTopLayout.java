package com.danielhan.sample.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by hankun on 2016/9/23.
 */

public class RoundTopLayout extends RelativeLayout {

    private float gapSize;//弧顶和底线间的高度
    private float centerWidth;// 所在矩形宽
    private float lineSize;//线宽
    private int lineColor;//线颜色
    private int backColor;//填充颜色

    private Paint paint;//线画笔
    private Paint innerPaint;//填充画笔
    private int height;//控件高
    private int width;//控件宽

    private RectF rectF;//凸起边缘容器
    private RectF innerRectF;//凸起填充容器
    private RectF backRectF;//横线下方填充容器

    private int startAngle = 180;//弧线的起始角度
    private int sweepAngle = 180;//弧线烧过角度

    public RoundTopLayout(Context context) {
        super(context);
        init(context, null, R.attr.roundTopLayoutStyle);
    }

    public RoundTopLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public RoundTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.roundTopLayoutStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        //Load defaults from resources
        final Resources res = getResources();
        final float defaultGapSize = res.getDimension(R.dimen.default_rtl_gapsize);
        final float defaultCenterWidth = res.getDimension(R.dimen.default_rtl_centerwidth);
        final float defaultLineSize = res.getDimension(R.dimen.default_rtl_linesize);
        final int defaultLineColor = res.getColor(R.color.default_rtl_linecolor);
        final int defaultBackColor = res.getColor(R.color.default_rtl_backcolor);

        //Retrieve styles attributes
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundTopLayout, defStyle, 0);
        gapSize = a.getDimension(R.styleable.RoundTopLayout_gapsize, defaultGapSize);
        centerWidth = a.getDimension(R.styleable.RoundTopLayout_centerwidth, defaultCenterWidth);
        lineSize = a.getDimension(R.styleable.RoundTopLayout_linesize, defaultLineSize);
        lineColor = a.getColor(R.styleable.RoundTopLayout_linecolor, defaultLineColor);
        backColor = a.getColor(R.styleable.RoundTopLayout_backcolor, defaultBackColor);
        a.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(backColor);
        innerPaint.setStrokeWidth(lineSize * 2);
        innerPaint.setDither(true);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();
        innerRectF = new RectF();
        backRectF = new RectF();

        setBackgroundColor(getResources().getColor(android.R.color.transparent));//不设置背景会使画的东西消失
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画从左端开始到弧左端的直线
        canvas.drawLine(0, gapSize + lineSize, width, gapSize + lineSize, paint);
        //画填充弧线，可以避免填充覆盖弧线
        canvas.drawArc(innerRectF, startAngle, sweepAngle, false, innerPaint);
        //画弧线的时候，如果画笔的宽度比较低，那么看起来直线和弧线的宽度会不一样，需要设置画笔画完弧线后重置再继续画
        paint.setStrokeWidth(lineSize /2);
        //画外层弧线
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
        paint.setStrokeWidth(lineSize);//重置
        //画填充，并覆盖弧线及其填充
        canvas.drawRect(backRectF, innerPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        calc();
    }

    private void calc() {
        rectF.set(((width - centerWidth) / 2) - lineSize, lineSize, ((width + centerWidth) / 2) + lineSize, getMeasuredHeight());
        innerRectF.set(((width - centerWidth) / 2), lineSize * 2, ((width + centerWidth) / 2), getMeasuredHeight());
        backRectF.set(0, (float) (gapSize + lineSize * 2), width, getMeasuredHeight());
    }

    public float getGapSize() {
        return gapSize;
    }

    public void setGapSize(float gapSize) {
        this.gapSize = gapSize;
        calc();
        invalidate();
    }

    public float getCenterWidth() {
        return centerWidth;
    }

    public void setCenterWidth(float centerWidth) {
        this.centerWidth = centerWidth;
        calc();
        invalidate();
    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
        paint.setStrokeWidth(lineSize);
        innerPaint.setStrokeWidth(lineSize * 2);
        calc();
        invalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        paint.setColor(lineColor);
        invalidate();
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        innerPaint.setColor(backColor);
        invalidate();
    }
}
