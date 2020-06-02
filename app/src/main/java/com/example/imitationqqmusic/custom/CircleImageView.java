package com.example.imitationqqmusic.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {

    private float scale;
    private Paint paint;
    private float radius;
    private BitmapShader bitmapShader;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = getBitmap(getDrawable());
        if (bitmap == null){
            super.onDraw(canvas);
        }else {
            radius = Math.min(getHeight(), getWidth()) / 2.0f;
            System.out.println("==============================bitmap.getWidth(): " + bitmap.getWidth());
            System.out.println("==============================bitmap.getHeight(): " + bitmap.getHeight());
            System.out.println("==============================getHeight(): " + getHeight());
            System.out.println("==============================getWidth(): " + getWidth());
            scale = (radius * 2.0f) / Math.min(bitmap.getWidth(), bitmap.getHeight());
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint = new Paint();
            Matrix matrix = new Matrix();
            matrix.setScale(radius, radius);
            bitmapShader.setLocalMatrix(matrix);
            paint.setShader(bitmapShader);
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    private Bitmap getBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }else if (drawable == null){
            return null;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }
}
