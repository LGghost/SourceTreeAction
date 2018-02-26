package cn.order.ordereasy.view.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.PdfManager;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;

public class PrintSetUpActivity extends BaseActivity {
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_set_up_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            path = bundle.getString("path");
        }
    }

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.a4_layout)
    void a4_layout() {//A4纸模板
        if (SystemfieldUtils.isAppInstalled(this)) {//判断是否安装了打印软件
            new CreatePdfTask(0).execute();//异步生成pdf文件
        } else {
            ToastUtil.show("请先安装PrintShare打印工具");
        }
    }

    @OnClick(R.id.zhenshi_layout)
    void zhenshi_layout() {//针式纸模板
        if (SystemfieldUtils.isAppInstalled(this)) {
            new CreatePdfTask(1).execute();
        } else {
            ToastUtil.show("请先安装PrintShare打印工具");
        }
    }

    public class CreatePdfTask extends AsyncTask<Void, Void, String> {
        private int type;

        public CreatePdfTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            ProgressUtil.showDialog(PrintSetUpActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            String file;
            if (type == 0) {
                file = PdfManager.Pdf(path, 0);
            } else {
                file = PdfManager.Pdf(path, 1);
            }
            return file;
        }

        @Override
        protected void onPostExecute(String file) {
            ProgressUtil.dissDialog();
            PdfManager.print(PrintSetUpActivity.this, file);//跳转到PrintShare打印界面中进行打印
        }
    }
}