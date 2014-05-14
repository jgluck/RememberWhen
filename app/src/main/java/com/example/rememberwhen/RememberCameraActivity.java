package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.glass.media.CameraManager;
//import org.apache.http.client.methods.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.*;

import java.io.File;

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
    
    protected void loadPic(File f){
    	if(f.exists()){

    	    //Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
    	    BitmapFactory.Options options = new BitmapFactory.Options();
    	    options.inJustDecodeBounds = true;
    	    BitmapFactory.decodeFile(f.getAbsolutePath(), options);
    	    int imageHeight = options.outHeight;
    	    int imageWidth = options.outWidth;
    	    String imageType = options.outMimeType;
    	    if(imageWidth > imageHeight) {
    	        options.inSampleSize = calculateInSampleSize(options,512,256);//if landscape
    	    } else{
    	        options.inSampleSize = calculateInSampleSize(options,256,512);//if portrait
    	    }
    	    options.inJustDecodeBounds = false;
    	    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),options);
    	    ImageView myImage = (ImageView) findViewById(R.id.photoResult);
//    	    Drawable d = Drawable.createFromPath(f.getAbsolutePath());
    	    myImage.setImageBitmap(myBitmap);
    	    //myImage.setImageBitmap(Bitmap.createScaledBitmap(myBitmap,2040, 2040, false));
    	    //myImage.setImageBitmap(myBitmap);
    	}
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

    public void postData() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("operation", "insertItem");
        data.put("message", "AndDev is Cool!");
        //data.put("imageUrl", "/static/images/chipotle-tube-640x360.jpg");
        //data.put("contentType", "image/jpeg");

        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
        asyncHttpPost.execute("https://remember-when1.appspot.com/");
        /*
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://duruofei.com/ThermalGrid/?add=11101101011010100101101011011110101");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("operation", "insertItem"));
            //nameValuePairs.add(new BasicNameValuePair("message", "AndDev is Cool!"));
            //nameValuePairs.add(new BasicNameValuePair("imageUrl", "/static/images/chipotle-tube-640x360.jpg"));
            //nameValuePairs.add(new BasicNameValuePair("contentType", "image/jpeg"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.w("myApp", "success");
        } catch (Exception e) {
            Log.w("myApp", "fail"+e);
            // TODO Auto-generated catch block
        }*/
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
