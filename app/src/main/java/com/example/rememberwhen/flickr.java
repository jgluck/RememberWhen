package com.example.rememberwhen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kentwills on 5/13/14.
 */
public class flickr extends AsyncTask<String, String, String> {

    private HashMap<String, String> mData = null;// post data

    public flickr(HashMap<String, String> data) {
        mData = data;
    }

    public static String api_key = "b79c787dc2f9f079cca1dbc2746e83c8";
    public static String auth_token = "72157644715092963-c0f4d9bc18234bed";
    public static String api_sig = "0e22971829a32eda73aaa08ab36384a8";
    public static String lat = "38.992130";
    public static String lon = "-76.942914";
    public static String radius = "1";
    public static String results_per_page="5";

    @Override
    protected String doInBackground(String... params) {
        String data=get_geo_photos();
        Log.w("flickr", data);
        //get_photo("4","3206","2929406868","dc84ae77c0","test.jpg"));
        return "yes";
    }


    //https://www.flickr.com/services/api/explore/flickr.photos.search
    public static String get_geo_photos() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
                "&api_key=" + api_key +
                "&lat=" + lat +
                "&lon=" + lon +
                "&radius=" + radius +
                "&per_page="+results_per_page+
                "&page=1"+
                "&format=json"+
                "&nojsoncallback=1"+
                "&auth_token="+auth_token+
                "&api_sig="+api_sig;
        Log.w("flickr",url);
        JSONObject json = null;
        try {
            json = readJsonFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json.toString());
        return json.toString();
    }

    //https://www.flickr.com/services/api/misc.urls.html
    public static void get_photo(String farmid, String serverid,String id,String secret,String local_save) {
        Image flickrImage = null;
        String surl = "http://farm" + farmid + ".staticflickr.com/" + serverid + "/" + id + "_" + secret + ".jpg";
        try {
            InputStream URLcontent = (InputStream) new URL(surl).getContent();
            Drawable image = Drawable.createFromStream(URLcontent, "your source link");
            //URL url = new URL(surl);
            //Bitmap tgtImg = BitmapFactory.decodeFile("ImageD2.jpg");
            //image = ImageIO.read(url);
        }
        catch (Exception e){
            Log.w("flickr",e);
        }
        //imgRequest = urllib2.Request("http://farm" + farmid + ".staticflickr.com/" + serverid + "/" + id + "_" + secret + ".jpg");
        //imgData = urllib2.urlopen(imgRequest).read();

        //save a photo
        //save_photo_to_file(imgData, 'picture_out.jpg');
    }
    /*
    public static void save_photo_to_file(Image imgData, String file_name) {
        //with open (file_name, 'wb')as f:
        //f.write(imgData)
    }*/

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * on getting result
     */
    @Override
    protected void onPostExecute(String result) {
        // something...
    }

}
