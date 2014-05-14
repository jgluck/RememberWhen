package com.example.rememberwhen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kentwills on 5/14/14.
 */
public class ImageHandler {

    public static Bitmap loadPic(File f){
        if(f.exists()){

            BitmapFactory.Options options = new BitmapFactory.Options();
    	    options.inJustDecodeBounds = true;
    	    int imageHeight = options.outHeight;
    	    int imageWidth = options.outWidth;
    	    String imageType = options.outMimeType;
    	    if(imageWidth > imageHeight) {
    	        options.inSampleSize = ImageHandler.calculateInSampleSize(options,512,256);//if landscape
    	    } else{
    	        options.inSampleSize = ImageHandler.calculateInSampleSize(options,256,512);//if portrait
    	    }
    	    options.inJustDecodeBounds = false;
    	    return BitmapFactory.decodeFile(f.getAbsolutePath(),options);
        }
        return null;
    }

    public static Bitmap LoadPicFromURL(String url){
        try {
            Bitmap x;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            if(imageWidth > imageHeight) {
                options.inSampleSize = ImageHandler.calculateInSampleSize(options,512,256);//if landscape
            } else{
                options.inSampleSize = ImageHandler.calculateInSampleSize(options,256,512);//if portrait
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input,null,options);
        }
        catch (Exception e){
            Log.w("flickr", e);
        }

        return null;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


}
