package com.example.rememberwhen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.File;

/**
 * Created by kentwills on 5/14/14.
 */
public class ImageHandler {

    public static Bitmap loadPic(File f){
        if(f.exists()){

            BitmapFactory.Options options = new BitmapFactory.Options();
    	    options.inJustDecodeBounds = true;
    	    BitmapFactory.decodeFile(f.getAbsolutePath(), options);
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
