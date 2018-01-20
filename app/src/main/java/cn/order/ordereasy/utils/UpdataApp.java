package cn.order.ordereasy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdataApp {

    private static final String DOWNLOAD_NAME = "ordereasy.apk";
    private Context context;

    public UpdataApp(Context context, String url) {
        this.context = context;
        DownloadTask downloadTask = new DownloadTask(
                context);
        downloadTask.execute(url);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private ProgressDialog progressDialog;

        public DownloadTask(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            // 设置进度条风格，风格为长形
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 设置ProgressDialog 标题
            progressDialog.setTitle("提示");
            // 设置ProgressDialog 提示信息
            progressDialog.setMessage("正在下载...");
            // 设置ProgressDialog 标题图标
//            progressDialog.setIcon(R.drawable.folder);
            // 设置ProgressDialog 进度条进度
            progressDialog.setProgress(100);
            // 设置ProgressDialog 的进度条是否不明确
            progressDialog.setIndeterminate(false);
            // 设置ProgressDialog 是否可以按退回按键取消
            progressDialog.setCancelable(true);
            // 设置点击屏幕ProgressDialog不消失
            progressDialog.setCanceledOnTouchOutside(false);
            // 让ProgressDialog显示
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_NAME);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;

                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
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
            if (progressDialog != null) {
                progressDialog.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (s == null) {
                update();
            }
        }

    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Android 7.0 系统共享文件需要通过 FileProvider 添加临时权限，否则系统会抛出 FileUriExposedException .
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "cn.order.ordereasy.provider", new File(Environment
                    .getExternalStorageDirectory(), DOWNLOAD_NAME));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), DOWNLOAD_NAME)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
