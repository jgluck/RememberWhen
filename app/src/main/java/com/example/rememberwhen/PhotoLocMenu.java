package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotoLocMenu extends Activity {
    private final Handler mHandler = new Handler();
    SharedPreferences prefs;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        prefs = this.getSharedPreferences(
                "com.example.rememberwhen", Context.MODE_PRIVATE);
        inflater.inflate(R.menu.picture_location, menu);
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
                        stopService(new Intent(PhotoLocMenu.this, RememberWhenService.class)); //maybe broken
                    }
                });
                return true;
            case R.id.navigate:
                Intent navIntent = new Intent(Intent.ACTION_VIEW);
                String loc = prefs.getString("current_photo_loc","0,0");
                String lat = loc.split(",")[0];
                String lon = loc.split(",")[1];
                navIntent.setData(Uri.parse(String.format("google.navigation:ll=%s,%s&title=memory&mode=w",lat,lon)));

                startActivity(navIntent);
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
