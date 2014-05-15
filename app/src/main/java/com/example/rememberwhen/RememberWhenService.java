package com.example.rememberwhen;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import java.util.ArrayList;

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
            Intent sudden_intent;
            ArrayList<String> voiceResults = intent.getExtras()
                    .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            if (voiceResults.size() == 0){
                sudden_intent = new Intent(this, RememberPhotoBundle.class);
            }else if(voiceResults.get(0).contains("memorize")){
                sudden_intent = new Intent(this, RememberCameraActivity.class);
            }else{
                sudden_intent = new Intent(this, RememberPhotoBundle.class);
            }




	        if (mLiveCard == null) {
	            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

	            RemoteViews views = new RemoteViews(this.getPackageName(),
	                    R.layout.remember_when_layout);
	            mLiveCard.setViews(views);

	            
	            Intent menuIntent = new Intent(this, RememberWhenMenu.class);

	            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
	            mLiveCard.attach(this);
	            mLiveCard.publish(PublishMode.REVEAL);
	        } else {
	            mLiveCard.navigate();
	        }
            sudden_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(sudden_intent);

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
