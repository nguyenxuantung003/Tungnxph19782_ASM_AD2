package com.example.nguyenxuantung_ph19782;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class CircularProgressDrawable extends Drawable {

    private final Paint paint;
    private int progress;
    private final int maxProgress;

    public CircularProgressDrawable(int maxProgress) {
        this.maxProgress = maxProgress;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(200); // Độ dày của đường vẽ
        paint.setColor(0xFFFF0000); // Màu sắc của progress
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float radius = Math.min(bounds.width(), bounds.height()) / 2f - paint.getStrokeWidth() / 2f;
        float cx = bounds.centerX();
        float cy = bounds.centerY();

        float sweepAngle = (progress / (float) maxProgress) * 360;
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, -90, sweepAngle, false, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidateSelf();
    }
}