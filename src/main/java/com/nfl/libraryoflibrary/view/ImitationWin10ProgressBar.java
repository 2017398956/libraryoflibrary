package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 模仿win10进度条
 * @author fuli.niu
 * 参考blog：http://blog.csdn.net/zhangml0522
 * PathMeasure.getSegment（）只在KITKAT以上版本有效，而对于以下版本一定要关闭硬件加速才有效
 */
public class ImitationWin10ProgressBar extends View {

	private Paint mPaint;// 绘制ProgressBar的画笔
	private float startPosition = -90f ;// 动画起点的位置
	private int paintSizeDp = 15 ;// 以dp设置画笔的粗细
	private int paintSize ;// 定义ProgressBar画笔的粗细
	private Path mPath;// ProgressBar的整个路径
	private Path dst = new Path();// ProgrerssBar当前的路径
	private PathMeasure mPathMeasure ; // 用于测量ProgressBar的路径
	private int mWidth, mHeight ; // 画布的宽高
	private ValueAnimator valueAnimator ; // ProgressBar的动画效果
	private float time ;// 动画的进度
	private float d ;// 画笔粗细为0时所绘的最远距离（由于本例是正方形，所以取最小 ,即：圆环直径）
	private float r ;// 画笔粗细为0时圆环的半径
	private float paintOffset ;// 画笔偏移量（画笔粗细可以造成绘制效果的偏差）
	private float pathLength ;// ProgressBar路径的总长度

	public ImitationWin10ProgressBar(Context context) {
		this(context , null);
	}

	public ImitationWin10ProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs , 0);
	}

	public ImitationWin10ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context , attrs , defStyleAttr) ;
		this.setLayerType(LAYER_TYPE_SOFTWARE , null);// 关闭硬件加速 , 解决低版本无动画效果的问题
		paintSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paintSizeDp , context.getResources().getDisplayMetrics()) ;
	}

	private void init() {
		// 初始化ProgressBar的画笔
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(paintSize);// 设置画笔尺寸的大小
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔为圆笔
		mPaint.setAntiAlias(true);// 抗锯齿

		// 初始化ProgressBar的路径
		mPath = new Path();
		d = mWidth > mHeight ? mHeight : mWidth ;// 画笔粗细为0时所绘的最远距离（由于本例是正方形，所以取最小）
		r = d / 2 ;
		paintOffset = paintSize / 2 ;// 画笔偏移量（画笔粗细可以造成绘制效果的偏差）
		// 小知识：Rect是使用int类型作为数值，RectF是使用float类型作为数值
		RectF rectF = new RectF(paintOffset , paintOffset , d - paintOffset, d - paintOffset);
		mPath.addArc(rectF, startPosition ,359.9f);// 默认从上方开始;这里角度不能使用360f，会导致起点从0度开始而不是-90度开始
// 		mPath.addCircle( r , r , r - paintOffset , Path.Direction.CW);// 画圆环
//		mPath.addOval(rectF , Path.Direction.CW);// 画椭圆
//		mPath.addRect(rectF , Path.Direction.CW);// 画矩形
//		mPath.addRoundRect(rectF , d , d  , Path.Direction.CW);// 画圆角
		mPathMeasure = new PathMeasure( mPath , true);
		pathLength = mPathMeasure.getLength() ;
		// 初始化动画效果
		valueAnimator = ValueAnimator.ofFloat(0f,1f).setDuration(3000);
		valueAnimator.setRepeatCount(-1); // 无限重复
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				time = (float) animation.getAnimatedValue() ;
				invalidate();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth() ;
		mHeight = getMeasuredHeight() ;
		// setMeasuredDimension(mWidth , mHeight);
		init();
		valueAnimator.start();// 启动动画效果
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 解决起点闪烁，这样写可避免起点改变造成闪烁的问题
		if(time >= 0.95) {
			dst.reset();
			setDstPath(0);
			canvas.drawPath(dst ,mPaint);
		}
		dst.reset(); //将路径重置
		int num = (int) (time / 0.05);
		float y , x ;
		switch(num){
			default:
			case 3:
				x = time - 0.15f * (1- time);
				y = - pathLength * x * x + 2 * pathLength * x;
				setDstPath(y) ;
			case 2:
				x = time -0.10f*(1- time);
				y = - pathLength * x * x + 2 * pathLength * x;
				setDstPath(y) ;
			case 1:
				x = time -0.05f*(1- time);
				y = - pathLength * x * x + 2 * pathLength * x;
				setDstPath(y) ;
			case 0:
				x = time;
				y = - pathLength * x * x + 2 * pathLength * x;
				setDstPath(y) ;
				break;
		}
		canvas.drawPath(dst ,mPaint);
	}

	private void setDstPath(float positionOnPath){
		mPathMeasure.getSegment(positionOnPath ,positionOnPath + 1,dst,true);
	}
}
