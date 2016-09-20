package com.nfl.libraryoflibrary.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.ToastTool;

/**
 * 模仿win10进度条
 * @author fuli.niu
 * 参考blog：http://blog.csdn.net/zhangml0522
 */
/**
 * @author fuli.niu
 *
 */
public class ImitationWin10ProgressBar extends View {

	private Context context ;
	private Paint mPaint;// 绘制ProgressBar的画笔
	private int paintSize = 15 ;// 定义ProgressBar画笔的粗细
	private Path mPath;// ProgressBar的路径
	private PathMeasure mPathMeasure ; // 用于测量ProgressBar的路径
	private int mWidth, mHeight ; // 画布的宽高
	private ValueAnimator valueAnimator ; // ProgressBar的动画效果
	private float time ;// 动画的进度

	public ImitationWin10ProgressBar(Context context) {
		this(context , null);
		this.context = context ;
	}

	public ImitationWin10ProgressBar(Context context, AttributeSet attrs) {
		this(context, null , 0);
	}

	public ImitationWin10ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context , attrs , defStyleAttr) ;
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
		float d = mWidth > mHeight ? mHeight : mWidth ;// 画笔粗细为0时所绘的最远距离（由于本例是正方形，所以取最小）
		float r = d / 2 ;
		float paintOffset = paintSize / 2 ;// 画笔偏移量（画笔粗细可以造成绘制效果的偏差）
		// 小知识：Rect是使用int类型作为数值，RectF是使用float类型作为数值
		RectF rectF = new RectF(paintOffset , paintOffset , d - paintOffset, d - paintOffset);
		mPath.addCircle( r , r , r - paintOffset , Path.Direction.CW);// 画圆环
//		mPath.addArc(rectF,-90,360f);// 角度自己调整，可画圆弧
//		mPath.addOval(rectF , Path.Direction.CW);// 画椭圆
//		mPath.addRect(rectF , Path.Direction.CW);// 画矩形
//		mPath.addRoundRect(rectF , d , d  , Path.Direction.CW);// 画圆角
		mPathMeasure = new PathMeasure( mPath , true);

		// 初始化动画效果
		valueAnimator = ValueAnimator.ofFloat(0f,1f).setDuration(3000);
		valueAnimator.setRepeatCount(-1); // 无限重复
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				time = (float) animation.getAnimatedValue();
				invalidate();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth() ;
		ToastTool.showCustomShortToast("mw:" + getMeasuredWidth() + ",w:" + getWidth());
		mHeight = getMeasuredHeight() ;
		init();
		valueAnimator.start();// 启动动画效果
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制辅助线
		Paint assitPaint = new Paint() ;
		assitPaint.setStyle(Paint.Style.STROKE);
		assitPaint.setStrokeWidth(1);
		assitPaint.setColor(Color.WHITE);
		assitPaint.setStrokeCap(Paint.Cap.ROUND);
		assitPaint.setAntiAlias(true);
		canvas.drawRect(0 , 0 , getMeasuredWidth() , getMeasuredHeight() , assitPaint);

		// 绘制ProgressBar
		Path dst = new Path();// ProgrerssBar当前的路径
		// 每间隔0.05就画一个点，总共画4个点
//		int num = (int) (time / 0.05);
//		float s,y,x;
//		switch(num){
//			default:
//			case 3:
//				x = time - 0.15f;
//				s = mPathMeasure.getLength();
//				y = s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 2:
//				x = time - 0.10f;
//				s = mPathMeasure.getLength();
//				y = s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 1:
//				x = time - 0.05f;
//				s = mPathMeasure.getLength();
//				y = s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 0:
//				x = time ;
//				s = mPathMeasure.getLength();
//				y = s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//				break;
//		}
		mPathMeasure.getSegment(mPathMeasure.getLength() * time , mPathMeasure.getLength() * time + 1f,dst, true);
		canvas.drawPath(dst ,mPaint);


//		canvas.translate(mWidth/2,mHeight/2);
//		Path dst = new Path();
//		if(t>=0.95){
//			canvas.drawPoint(0,-150,mPaint);
//		}
//		int num = (int) (t/0.05);
//		float s,y,x;
//		switch(num){
//			default:
//			case 3:
//				x = t-0.15f*(1-t);
//				s = mPathMeasure.getLength();
//				y = -s*x*x+2*s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 2:
//				x = t-0.10f*(1-t);
//				s = mPathMeasure.getLength();
//				y = -s*x*x+2*s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 1:
//				x = t-0.05f*(1-t);
//				s = mPathMeasure.getLength();
//				y = -s*x*x+2*s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//			case 0:
//				x = t;
//				s = mPathMeasure.getLength();
//				y = -s*x*x+2*s*x;
//				mPathMeasure.getSegment(y,y+1,dst,true);
//				break;
//		}
//		canvas.drawPath(dst,mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
//		mWidth = w;
//		mHeight = h;
	}


}
