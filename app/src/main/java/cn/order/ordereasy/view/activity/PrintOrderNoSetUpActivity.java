package cn.order.ordereasy.view.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.PrintInfo;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyHeaderFooter;
import cn.order.ordereasy.utils.PdfManager;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;

public class PrintOrderNoSetUpActivity extends BaseActivity {
    private Order order;
    private List<Goods> goods = new ArrayList<>();
    private List<PrintInfo> list = new ArrayList();
    private SharedPreferences spPreferences;
    private String shopName;
    private String mobile;
    private String address;
    private String boss_name;
    private String outPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_set_up_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        spPreferences = getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");

        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            shopName = shop.get("name").getAsString();
            mobile = shop.get("mobile").getAsString();
            address = shop.get("address").getAsString();
            boss_name = shop.get("boss_name").getAsString();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = (Order) bundle.getSerializable("data");
        }
        initData();
    }

    private void initData() {
        goods = order.getGoods_list();
        if (goods.size() > 0) {
            for (int i = 0; i < goods.size(); i++) {
                List<Product> products = goods.get(i).getProduct_list();
                String itemCode = goods.get(i).getGoods_no();
                String goodsName = goods.get(i).getTitle();
                for (int j = 0; j < products.size(); j++) {
                    PrintInfo printInfo = new PrintInfo();
                    printInfo.setItemCode(itemCode);
                    printInfo.setGoodsName(goodsName);
                    List<String> spec_data = products.get(j).getSpec_data();
                    if (spec_data.size() == 1) {
                        printInfo.setSpecificationModel(spec_data.get(0));
                    } else {
                        printInfo.setSpecificationModel(spec_data.get(0) + "/" + spec_data.get(1));
                    }
                    printInfo.setUnitPrice(products.get(j).getSell_price() + "x" + products.get(j).getOperate_num());
                    double money = products.get(j).getSell_price() * products.get(j).getOperate_num();
                    printInfo.setMoney(money);
                    list.add(printInfo);
                }
            }
        }
    }

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.a4_layout)
    void a4_layout() {//A4纸模板
        if (SystemfieldUtils.isAppInstalled(this)) {//判断是否安装了PrintShare打印工具
            new CreatePdfTask(0).execute();
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

    public class CreatePdfTask extends AsyncTask<Void, Void, Void> {
        private int type;

        public CreatePdfTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            ProgressUtil.showDialog(PrintOrderNoSetUpActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (type == 0) {
                createOrderNoPdf(order, 0);
            } else {
                createOrderNoPdf(order, 1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ProgressUtil.dissDialog();
            PdfManager.print(PrintOrderNoSetUpActivity.this, outPdf);
        }
    }

    private void createOrderNoPdf(Order order, int type) {
        Rectangle rectangle;
        Document document;
        if (type == 0) {//区分A4纸和针式
            rectangle = PageSize.A4;
            document = new Document(rectangle, 20, 20, 50, 60);
        } else {
            rectangle = new RectangleReadOnly(682.0F, 793.0F);
            document = new Document(rectangle, 20, 20, 50, 60);
        }
        outPdf = Config.DIR_IMAGE_PATH1 + "outPdf.pdf";//文件保存的路径
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(outPdf));
            writer.setPageEvent(new MyHeaderFooter(order, type));
            document.open();
            BaseFont bfChinese = null;
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font18 = new Font(bfChinese, 18, Font.BOLD);//自定义字体大小
            Font font15 = new Font(bfChinese, 15, Font.BOLD);
            Font font10 = new Font(bfChinese, 10, Font.BOLD);
            Font font9 = new Font(bfChinese, 9, Font.BOLD);
            Font font8 = new Font(bfChinese, 8, Font.BOLD);
            PdfPTable table1 = new PdfPTable(2);
            int width1[] = {12, 35};//每个表格的宽
            table1.setWidths(width1);
            PdfPCell cell4 = new PdfPCell(new Paragraph(shopName, font18));
            PdfPCell cell5 = new PdfPCell(new Paragraph("销售单", font10));
            cell5.setPaddingTop(8);
            cell4.setBorder(0);//设置边框
            cell5.setBorder(0);
            table1.addCell(cell4);
            table1.addCell(cell5);
            table1.setWidthPercentage(100);
            document.add(table1);
            //加入空行
            Paragraph blankRow2 = new Paragraph(7f, " ", font8);
            document.add(blankRow2);
            PdfPTable table2 = new PdfPTable(3);
            int width3[] = {12, 15, 60};//每个表格的宽
            table2.setWidths(width3);
            PdfPCell cell6 = new PdfPCell(new Paragraph(boss_name, font9));
            PdfPCell cell7 = new PdfPCell(new Paragraph(mobile, font9));
            PdfPCell cell8 = new PdfPCell(new Paragraph(address, font9));
            cell6.setBorder(0);
            cell7.setBorder(0);
            cell8.setBorder(0);
            table2.addCell(cell6);
            table2.addCell(cell7);
            table2.addCell(cell8);
            table2.setWidthPercentage(100);
            document.add(table2);
            //加入空行
            Paragraph blankRow3 = new Paragraph(12f, " ", font18);
            document.add(blankRow3);

            PdfPTable table3 = new PdfPTable(2);
            int width4[] = {50, 50};//每个表格的宽
            table3.setWidths(width4);
            PdfPCell cell9 = new PdfPCell(new Paragraph("订单号：" + order.getOrder_no(), font10));
            PdfPCell cell10 = new PdfPCell(new Paragraph("日期：" + TimeUtil.getTimeStamp2Str(Long.parseLong(order.getCreate_time()), "yyyy-MM-dd HH:mm:ss"), font10));
            cell10.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell9.setBorder(0);
            cell10.setBorder(0);
            table3.addCell(cell9);
            table3.addCell(cell10);
            table3.setWidthPercentage(100);
            document.add(table3);
            //加入空行
            Paragraph blankRow4 = new Paragraph(9f, " ", font10);
            document.add(blankRow4);
            PdfPTable table4 = new PdfPTable(2);
            int width5[] = {50, 50};//每个表格的宽
            table4.setWidths(width5);
            PdfPCell cell11 = new PdfPCell(new Paragraph("客户姓名：" + order.getCustomer_name(), font10));
            PdfPCell cell12 = new PdfPCell(new Paragraph("联系方式：" + order.getTelephone(), font10));
            cell12.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell11.setBorder(0);
            cell12.setBorder(0);
            table4.addCell(cell11);
            table4.addCell(cell12);
            table4.setWidthPercentage(100);
            document.add(table4);
            //加入空行
            Paragraph blankRow5 = new Paragraph(9f, " ", font10);
            document.add(blankRow5);
            PdfPTable table5 = new PdfPTable(2);
            int width6[] = {50, 50};//每个表格的宽
            table5.setWidths(width6);
            PdfPCell cell13 = new PdfPCell(new Paragraph("收货地址：" + order.getAddress(), font10));
            PdfPCell cell14 = new PdfPCell(new Paragraph("开单人：" + order.getUser_name(), font10));
            cell14.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell13.setBorder(0);
            cell14.setBorder(0);
            table5.addCell(cell13);
            table5.addCell(cell14);
            table5.setWidthPercentage(100);
            document.add(table5);
            //直线
            Paragraph p1 = new Paragraph("");
            p1.add(new Chunk(new LineSeparator()));
            document.add(p1);
            PdfPTable table6 = new PdfPTable(6);
            int width7[] = {20, 20, 20, 60, 20, 20};
            table6.setWidths(width7);
            PdfPCell cell15 = new PdfPCell(new Paragraph("序号", font10));
            PdfPCell cell16 = new PdfPCell(new Paragraph("货品编号", font10));
            PdfPCell cell17 = new PdfPCell(new Paragraph("货品名称", font10));
            PdfPCell cell18 = new PdfPCell(new Paragraph("规格类型", font10));
            PdfPCell cell19 = new PdfPCell(new Paragraph("价格/数量", font10));
            PdfPCell cell20 = new PdfPCell(new Paragraph("金额", font10));
            //表格高度
            cell15.setFixedHeight(20);
            cell16.setFixedHeight(20);
            cell17.setFixedHeight(20);
            cell18.setFixedHeight(20);
            cell19.setFixedHeight(20);
            cell20.setFixedHeight(20);
            //水平居中
            cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
            //垂直居中
            cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell19.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell15.setBorder(0);
            cell16.setBorder(0);
            cell17.setBorder(0);
            cell18.setBorder(0);
            cell19.setBorder(0);
            cell20.setBorder(0);

            table6.addCell(cell15);
            table6.addCell(cell16);
            table6.addCell(cell17);
            table6.addCell(cell18);
            table6.addCell(cell19);
            table6.addCell(cell20);
            table6.setWidthPercentage(100);
            document.add(table6);
            //直线
            Paragraph p2 = new Paragraph();
            p2.add(new Chunk(new LineSeparator()));
            p2.setSpacingBefore(-10);
            document.add(p2);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (type != 0) {
                        if (i == 23) {
                            getFooter(document);
                            document.newPage();
                        }
                        if (i > 23 && (i - 23) % 29 == 0) {
                            getFooter(document);
                            document.newPage();
                        }
                    } else {
                        if (i == 26) {
                            getFooter(document);
                            document.newPage();
                        }
                        if (i > 23 && (i - 23) % 32 == 0) {
                            getFooter(document);
                            document.newPage();
                        }
                    }
                    document.add(getListTable(i, list.get(i)));
                }
            }
            //直线
            Paragraph p3 = new Paragraph("");
            p3.add(new Chunk(new LineSeparator()));
            p3.setSpacingBefore(-5);
            document.add(p3);
            double price = order.getOrder_sum() - order.getPayable();
            price = (double) Math.round(price * 100) / 100;
            if (price > 0) {
                Paragraph paragraph = new Paragraph("订单金额：" + String.valueOf(order.getPayable()) + "(优惠" + price + ")", font15);
                document.add(paragraph);
            } else {
                Paragraph paragraph = new Paragraph("订单金额：" + String.valueOf(order.getPayable()), font15);
                document.add(paragraph);
            }
            //直线
            Paragraph p4 = new Paragraph("");
            p4.add(new Chunk(new LineSeparator()));
            p4.setSpacingBefore(-5);
            document.add(p4);
            getFooter(document);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPTable getListTable(int i, PrintInfo printInfo) {

        PdfPTable table = new PdfPTable(6);
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font9 = new Font(bfChinese, 9, Font.BOLD);
            int width7[] = {20, 20, 20, 60, 20, 20};
            table.setWidths(width7);
            PdfPCell cell1 = new PdfPCell(new Paragraph(i + 1 + "", font9));
            PdfPCell cell2 = new PdfPCell(new Paragraph(printInfo.getItemCode() + "", font9));
            PdfPCell cell3 = new PdfPCell(new Paragraph(printInfo.getGoodsName(), font9));
            PdfPCell cell4 = new PdfPCell(new Paragraph(printInfo.getSpecificationModel(), font9));
            PdfPCell cell5 = new PdfPCell(new Paragraph(printInfo.getUnitPrice(), font9));
            PdfPCell cell6 = new PdfPCell(new Paragraph("¥" + printInfo.getMoney(), font9));
            //表格高度
            cell1.setFixedHeight(19);
            cell2.setFixedHeight(19);
            cell3.setFixedHeight(19);
            cell4.setFixedHeight(19);
            cell5.setFixedHeight(19);
            cell6.setFixedHeight(19);
            //水平居中
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            //垂直居中
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell1.setBorder(0);
            cell2.setBorder(0);
            cell3.setBorder(0);
            cell4.setBorder(0);
            cell5.setBorder(0);
            cell6.setBorder(0);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.setWidthPercentage(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    private void getFooter(Document document) {
        //页脚
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font8 = new Font(bfChinese, 8, Font.BOLD);
            Font font10 = new Font(bfChinese, 10, Font.BOLD);
            Font font17 = new Font(bfChinese, 17, Font.BOLD);
            Paragraph paragraph = new Paragraph("备注：" + order.getRemark(), font10);
            document.add(paragraph);
            Paragraph paragraph1 = new Paragraph("注明：" + "请将货物核对清楚，发现质量问题请及时与我们联系", font8);
            document.add(paragraph1);

            PdfPTable table = new PdfPTable(2);
            int width[] = {50, 50};//每个表格的宽
            table.setWidths(width);
            PdfPCell cell = new PdfPCell(new Paragraph("审核签章：", font17));
            PdfPCell cell2 = new PdfPCell(new Paragraph("账务签章：", font17));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setVerticalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            cell2.setBorder(0);
            table.addCell(cell);
            table.addCell(cell2);
            table.setWidthPercentage(100);
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}