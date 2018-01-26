package cn.order.ordereasy.view.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.pdf.PdfDocument.Page;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.PrintPreviewAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.PrintInfo;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * 打印预览Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class PrintPreviewActivity extends BaseActivity {
    private static final int UNIT_IN_INCH = 1000;
    private Order order;
    private PrintPreviewAdapter adapter;
    private List<PrintInfo> list = new ArrayList();
    private List<Goods> goods = new ArrayList<>();
    // PrintAttributes可以让你指定一种颜色模式，媒体尺寸，边距还有分辨率
    private PrintAttributes mPrintAttributes;
    // PrintDocumentInfo对象，用于描述所打印的内容
    private PrintDocumentInfo mDocumentInfo;
    private int mRenderPageWidth;
    private int mRenderPageHeight;
    private Context mPrintContext;
    private LinearLayout toplayout;
    private LinearLayout bottomlayout;
    private LinearLayout title_layout;
    private LinearLayout tail_layout;
    private TextView return_click;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_preview1);
        setColor(this, this.getResources().getColor(R.color.lanse));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = (Order) bundle.getSerializable("data");
        }
        return_click = (TextView) findViewById(R.id.return_click);
        return_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setData();
        print();
    }

    private void setData() {
        if (order != null) {
            View preview = this.getLayoutInflater().inflate(R.layout.print_preview, null);
            toplayout = (LinearLayout) preview.findViewById(R.id.print_preview_toplayout);
            title_layout = (LinearLayout) preview.findViewById(R.id.title_layout);
            bottomlayout = (LinearLayout) preview.findViewById(R.id.print_preview_bottomlayout);
            tail_layout = (LinearLayout) preview.findViewById(R.id.tail_layout);
            TextView order_number = (TextView) preview.findViewById(R.id.order_number);
            TextView order_date = (TextView) preview.findViewById(R.id.order_date);
            TextView customer_name = (TextView) preview.findViewById(R.id.customer_name);
            TextView customer_phone = (TextView) preview.findViewById(R.id.customer_phone);
            TextView receiving_address = (TextView) preview.findViewById(R.id.receiving_address);
            TextView drawer = (TextView) preview.findViewById(R.id.drawer);
            TextView order_amount = (TextView) preview.findViewById(R.id.order_amount);
            TextView remarks = (TextView) preview.findViewById(R.id.beizhu_text);
            TextView discount = (TextView) preview.findViewById(R.id.order_discount);

            order_number.setText(order.getOrder_no());
            order_date.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(order.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
            customer_name.setText(order.getCustomer_name());
            customer_phone.setText(order.getTelephone());
            receiving_address.setText(order.getAddress());
            drawer.setText(order.getUser_name());
            order_amount.setText(String.valueOf(order.getPayable()));
            remarks.setText(order.getRemark());
            discount.setText(String.valueOf(order.getDiscount_price()));
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
            adapter = new PrintPreviewAdapter(this, list);
        }
    }

    private void print() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 打印服务,访问打印队列，并提供PrintDocumentAdapter类支持
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
            builder.setMediaSize(PrintAttributes.MediaSize.NA_LETTER);
            printManager.print("MotoGP stats", new PrintDocument(), builder.build());
        } else {
            ToastUtil.show("系统不支持打印功能");
        }
    }

    class PrintDocument extends PrintDocumentAdapter {

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            boolean layoutNeeded = false;

            final int density = Math.max(newAttributes.getResolution()
                    .getHorizontalDpi(), newAttributes.getResolution()
                    .getVerticalDpi());

            final int marginLeft = (int) (density
                    * (float) newAttributes.getMinMargins().getLeftMils() / UNIT_IN_INCH);
            final int marginRight = (int) (density
                    * (float) newAttributes.getMinMargins().getRightMils() / UNIT_IN_INCH);
            final int contentWidth = (int) (density
                    * (float) newAttributes.getMediaSize().getWidthMils() / UNIT_IN_INCH)
                    - marginLeft - marginRight;
            if (mRenderPageWidth != contentWidth) {
                mRenderPageWidth = contentWidth;
                layoutNeeded = true;
            }

            final int marginTop = (int) (density
                    * (float) newAttributes.getMinMargins().getTopMils() / UNIT_IN_INCH);
            final int marginBottom = (int) (density
                    * (float) newAttributes.getMinMargins().getBottomMils() / UNIT_IN_INCH);
            final int contentHeight = (int) (density
                    * (float) newAttributes.getMediaSize().getHeightMils() / UNIT_IN_INCH)
                    - marginTop - marginBottom;
            if (mRenderPageHeight != contentHeight) {
                mRenderPageHeight = contentHeight;
                layoutNeeded = true;
            }

            if (mPrintContext == null
                    || mPrintContext.getResources().getConfiguration().densityDpi != density) {
                Configuration configuration = new Configuration();
                configuration.densityDpi = density;
                mPrintContext = createConfigurationContext(configuration);
                mPrintContext.setTheme(android.R.style.Theme_Holo_Light);
            }

            if (!layoutNeeded) {
                callback.onLayoutFinished(mDocumentInfo, false);
                return;
            }
            new PrintLayoutTask(newAttributes, cancellationSignal, callback).execute();
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                return;
            }
            new PrintWriteTask(pages, destination, cancellationSignal, callback).execute();
        }

        @Override
        public void onFinish() {
            PrintPreviewActivity.this.finish();
        }
    }

    class PrintLayoutTask extends AsyncTask<Void, Void, PrintDocumentInfo> {
        private CancellationSignal cancellationSignal;
        private PrintDocumentAdapter.LayoutResultCallback callback;
        private PrintAttributes newAttributes;

        private PrintLayoutTask(PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback) {
            this.cancellationSignal = cancellationSignal;
            this.callback = callback;
            this.newAttributes = newAttributes;
        }

        @Override
        protected void onPreExecute() {

            cancellationSignal
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            cancel(true);
                        }
                    });
            mPrintAttributes = newAttributes;
        }

        @Override
        protected PrintDocumentInfo doInBackground(Void... params) {
            int currentPage = 0;
            int pageContentHeight = 0;
            int viewType = -1;
            measureView(toplayout);
            pageContentHeight += toplayout.getMeasuredHeight();
            if (pageContentHeight > mRenderPageHeight) {
                pageContentHeight = toplayout
                        .getMeasuredHeight();
                currentPage++;
            }
            View view = null;
            LinearLayout dummyParent = new LinearLayout(
                    mPrintContext);
            dummyParent.setOrientation(LinearLayout.VERTICAL);

            final int itemCount = adapter.getCount();
            for (int i = 0; i < itemCount; i++) {
                if (isCancelled()) {
                    return null;
                }

                final int nextViewType = adapter
                        .getItemViewType(i);
                if (viewType == nextViewType) {
                    view = adapter
                            .getView(i, view, dummyParent);
                } else {
                    view = adapter
                            .getView(i, null, dummyParent);
                }
                viewType = nextViewType;

                measureView(view);

                pageContentHeight += view.getMeasuredHeight();
                int spacing = mRenderPageHeight - pageContentHeight - tail_layout.getMeasuredHeight();
                if (spacing > 200 && spacing < 300) {
                    measureView(tail_layout);
                    pageContentHeight = title_layout.getMeasuredHeight();
                    currentPage++;
                    measureView(title_layout);
                }
//                if (pageContentHeight > mRenderPageHeight) {
//                    pageContentHeight = view
//                            .getMeasuredHeight();
//                    currentPage++;
//                }
            }
            measureView(bottomlayout);
            pageContentHeight += bottomlayout.getMeasuredHeight();
            if (pageContentHeight > mRenderPageHeight) {
                currentPage++;
            }
            PrintDocumentInfo info = new PrintDocumentInfo.Builder(
                    "MotoGP_stats.pdf")
                    .setContentType(
                            PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(currentPage + 1).build();

            callback.onLayoutFinished(info, true);
            return info;
        }

        @Override
        protected void onPostExecute(PrintDocumentInfo result) {
            mDocumentInfo = result;
            Log.e("PrintPreviewActivity", "onPostExecute222");
        }

        protected void onCancelled(PrintDocumentInfo printDocumentInfo) {
            callback.onLayoutCancelled();
        }


    }

    class PrintWriteTask extends AsyncTask<Void, Void, PrintDocumentInfo> {
        private final SparseIntArray mWrittenPages = new SparseIntArray();
        // PrintedPdfDocument:基于特定PrintAttributeshelper创建PDF。
        private final PrintedPdfDocument mPdfDocument = new PrintedPdfDocument(
                PrintPreviewActivity.this, mPrintAttributes);
        private CancellationSignal cancellationSignal;
        private PrintDocumentAdapter.WriteResultCallback callback;
        private PrintAttributes newAttributes;
        private PageRange[] pages;
        private ParcelFileDescriptor destination;

        private PrintWriteTask(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
            this.pages = pages;
            this.destination = destination;
            this.cancellationSignal = cancellationSignal;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {

            cancellationSignal
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected PrintDocumentInfo doInBackground(Void... params) {
            int currentPage = -1;
            int pageContentHeight = 0;
            int viewType = -1;
            View view = null;
            Page page = null;

            LinearLayout dummyParent = new LinearLayout(
                    mPrintContext);
            dummyParent.setOrientation(LinearLayout.VERTICAL);

            final float scale = Math.min((float) mPdfDocument
                    .getPageContentRect().width()
                    / mRenderPageWidth, (float) mPdfDocument
                    .getPageContentRect().height()
                    / mRenderPageHeight);

            measureView(toplayout);
            if (currentPage < 0
                    || pageContentHeight > mRenderPageHeight) {
                pageContentHeight = toplayout.getMeasuredHeight();
                currentPage++;
                if (page != null) {
                    mPdfDocument.finishPage(page);
                }
                if (containsPage(pages, currentPage)) {
                    page = mPdfDocument.startPage(currentPage);
                    page.getCanvas().scale(scale, scale);
                    mWrittenPages.append(mWrittenPages.size(),
                            currentPage);
                } else {
                    page = null;
                }
            }

            if (page != null) {
                toplayout.layout(0, 0, toplayout.getMeasuredWidth(),
                        toplayout.getMeasuredHeight());
                toplayout.draw(page.getCanvas());
                page.getCanvas().translate(0, toplayout.getHeight());
            }
            final int itemCount = adapter.getCount();
            for (int i = 0; i < itemCount; i++) {
                if (isCancelled()) {
                    return null;
                }

                final int nextViewType = adapter.getItemViewType(i);
                if (viewType == nextViewType) {
                    view = adapter.getView(i, view, dummyParent);
                } else {
                    view = adapter.getView(i, null, dummyParent);
                }
                viewType = nextViewType;
                measureView(view);
                if (page != null) {
                    view.layout(0, 0, view.getMeasuredWidth(),
                            view.getMeasuredHeight());
                    view.draw(page.getCanvas());
                    page.getCanvas().translate(0, view.getHeight());
                }
                pageContentHeight += view.getMeasuredHeight();
//                if (currentPage < 0
//                        || pageContentHeight > mRenderPageHeight) {
//                    pageContentHeight = view.getMeasuredHeight();
//                    currentPage++;
//                    if (page != null) {
//                        mPdfDocument.finishPage(page);
//                    }
//                    if (containsPage(pages, currentPage)) {
//                        page = mPdfDocument.startPage(currentPage);
//                        page.getCanvas().scale(scale, scale);
//                        mWrittenPages.append(mWrittenPages.size(),
//                                currentPage);
//                    } else {
//                        page = null;
//                    }
//                }
                int spacing = mRenderPageHeight - pageContentHeight - tail_layout.getMeasuredHeight();
                if (spacing > 200 && spacing < 300) {
                    measureView(tail_layout);
                    if (page != null) {
                        tail_layout.layout(0, 0, tail_layout.getMeasuredWidth(),
                                tail_layout.getMeasuredHeight());
                        tail_layout.draw(page.getCanvas());
                        page.getCanvas().translate(0, tail_layout.getHeight());
                    }
                    pageContentHeight = title_layout.getMeasuredHeight();
                    currentPage++;
                    measureView(title_layout);
                    if (page != null) {
                        mPdfDocument.finishPage(page);
                    }
                    if (containsPage(pages, currentPage)) {
                        page = mPdfDocument.startPage(currentPage);
                        page.getCanvas().scale(scale, scale);
                        mWrittenPages.append(mWrittenPages.size(),
                                currentPage);
                    } else {
                        page = null;
                    }
                    if (page != null) {
                        title_layout.layout(0, 0, title_layout.getMeasuredWidth(),
                                title_layout.getMeasuredHeight());
                        title_layout.draw(page.getCanvas());
                        page.getCanvas().translate(0, title_layout.getHeight());
                    }
                }

            }
            measureView(bottomlayout);
            if (currentPage < 0
                    || pageContentHeight > mRenderPageHeight) {
                currentPage++;
                if (page != null) {
                    mPdfDocument.finishPage(page);
                }
                if (containsPage(pages, currentPage)) {
                    page = mPdfDocument.startPage(currentPage);
                    page.getCanvas().scale(scale, scale);
                    mWrittenPages.append(mWrittenPages.size(),
                            currentPage);
                } else {
                    page = null;
                }
            }

            if (page != null) {
                bottomlayout.layout(0, 0, bottomlayout.getMeasuredWidth(),
                        bottomlayout.getMeasuredHeight());
                bottomlayout.draw(page.getCanvas());
                page.getCanvas().translate(0, bottomlayout.getHeight());
            }
            if (page != null) {
                mPdfDocument.finishPage(page);
            }

            try {
                mPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
                PageRange[] pageRanges = computeWrittenPageRanges(mWrittenPages);
                callback.onWriteFinished(pageRanges);
            } catch (IOException ioe) {
                callback.onWriteFailed(null);
            } finally {
                mPdfDocument.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PrintDocumentInfo printDocumentInfo) {
            Log.e("PrintPreviewActivity", "onPostExecute");
        }

        @Override
        protected void onCancelled(PrintDocumentInfo printDocumentInfo) {
            callback.onWriteCancelled();
            mPdfDocument.close();
        }
    }

    private PageRange[] computeWrittenPageRanges(
            SparseIntArray writtenPages) {
        List<PageRange> pageRanges = new ArrayList<PageRange>();

        int start = -1;
        int end = -1;
        final int writtenPageCount = writtenPages.size();
        for (int i = 0; i < writtenPageCount; i++) {
            if (start < 0) {
                start = writtenPages.valueAt(i);
            }
            int oldEnd = end = start;
            while (i < writtenPageCount && (end - oldEnd) <= 1) {
                oldEnd = end;
                end = writtenPages.valueAt(i);
                i++;
            }
            PageRange pageRange = new PageRange(start, end);
            pageRanges.add(pageRange);
            start = end = -1;
        }

        PageRange[] pageRangesArray = new PageRange[pageRanges.size()];
        pageRanges.toArray(pageRangesArray);
        return pageRangesArray;
    }

    private boolean containsPage(PageRange[] pageRanges, int page) {
        final int pageRangeCount = pageRanges.length;
        for (int i = 0; i < pageRangeCount; i++) {
            if (pageRanges[i].getStart() <= page
                    && pageRanges[i].getEnd() >= page) {
                return true;
            }
        }
        return false;
    }

    private void measureView(View view) {
        final int widthMeasureSpec = ViewGroup.getChildMeasureSpec(
                View.MeasureSpec.makeMeasureSpec(mRenderPageWidth,
                        View.MeasureSpec.EXACTLY), 0,
                view.getLayoutParams().width);
        final int heightMeasureSpec = ViewGroup.getChildMeasureSpec(
                View.MeasureSpec.makeMeasureSpec(mRenderPageHeight,
                        View.MeasureSpec.EXACTLY), 0,
                view.getLayoutParams().height);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
