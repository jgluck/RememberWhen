package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberwhen.OAuth.tasks.GetOAuthTokenTask;
import com.example.rememberwhen.OAuth.tasks.LoadPhotostreamTask;
import com.example.rememberwhen.OAuth.tasks.LoadUserTask;
import com.example.rememberwhen.OAuth.tasks.OAuthTask;
import com.google.android.glass.media.CameraManager;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RememberCameraActivity extends Activity{

    public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
    public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
    public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
    public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
    public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
    public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$


    private static final Logger logger = LoggerFactory.getLogger(RememberCameraActivity.class);

    private ImageView userIcon;
    private ListView listView;
    private TextView textUserTitle;
    private TextView textUserName;
    private TextView textUserId;

    public void setUser(User user) {
        textUserTitle.setText(user.getUsername());
        textUserName.setText(user.getRealName());
        textUserId.setText(user.getId());
    }

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    public void loadPic(Bitmap b){

            Bitmap myBitmap = b;
            ImageView myImage = (ImageView) findViewById(R.id.photoResult);
            TextView myText = (TextView) findViewById(R.id.photo_view_loading_text);
            myText.setVisibility(View.INVISIBLE);
            myImage.setImageBitmap(myBitmap);

    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
	        String picturePath = data.getStringExtra(
	                CameraManager.EXTRA_PICTURE_FILE_PATH);
            String thumbnailPath = data.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);
	        processPictureWhenReady(picturePath, thumbnailPath);
            super.onActivityResult(requestCode, resultCode, data);
	    }else{
            super.onActivityResult(requestCode, resultCode, data);
            finish();
        }

//	    super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPictureWhenReady(final String picturePath, final String thumbnailPath) {
	    final File pictureFile = new File(picturePath);
        final File pictureThumbnail = new File(thumbnailPath);

        if(pictureThumbnail.exists()){
            loadPic(pictureThumbnail);
        }

        if (pictureFile.exists()) {

            //Insert here
            HashMap<String, String> data = new HashMap<String, String>();
            Location loc = GPSDebugActivity.getLastLocation(this);
            data.put("lat", loc.getLatitude()+"");
            data.put("lon",loc.getLongitude()+"");
            //ImageView myImage = (ImageView) findViewById(R.id.photoResult);
            //TextView myText = (TextView) findViewById(R.id.photo_view_loading_text);
            //oauthTEST();
            //sendEmail(pictureFile);
            new AsyncMailer(picturePath,data, ImageHandler.loadPic(pictureFile),this).execute();

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
	                                processPictureWhenReady(picturePath,thumbnailPath);
	                            }
	                        });
	                    }
	                }
	            }
	        };
	        observer.startWatching();
	    }
	}

    public void oauthTEST(){
        OAuth oauth = getOAuthToken();
        if (oauth == null || oauth.getUser() == null) {
            OAuthTask task = new OAuthTask(this);
            task.execute();
        } else {
            load(oauth);
        }

        Intent intent = getIntent();
        String scheme = intent.getScheme();
        OAuth savedToken = getOAuthToken();
        if (CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
            Uri uri = intent.getData();
            String query = uri.getQuery();
            logger.debug("Returned Query: {}", query); //$NON-NLS-1$
            String[] data = query.split("&"); //$NON-NLS-1$
            if (data != null && data.length == 2) {
                String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
                String oauthVerifier = data[1]
                        .substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
                logger.debug("OAuth Token: {}; OAuth Verifier: {}", oauthToken, oauthVerifier); //$NON-NLS-1$

                oauth = getOAuthToken();
                if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
                    GetOAuthTokenTask task = new GetOAuthTokenTask(this);
                    task.execute(oauthToken, oauth.getToken().getOauthTokenSecret(), oauthVerifier);
                }
            }
        }
    }

    private void load(OAuth oauth) {
        if (oauth != null) {
            new LoadUserTask(this, userIcon).execute(oauth);
            new LoadPhotostreamTask(this, listView).execute(oauth);
        }
    }

    public void onOAuthDone(OAuth result) {
        if (result == null) {
            Toast.makeText(this,
                    "Authorization failed", //$NON-NLS-1$
                    Toast.LENGTH_LONG).show();
        } else {
            User user = result.getUser();
            OAuthToken token = result.getToken();
            if (user == null || user.getId() == null || token == null
                    || token.getOauthToken() == null
                    || token.getOauthTokenSecret() == null) {
                Toast.makeText(this,
                        "Authorization failed", //$NON-NLS-1$
                        Toast.LENGTH_LONG).show();
                return;
            }
            String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", //$NON-NLS-1$
                    user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            Toast.makeText(this,
                    message,
                    Toast.LENGTH_LONG).show();
            saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            load(result);
        }
    }


    public OAuth getOAuthToken() {
        //Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
        String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
        if (oauthTokenString == null && tokenSecret == null) {
            logger.warn("No oauth token retrieved"); //$NON-NLS-1$
            return null;
        }
        OAuth oauth = new OAuth();
        String userName = settings.getString(KEY_USER_NAME, null);
        String userId = settings.getString(KEY_USER_ID, null);
        if (userId != null) {
            User user = new User();
            user.setUsername(userName);
            user.setId(userId);
            oauth.setUser(user);
        }
        OAuthToken oauthToken = new OAuthToken();
        oauth.setToken(oauthToken);
        oauthToken.setOauthToken(oauthTokenString);
        oauthToken.setOauthTokenSecret(tokenSecret);
        logger.debug("Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
        return oauth;
    }

    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
        logger.debug("Saving userName=%s, userId=%s, oauth token={}, and token secret={}", new String[]{userName, userId, token, tokenSecret}); //$NON-NLS-1$
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_OAUTH_TOKEN, token);
        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }


	
}
