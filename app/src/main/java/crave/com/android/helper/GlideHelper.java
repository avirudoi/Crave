package crave.com.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Transformation;

import crave.com.android.R;


public enum GlideHelper {

    Instance;

    public void displayMainImage(String model, Context context, ImageView imgQueue) {
        Glide.with(context)
                .load(model)
                .placeholder(R.drawable.temp_image)
                .error(R.drawable.temp_image)
                .centerCrop()
                .into(imgQueue);
    }

    public void displayImageGallery(String model, Context context, ImageView imgQueue) {
        Glide.with(context)
                .load(model)
                .placeholder(R.drawable.temp_image)
                .error(R.drawable.temp_image)
                .centerCrop()
                .into(imgQueue);
    }

    public void displayImageFromCamera(byte[] model, Context context, ImageView imgQueue) {
        Glide.with(context)
                .load(model)
                .centerCrop()
                .into(imgQueue);
    }


    public void displayItemImage(byte[] model, Context context, ImageView imgQueue) {
        Glide.with(context)
                .load(model)
                .centerCrop()
                .into(imgQueue);
    }

    public void displayRoundProfileImage(String url, Context context, ImageView imgQueue) {
        Glide.with(context)
                .load(url)
                .transform(new CircleTransform(context))
                .into(imgQueue);

    }

    public static class CropSquareTransformation extends BitmapTransformation {
        public CropSquareTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }


    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public class CropSquareTransformationNew implements Transformation {

        private int mBorderSize = 10;
        private int mCornerRadius = 20;
        private int mColor = Color.BLACK;

        @Override
        public Bitmap transform(Bitmap source) {
            // TODO Auto-generated method stub
            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = mCornerRadius;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, rect, rect, paint);

            // draw border
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) mBorderSize);
            canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);
            //-------------------

            if (source != output) source.recycle();

            return output;
        }

        @Override
        public String key() {
            // TODO Auto-generated method stub
            return "grayscaleTransformation()";

        }
    }
}