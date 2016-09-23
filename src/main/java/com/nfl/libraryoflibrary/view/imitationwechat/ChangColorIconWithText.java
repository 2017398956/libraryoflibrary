package com.nfl.libraryoflibrary.view.imitationwechat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.nfl.libraryoflibrary.R;

public class ChangColorIconWithText extends View {

	private int mColor = 0xFF45C01A; // 默认颜色为绿色
	private Bitmap mIconBitmap;// 用于存储图标
	private String mText = "test";// 用于存储图标下的文字内容
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()); // 图标下文字的实际大小（默认值12sp）
	
	private float mAlpha ; // 透明度

	/*
	 * 用于绘制icon的背景
	 */
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	private Rect mIconRect;
	/*
	 * 用于绘制text
	 */
	private Rect mTextBound;// 文字所占的区域
	private Paint mTextPaint;// 文字画笔

	public ChangColorIconWithText(Context context) {
		this(context, null);
	}

	public ChangColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 获得自定义属性的值
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ChangColorIconWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		/**
		 * 加载自定义样式
		 * @param attrs : android系统的属性值集合
		 * @param R.styleable.ChangColorIconWithText ： 自定义的属性值集合
		 * @return 自定义属性集合
		 */
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ChangColorIconWithText);
		/*
		 * 获得自定义样式中的所有参数的个数，
		 * 这里为n = 4 ；
		 */
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			if( attr == R.styleable.ChangColorIconWithText_color){
				mColor = a.getColor(attr, mColor);
			}else if( attr == R.styleable.ChangColorIconWithText_image){
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
			}else if( attr == R.styleable.ChangColorIconWithText_text){
				mText = a.getString(attr);
			}else if( attr == R.styleable.ChangColorIconWithText_text_size){
				mTextSize = (int) a.getDimension(attr, TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
								getResources().getDisplayMetrics()));
			}
		}
		/*
		 * TypedArray使用后需要重新利用，以便于其他布局的使用
		 */
		a.recycle();
		/*
		 * 绘制文字工具的初始化
		 */
		mTextBound = new Rect();// 文字所占的区域
		mTextPaint = new Paint();// 文字画笔
		/*
		 * 设置画笔的属性
		 */
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(mColor);
		/*
		 * 得到文字所占的区域大小mTextBound
		 */
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/*
		 *由于图片为正方形所以iconWidth = iconHeight 
		 */
		int iconWidth = Math.min(getMeasuredHeight() - getPaddingBottom()
				- getPaddingTop() - mTextBound.height(), getMeasuredWidth()
				- getPaddingLeft() - getPaddingRight());
		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = (getMeasuredHeight() - mTextBound.height()) / 2 - iconWidth
				/ 2;
		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		int alpha = (int) Math.ceil(255 * mAlpha);
		// 在内存中完成图标的绘制
		setupTargetBitmap(alpha);
		// 绘制文本
		drawSourceText(canvas, alpha);
		
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	private void drawTargetText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2 ;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x , y , mTextPaint);
	}

	private void drawSourceText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255 - alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2 ;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x , y , mTextPaint);
	}

	/**
	 * 绘制用于显示的图标
	 * @param alpha
	 */
	private void setupTargetBitmap(int alpha) {
		// TODO Auto-generated method stub
		/*
		 * 绘制与icon等大小的纯色（mColor）背景图,而且该背景色为动态透明度
		 */
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);// 默认为0，全透明
		mCanvas.drawRect(mIconRect, mPaint);
		/*
		 * 以DST_IN模式绘制背景图上的icon其透明度永远为255，即：不透明
		 */
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}
	
	public void setIconAlpha(float alpha){
		this.mAlpha = alpha ;
		invalidateView();
	}

	/**
	 * 重绘
	 */
	private void invalidateView() {
		// TODO Auto-generated method stub
		if(Looper.getMainLooper() == Looper.myLooper()){
			invalidate();
		}else{
			postInvalidate(); 
		}
	}

}
