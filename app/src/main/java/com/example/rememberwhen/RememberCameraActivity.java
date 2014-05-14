package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.media.CameraManager;

import java.io.File;
import java.util.HashMap;

public class RememberCameraActivity extends Activity{


	private int TAKE_PICTURE_REQUEST = 101;
	SharedPreferences prefs;

	
	private void getPicNum(){
		String picKey = "com.example.rememberwhen.picrequest";
		this.TAKE_PICTURE_REQUEST = prefs.getInt(picKey, 101);
		prefs.edit().putInt(picKey, this.TAKE_PICTURE_REQUEST+1);
	}
	
	private void takePicture() {
		getPicNum();
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, TAKE_PICTURE_REQUEST);
	}
	
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
  		      "com.example.rememberwhen", Context.MODE_PRIVATE);
        setContentView(R.layout.remember_camera_layout);
        
        takePicture();

    }
    
    protected void loadPic(File f){
    	if(f.exists()){
    	    Bitmap myBitmap = ImageHandler.loadPic(f);
    	    ImageView myImage = (ImageView) findViewById(R.id.photoResult);
            TextView myText = (TextView) findViewById(R.id.photo_view_loading_text);
            myText.setVisibility(View.INVISIBLE);
//    	    Drawable d = Drawable.createFromPath(f.getAbsolutePath());
    	    myImage.setImageBitmap(myBitmap);
    	    //myImage.setImageBitmap(Bitmap.createScaledBitmap(myBitmap,2040, 2040, false));
    	    //myImage.setImageBitmap(myBitmap);
    	}
    }

    protected void loadPic(Bitmap b){

            Bitmap myBitmap = b;
            ImageView myImage = (ImageView) findViewById(R.id.photoResult);
            TextView myText = (TextView) findViewById(R.id.photo_view_loading_text);
            myText.setVisibility(View.INVISIBLE);
//    	    Drawable d = Drawable.createFromPath(f.getAbsolutePath());
            myImage.setImageBitmap(myBitmap);
            //myImage.setImageBitmap(Bitmap.createScaledBitmap(myBitmap,2040, 2040, false));
            //myImage.setImageBitmap(myBitmap);
        
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
	        String picturePath = data.getStringExtra(
	                CameraManager.EXTRA_PICTURE_FILE_PATH);
	        processPictureWhenReady(picturePath);
	    }

	    super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPictureWhenReady(final String picturePath) {
	    final File pictureFile = new File(picturePath);

	    if (pictureFile.exists()) {

            //Insert here
            //postData();
            HashMap<String, String> data = new HashMap<String, String>();
            //AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
            //asyncHttpPost.execute("https://remember-when1.appspot.com/");

            data.put("geo", "lat,lon");
            flickr f = new flickr(data);
            f.execute("");
	    	loadPic(pictureFile);
	        //finish();
	    } else {
	        // The file does not exist yet. Before starting the file observer, you
	        // can update your UI to let the user know that the application is
	        // waiting for the picture (for example, by displaying the thumbnail
	        // image and a progress indicator).

	        final File parentDirectory = pictureFile.getParentFile();
	        FileObserver observer = new FileObserver(parentDirectory.getPath(),
	                FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO) {
	            // Protect against additional pending events after CLOSE_WRITE
	            // or MOVED_TO is handled.
	            private boolean isFileWritten;

	            @Override
	            public void onEvent(int event, String path) {
	                if (!isFileWritten) {
	                    // For safety, make sure that the file that was created in
	                    // the directory is actually the one that we're expecting.
	                    File affectedFile = new File(parentDirectory, path);
	                    isFileWritten = affectedFile.equals(pictureFile);

	                    if (isFileWritten) {
	                        stopWatching();

	                        // Now that the file is ready, recursively call
	                        // processPictureWhenReady again (on the UI thread).
	                        runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                                processPictureWhenReady(picturePath);
	                            }
	                        });
	                    }
	                }
	            }
	        };
	        observer.startWatching();
	    }
	}
	
}
