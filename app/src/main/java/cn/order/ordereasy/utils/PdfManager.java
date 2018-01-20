package cn.order.ordereasy.utils;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfManager {

    public static String Pdf(String path, int type) {
        File file = new File(path);
        String outPdf = Config.DIR_IMAGE_PATH1 + "outPdf.pdf";
        if (file.exists()) {
            Document document;
            if (type == 0) {
                document = new Document(PageSize.A4, 20, 20, 20, 20);
            } else {
                document = new Document(new RectangleReadOnly(682.0F, 793.0F), 20, 20, 20, 20);
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outPdf);
                PdfWriter.getInstance(document, fos);
                document.open();
                Image image = Image.getInstance(path);
                float imageHeight = image.getScaledHeight();
                float imageWidth = image.getScaledWidth();
                int i = 0;
                while (imageHeight > 500 || imageWidth > 500) {
                    image.scalePercent(100 - i);
                    i++;
                    imageHeight = image.getScaledHeight();
                    imageWidth = image.getScaledWidth();
                }

                image.setAlignment(Image.MIDDLE);
                document.add(image);
                document.close();
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outPdf;
    }


    public static void print(Context context ,String file){
        File filePdf = new File(file);
        if (filePdf.exists()) {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityPrintPDF");
            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
            intent.setType("application/pdf");
            List<ResolveInfo> resInfoList = context.getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, FileUtils.getUriForFile(context, filePdf), Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setData(FileUtils.getUriForFile(context, filePdf));
            context.startActivity(intent);
        }
    }

}