package com.example.rememberwhen;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RememberWhenMenu extends Activity {
	private final Handler mHandler = new Handler();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remember_when, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.stop:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
                post(new Runnable() {

                    @Override
                    public void run() {
                        stopService(new Intent(RememberWhenMenu.this, RememberWhenService.class));
                    }
                });
                return true;
            case R.id.Dowse:
            	startActivity(new Intent(this, RememberCameraActivity.class));
                return true;
            case R.id.gps_debug:
                startActivity(new Intent(this, GPSDebugActivity.class));
                return true;
            case R.id.example_cards:
                startActivity(new Intent(this, RememberPhotoBundle.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // Nothing else to do, closing the Activity.
        finish();
    }
    
    /**
     * Posts a {@link Runnable} at the end of the message loop, overridable for testing.
     */
    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }
	
}
