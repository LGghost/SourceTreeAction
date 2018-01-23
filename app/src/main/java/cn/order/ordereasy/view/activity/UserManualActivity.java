package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.barteksc.pdfviewer.PDFView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class UserManualActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manual_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        displayFromAssets("user_manual.pdf");
    }

    //找到控件ID
    @InjectView(R.id.user_pdfview)
    PDFView user_pdfview;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    private void displayFromAssets(String assetFileName) {
        user_pdfview.fromAsset(assetFileName) //设置pdf文件地址
                .defaultPage(0)  //设置默认显示第1页
                .enableSwipe(true) //是否允许翻页，默认是允许翻页
                .spacing(10)
                .load();
    }

}
