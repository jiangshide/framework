package com.zd112.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/13.
 * @Emal:18311271399@163.com
 */
public class CusImageView extends android.support.v7.widget.AppCompatImageView {

    private final int DEFAULT_CORNER = 20;
    private final float DEFAULT_SX = -1;
    private final float DEFAULT_SY = 1;
    private final float DEFAULT_SCALE_SX = 0.5f;
    private final float DEFAULT_SCALE_SY = 0.5f;

    public CusImageView(Context context) {
        super(context);
    }

    public CusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFlipping() {
        setFlipping(DEFAULT_SX, DEFAULT_SY);
    }

    public void setFlipping(float sx, float sy) {
        setFlipping(null, sx, sy);
    }

    public void setFlipping(Bitmap bitmap, float sx, float sy) {
        if (bitmap == null) {
            setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(this.getDrawingCache());
            setDrawingCacheEnabled(false);
        }
        if (bitmap == null) return;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
    }

    public void setScale() {
        setScale(DEFAULT_SCALE_SX, DEFAULT_SCALE_SY);
    }

    public void setScale(float sx, float sy) {
        setScale(null, sx, sy);
    }

    public void setScale(Bitmap bitmap, float sx, float sy) {
        if (bitmap == null) {
            setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(getDrawingCache());
            setDrawingCacheEnabled(false);
        }
        if (bitmap == null) return;
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
    }

    public void setReflectionOrigin() {
        setReflectionOrigin(DEFAULT_SY, DEFAULT_SX);
    }

    public void setReflectionOrigin(float sx, float sy) {
        setReflectionOrigin(null, sx, sy);
    }

    public void setReflectionOrigin(Bitmap bitmap, float sx, float sy) {
        if (bitmap == null) {
            setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(getDrawingCache());
            setDrawingCacheEnabled(false);
        }
        if (bitmap == null) return;
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(sx, sy);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x70ffffff, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        setImageBitmap(bitmapWithReflection);
    }

    public void setCorner() {
        setCorner(DEFAULT_CORNER);
    }

    public void setCorner(float corner) {
        setCorner(null, corner);
    }

    public void setCorner(Bitmap bitmap, float corner) {
        if (bitmap == null) {
            setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(getDrawingCache());
            setDrawingCacheEnabled(false);
        }
        if (bitmap == null) return;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        setImageBitmap(output);
    }
}
