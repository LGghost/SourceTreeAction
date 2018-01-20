package cn.order.ordereasy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownImage {
    private String path;
    private DownImageTask downImageTask;
    private Bitmap bitMap;
    private CompletionListener mlistener;
    public DownImage(String path) {
        this.path = path;
        downImageTask = new DownImageTask();
        downImageTask.execute();
    }

    class DownImageTask extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap map = null;
            try {
                URL url = new URL(path);
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream in;
                in = conn.getInputStream();
                map = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mlistener != null){
                mlistener.getCompletion(bitmap);
            }
        }
    }

    public void setCompletionListener(CompletionListener mlistener) {
        this.mlistener = mlistener;
    }

    public interface CompletionListener {
        void getCompletion(Bitmap bitmap);
    }
}
