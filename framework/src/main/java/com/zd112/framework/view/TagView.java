package com.zd112.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/11.
 * @Emal:18311271399@163.com
 */
public class TagView extends ViewGroup {
    private List<Line> mLines = new LinkedList<Line>();    // 用来记录布局中有多少个行
    private Line mCurrentLine;                                   //当前行
    private int horizontalSpace = 10;                              //控件之间的间隙
    private int verticalSpace = 10;                                   //行之间的间隙

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 清空记录,,,不清空就会掉坑了
        mLines.clear();
        mCurrentLine = null;
        //1,获取自身的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //2,获取行的宽度
        int maxWidth = width - getPaddingLeft() - getPaddingRight();

        //3,获取孩子
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            //测量孩子的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //将孩子添加到行中,把行添加到集合中
            if (mCurrentLine == null) {
                mCurrentLine = new Line(maxWidth, horizontalSpace);
                mCurrentLine.addView(child);
                mLines.add(mCurrentLine);
            } else {
                if (mCurrentLine.canAdd(child)) {
                    // 可以加入
                    mCurrentLine.addView(child);
                } else {
                    // 换行
                    mCurrentLine = new Line(maxWidth, horizontalSpace);
                    // 添加到list
                    mLines.add(mCurrentLine);
                    // 添加孩子
                    mCurrentLine.addView(child);
                }
            }
        }
        //获取自己的高度
        int measuredHeight = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < mLines.size(); i++) {
            measuredHeight += mLines.get(i).height;
        }
        measuredHeight = measuredHeight + (mLines.size() - 1) * verticalSpace;
        // 设置自己的宽度和高度
        setMeasuredDimension(width, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int marginTop = getPaddingTop();
        int marginLeft = getPaddingLeft();
        // 让line自己去摆放
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(marginLeft, marginTop);
            marginTop += verticalSpace + line.height;
        }
    }

    /**
     * 用来记录每行控件的摆放
     */
    private class Line {
        private List<View> mViews = new ArrayList<View>();
        private int maxWidth;                              //行的宽度
        private int space;                                  //行的水平间隙
        private int usedWidth;                             //行使用了的宽度
        public int height;                                  //行高

        public Line(int maxWidth, int horizontalSpace) {
            this.maxWidth = maxWidth;
            this.space = horizontalSpace;
        }

        /**
         * 添加子控件
         *
         * @param child
         */
        public void addView(View child) {
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (mViews.size() == 0) {
                // 如果没有控件的情况下
                if (childWidth > maxWidth) {
                    usedWidth = maxWidth;
                    height = childHeight;
                } else {
                    usedWidth = childWidth;
                    height = childHeight;
                }
            } else {
                //有控件
                usedWidth = usedWidth + space + childWidth;
                height = (childHeight > height) ? childHeight : height;
            }
            mViews.add(child);
        }

        /**
         * 判断是否可以添加子控件
         *
         * @param child
         * @return
         */
        public boolean canAdd(View child) {
            int width = child.getMeasuredWidth();
            // line中没有view时
            if (mViews.size() == 0) {
                // 只要没有，就可以加
                return true;
            }
            if (usedWidth + width + space > maxWidth) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * 摆放行
         *
         * @param marginLeft
         * @param marginTop
         */
        public void layout(int marginLeft, int marginTop) {
            int extraWidth = maxWidth - usedWidth;
            int avgWidth = (int) (extraWidth * 1f / mViews.size() + 0.5f);
            //计算控件的上下左右的位置
            for (int i = 0; i < mViews.size(); i++) {

                View view = mViews.get(i);

                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();

                if (avgWidth > 0) {
                    // 重新的去期望孩子的宽高
                    int specWidth = MeasureSpec.makeMeasureSpec(viewWidth + avgWidth, MeasureSpec.EXACTLY);
                    int specHeight = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
                    view.measure(specWidth, specHeight);
                    // 重新获取宽度和高度
                    viewWidth = view.getMeasuredWidth();
                    viewHeight = view.getMeasuredHeight();
                }

                int extraTop = (int) ((height - viewHeight) / 2f + 0.5);

                int left = marginLeft;
                int top = marginTop + extraTop;
                int right = left + viewWidth;
                int bottom = top + viewHeight;
                //摆放每一个孩子的位置
                view.layout(left, top, right, bottom);
                marginLeft += viewWidth + space;
            }
        }
    }
}
