package com.example.rememberwhen;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
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

    public static String api_key = "b1ee1cd097d3e51136032e42982bb13c";
    public static String auth_token = "72157644256983320-2d75ec48e3c96572";
    public static String api_sig = "57ef74788a71651189054afdc7e36a0e";
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
                "&per_page=5"+
                "&page="+results_per_page+
                "&format=json"+
                "&nojsoncallback=1"+
                "&auth_token="+auth_token+
                "&api_sig="+api_sig;
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
    /*
    //https://www.flickr.com/services/api/misc.urls.html
    public static void get_photo(String farmid, String serverid,String id,String secret,String local_save) {
        //imgRequest = urllib2.Request("http://farm" + farmid + ".staticflickr.com/" + serverid + "/" + id + "_" + secret + ".jpg");
        //imgData = urllib2.urlopen(imgRequest).read();

        //save a photo
        //save_photo_to_file(imgData, 'picture_out.jpg');
    }
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
