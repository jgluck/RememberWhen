package com.example.rememberwhen;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by jgluck on 5/13/14.
 */
public class remember_camera_layout extends FrameLayout {

    private ImageView myImage;

    public remember_camera_layout(Context context) {
        this(context, null, 0);
    }

    public remember_camera_layout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public remember_camera_layout(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        LayoutInflater.from(context).inflate(R.layout.remember_camera_layout, this);

        myImage = (ImageView) findViewById(R.id.photoResult);


    }

    public void do_image_alpha(){
        myImage.setImageAlpha(5);
    }

    public void add_image(Bitmap x){

    }
}
