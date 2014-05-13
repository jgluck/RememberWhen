package com.example.rememberwhen;



import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class RememberWhenService extends Service{
	 private static final String LIVE_CARD_TAG = "RememberWhen";
	 //private ChronometerDrawer mCallback
	 
	 private LiveCard mLiveCard;
	 
	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }
	    
	    
	    
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        if (mLiveCard == null) {
	            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

	            // Keep track of the callback to remove it before unpublishing.
	            //mCallback = new ChronometerDrawer(this);
	            //mLiveCard.setDirectRenderingEnabled(true).getSurfaceHolder().addCallback(mCallback);
	            RemoteViews views = new RemoteViews(this.getPackageName(),
	                    R.layout.remember_camera_layout);
	            mLiveCard.setViews(views);
	            
	            Intent menuIntent = new Intent(this, RememberWhenMenu.class);
//	            Intent cameraIntent = new Intent(this, RememberCameraActivity.class);
//	            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//	            startActivity(cameraIntent);
	            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
//	            mLiveCard.setAction(PendingIntent.getActivity(this, 0, cameraIntent, 0));
	            mLiveCard.attach(this);
	            mLiveCard.publish(PublishMode.REVEAL);
	        } else {
	            Intent cameraIntent = new Intent(this, RememberCameraActivity.class);
	            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            startActivity(cameraIntent);
	            mLiveCard.navigate();
//	            Intent cameraIntent = new Intent(this, RememberCameraActivity.class);
//	            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//	            startActivity(cameraIntent);
	        }

	        return START_STICKY;
	    }
	    
	    
	    @Override
	    public void onDestroy() {
	        if (mLiveCard != null && mLiveCard.isPublished()) {
	            mLiveCard.unpublish();
	            mLiveCard = null;
	        }
	        super.onDestroy();
	    }
	
	    
	    
}
