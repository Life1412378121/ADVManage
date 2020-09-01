package com.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;

public class ImageUtils {

	public static Bitmap drawableToBitmap(Context context, int drawable) {
		// TODO Auto-generated method stub
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				drawable);
		return bmp;
	}

	/**
	 * 图片倒影
	 * 
	 * @param context
	 * @param drawable
	 * @return
	 */
	public static Bitmap createReflectedImage(Bitmap bmp) {

		final int reflectionGap = 1;
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bmp, 0, height / 2, width,
				height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 5), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bmp, 0, 0, null);

		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bmp.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70FFFFFF,
				0x00FFFFFF, TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
}
