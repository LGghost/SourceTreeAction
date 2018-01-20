package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class DataExportActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_export_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }


    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.financial_statements_layout)
    void financial_statements() {//财务报表
        Intent intent = new Intent(this, StatementActivity.class);
        intent.putExtra("sign", 1);
        startActivity(intent);
    }

    @OnClick(R.id.goods_sale_layout)
    void goods_sale() {//货品销售统计报表
        Intent intent = new Intent(this, StatementActivity.class);
        intent.putExtra("sign", 2);
        startActivity(intent);
    }

    @OnClick(R.id.detailed_statement_of_goods_layout)
    void detailed_statement() {//货品销售明细报表
        Intent intent = new Intent(this, StatementActivity.class);
        intent.putExtra("sign", 3);
        startActivity(intent);
    }

    @OnClick(R.id.export_history)
    void export_history() {//导出历史
        Intent intent = new Intent(this, ExportHistoryActivity.class);
        startActivity(intent);
    }

}