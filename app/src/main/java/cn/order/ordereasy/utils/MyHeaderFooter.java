package cn.order.ordereasy.utils;

import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import cn.order.ordereasy.bean.Order;

/**
 * iText5中并没有之前版本HeaderFooter对象设置页眉和页脚<br>
 * 不过，可以利用PdfPageEventHelper来完成页眉页脚的设置工作。<br>
 * 就是在页面完成但写入内容之前触发事件，插入页眉、页脚、水印等。<br>
 * <p>
 * author wangnian
 * date 2016/4/1
 */
public class MyHeaderFooter extends PdfPageEventHelper {
    // 获取字体，其实不用这么麻烦，后面有简单方法
    private BaseFont bfChinese = null;
    private Font font12;
    private Font font8;
    // 总页数
    private PdfTemplate totalPage;
    private Order order;
    private Rectangle rectangle;
    private int type;

    public MyHeaderFooter(Order order, int type) {
        this.order = order;
        this.type = type;
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        font12 = new Font(bfChinese, 12, Font.BOLD);
        font8 = new Font(bfChinese, 8, Font.BOLD);
        if (type == 0) {
            rectangle = PageSize.A4;
        } else {
            rectangle = new RectangleReadOnly(682.0F, 793.0F);
        }
    }

    // 打开文档时，创建一个总页数的模版
    public void onOpenDocument(PdfWriter writer, Document document) {
        Log.e("MyHeaderFooter", "onOpenDocument" + writer);
        PdfContentByte cb = writer.getDirectContent();
        totalPage = cb.createTemplate(30, 16);
    }

    // 一页加载完成触发，写入页眉和页脚
    public void onEndPage(PdfWriter writer, Document document) {
        Log.e("MyHeaderFooter", "onEndPage" + writer);
        int page = writer.getCurrentPageNumber();

        try {
            //页眉
            PdfPTable table = new PdfPTable(3);
            int width[] = {60, 60, 60};//每个表格的宽
            table.setTotalWidth(rectangle.getWidth() - 30);
            table.setWidths(width);
            PdfPCell cell1 = new PdfPCell(new Paragraph("订货无忧-批发贸易移动管家", font8));
            PdfPCell cell2 = new PdfPCell(new Paragraph("www.dinghuo5u.com", font8));
            PdfPCell cell3 = new PdfPCell(new Paragraph("客户电话-13974977597", font8));
            //水平居中
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //垂直居中
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setBorder(0);
            cell2.setBorder(0);
            cell3.setBorder(0);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            // 将页眉写到document中，位置可以指定，指定到下面就是页脚
            table.writeSelectedRows(0, -1, 15, rectangle.getHeight() - 12, writer.getDirectContent());
            if (page != 1) {
                PdfPTable table4 = new PdfPTable(1);
                table4.setTotalWidth(rectangle.getWidth());
                int width4[] = {100};//每个表格的宽
                table4.setWidths(width4);
                Paragraph p = new Paragraph("");
                p.add(new Chunk(new LineSeparator()));
                PdfPCell cell8 = new PdfPCell(p);
                cell8.setBorder(0);
                table4.addCell(cell8);
                table4.writeSelectedRows(0, -1, 20, rectangle.getHeight() - 20, writer.getDirectContent());
                PdfPTable table6 = getListTable();
                table6.writeSelectedRows(0, -1, 0, rectangle.getHeight() - 32, writer.getDirectContent());
                PdfPTable table5 = new PdfPTable(1);
                table5.setTotalWidth(rectangle.getWidth());
                int width5[] = {100};//每个表格的宽
                table5.setWidths(width5);
                Paragraph p1 = new Paragraph("");
                p1.add(new Chunk(new LineSeparator()));
                PdfPCell cell9 = new PdfPCell(p);
                cell9.setBorder(0);
                table5.addCell(cell9);
                table5.writeSelectedRows(0, -1, 20, rectangle.getHeight() - 38, writer.getDirectContent());
            }
            PdfPTable table6 = new PdfPTable(1);
            int width6[] = {100};//每个表格的宽
            table6.setTotalWidth(rectangle.getWidth());
            table6.setWidths(width6);
            PdfPCell cell10 = new PdfPCell(new Paragraph(page + "", font8));
            cell10.setBorder(0);
            table6.addCell(cell10);
            if (type == 0) {
                table6.writeSelectedRows(0, -1, rectangle.getWidth() / 2 - 4, 30, writer.getDirectContent());
            } else {
                table6.writeSelectedRows(0, -1, rectangle.getWidth() / 2 - 4, rectangle.getHeight() - 2, writer.getDirectContent());
            }
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }


    }

    // 全部完成后，将总页数的pdf模版写到指定位置
    public void onCloseDocument(PdfWriter writer, Document document) {
        Log.e("MyHeaderFooter", "onCloseDocument" + writer);
        String text = writer.getPageNumber() - 1 + "";
        ColumnText.showTextAligned(totalPage, Element.ALIGN_LEFT, new Paragraph(text, font12), 50, 50, 0);
    }

    private PdfPTable getListTable() {

        PdfPTable table = new PdfPTable(6);
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font9 = new Font(bfChinese, 9, Font.BOLD);
            int width7[] = {20, 20, 20, 20, 20, 20};
            table.setTotalWidth(rectangle.getWidth());
            table.setWidths(width7);
            PdfPCell cell1 = new PdfPCell(new Paragraph("序号", font9));
            PdfPCell cell2 = new PdfPCell(new Paragraph("货品编号", font9));
            PdfPCell cell3 = new PdfPCell(new Paragraph("货品名称", font9));
            PdfPCell cell4 = new PdfPCell(new Paragraph("规格类型", font9));
            PdfPCell cell5 = new PdfPCell(new Paragraph("价格/数量", font9));
            PdfPCell cell6 = new PdfPCell(new Paragraph("金额", font9));
            //表格高度
            cell1.setFixedHeight(20);
            cell2.setFixedHeight(20);
            cell3.setFixedHeight(20);
            cell4.setFixedHeight(20);
            cell5.setFixedHeight(20);
            cell6.setFixedHeight(20);
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
}