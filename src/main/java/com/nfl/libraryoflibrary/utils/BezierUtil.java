package com.nfl.libraryoflibrary.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2017/10/9.
 * 贝塞尔曲线工具
 */

public class BezierUtil {

    private final int LINEWIDTH = 5;
    private final int POINTWIDTH = 10;
    private Paint mPaint;
    private Path mPath;
    private List<Point> mPoints = new ArrayList<>();// 即将要穿越的点集合
    private List<Point> mMidPoints = new ArrayList<>();// 中点集合
    private List<Point> mMidMidPoints = new ArrayList<>();// 中点的中点集合
    private List<Point> mControlPoints = new ArrayList<>();// 移动后的点集合(控制点)
    private Canvas canvas;

    private BezierUtil() {
    }

    /**
     * @param points 要经过的各个点
     * @param canvas 由 View 传递过来的 canvas
     */
    public BezierUtil(List<Point> points, Canvas canvas) {
        init();
        this.canvas = canvas;
        if (null != points) {
            this.mPoints = points;
        }
        calMidPoints(this.mPoints);
        calMidMidPoints(this.mMidPoints);
        calControlPoints(this.mPoints, this.mMidPoints, this.mMidMidPoints);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mPath = new Path();
    }

    /**
     * 初始化中点集合
     */
    private void calMidPoints(List<Point> points) {
        mMidPoints.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            mMidPoints.add(new Point((points.get(i).x + points.get(i + 1).x) / 2, (points.get(i).y + points.get(i + 1).y) / 2));
        }
    }

    /**
     * 初始化中点的中点集合
     */
    private void calMidMidPoints(List<Point> midPoints) {
        mMidMidPoints.clear();
        for (int i = 0; i < midPoints.size() - 1; i++) {
            mMidMidPoints.add(new Point((midPoints.get(i).x + midPoints.get(i + 1).x) / 2, (midPoints.get(i).y + midPoints.get(i + 1).y) / 2));
        }
    }

    /**
     * 初始化控制点集合
     */
    private void calControlPoints(List<Point> points, List<Point> midPoints, List<Point> midMidPoints) {
        mControlPoints.clear();
        for (int i = 1; i < points.size() - 1; i++) {
            mControlPoints.add(new Point(
                    points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i - 1).x,
                    points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i - 1).y
            ));// before
            mControlPoints.add(new Point(
                    points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i).x,
                    points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i).y
            ));// after
        }
    }

    public void draw() {
        drawPoints();// 画原始点
        drawCrossPointsBrokenLine();// 画穿越原始点的折线
        drawMidPoints();// 画中间点
        drawMidMidPoints();// 画中间点的中间点
        drawControlPoints();// 画控制点
        drawBezier();// 画贝塞尔曲线
    }

    /**
     * 画原始点
     */
    private void drawPoints() {
        mPaint.setStrokeWidth(POINTWIDTH);
        for (int i = 0; i < mPoints.size(); i++) {
            this.canvas.drawPoint(mPoints.get(i).x, mPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画穿越原始点的折线
     */
    private void drawCrossPointsBrokenLine() {
        if (mPoints.size() == 0) {
            return;
        }
        mPaint.setStrokeWidth(LINEWIDTH);
        mPaint.setColor(Color.RED);
        mPath.reset();// 重置路径
        mPath.moveTo(mPoints.get(0).x, mPoints.get(0).y);// 画穿越原始点的折线
        for (int i = 1; i < mPoints.size(); i++) {
            mPath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
        }
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 画中间点
     */
    private void drawMidPoints() {
        mPaint.setStrokeWidth(POINTWIDTH);
        mPaint.setColor(Color.BLUE);
        for (int i = 0; i < mMidPoints.size(); i++) {
            canvas.drawPoint(mMidPoints.get(i).x, mMidPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画中间点的中间点
     */
    private void drawMidMidPoints() {
        mPaint.setColor(Color.YELLOW);
        for (int i = 0; i < mMidMidPoints.size(); i++) {
            canvas.drawPoint(mMidMidPoints.get(i).x, mMidMidPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画控制点
     */
    private void drawControlPoints() {
        mPaint.setColor(Color.GRAY);
        // 画控制点
        for (int i = 0; i < mControlPoints.size(); i++) {
            canvas.drawPoint(mControlPoints.get(i).x, mControlPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画贝塞尔曲线
     */
    private void drawBezier() {
        mPaint.setStrokeWidth(LINEWIDTH);
        mPaint.setColor(Color.BLACK);
        mPath.reset();// 重置路径
        if (mPoints.size() < 2) {
            // 少于 2 个点不需要绘制
            return;
        }
        int remainder = (mPoints.size() - 2) % 2;// 当余数为 0 时，最后一条不为二阶贝塞尔
        // 第一条为二阶贝塞尔绘制
        mPath.moveTo(mPoints.get(0).x, mPoints.get(0).y);// 起点
        mPath.quadTo(mControlPoints.get(0).x, mControlPoints.get(0).y,// 控制点
                mPoints.get(1).x, mPoints.get(1).y);
        for (int i = 1; i < mPoints.size() - remainder * 2; i++) {
            // 三阶贝塞尔
            mPath.cubicTo(mControlPoints.get(2 * i - 1).x, mControlPoints.get(2 * i - 1).y,// 控制点
                    mControlPoints.get(2 * i).x, mControlPoints.get(2 * i).y,// 控制点
                    mPoints.get(i + 1).x, mPoints.get(i + 1).y);// 终点
        }
        if (remainder == 1) {
            // 最后一条为二阶贝塞尔
            mPath.moveTo(mPoints.get(mPoints.size() - 2).x, mPoints.get(mPoints.size() - 2).y);// 起点
            mPath.quadTo(mControlPoints.get(mControlPoints.size() - 1).x, mControlPoints.get(mControlPoints.size() - 1).y,
                    mPoints.get(mPoints.size() - 1).x, mPoints.get(mPoints.size() - 1).y);// 终点
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void setPoints(List<Point> points) {
        mPoints.clear();
        if (null != points) {
            mPoints = points;
        }
        calMidPoints(this.mPoints);
        calMidMidPoints(this.mMidPoints);
        calControlPoints(this.mPoints, this.mMidPoints, this.mMidMidPoints);
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path mPath) {
        this.mPath = mPath;
    }
}
