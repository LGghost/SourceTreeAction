package cn.order.ordereasy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DownloadFile {

    private static final String DOWNLOAD_FILE_NAME = "/ordereasy/file/";
    private DownloadFileClickLister lister;
    private Context context;
    private String fileName;

    public DownloadFile(Context context, String url, String fileName) {
        this.context = context;
        this.fileName = fileName;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {
        private File file = null;

        public DownloadTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                Log.e("jjf", "url:" + url);
                connection = (HttpURLConnection) url.openConnection();
                SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
                String token = spPreferences.getString("token", "");
                connection.addRequestProperty("token", token);
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_FILE_NAME + fileName + FileUtils.getTheSuffix(connection.getHeaderField("content-Disposition")));

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }else{
                            FileUtils.deleteDir(file.getParentFile().getPath());
                            file.getParentFile().mkdirs();
                        }
                    }

                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                if (lister != null) {
                    lister.downloadFail();
                }
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("jjf", "file:" + file.getPath());
            if (lister != null) {
                lister.downloadComplete(file);
            }
        }

    }

    public void setDownloadFileClickLister(DownloadFileClickLister lister) {
        this.lister = lister;
    }

    public interface DownloadFileClickLister {
        void downloadComplete(File file);

        void downloadFail();
    }

}