package com.pactera.mysql2wordhelper.utils;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.pactera.mysql2wordhelper.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiTlUtil {

    final String MAIN_FILE_STRING="template/mainTemplate.docx";
    final String TABLE_FILE_STRING="template/tableTemplate.docx";


    public static void XWPFUtil(List<Map<String, Object>> tableList) throws IOException {
        String outFile = "./数据库表结构(MySQL)" + System.currentTimeMillis() + ".docx";
        DocxRenderData docxRenderData = new DocxRenderData(FileUtils.Base64ToFile(outFile), tableList);
        Map<String, Object> docxData = getData("MySQL数据库表结构", docxRenderData);
        XWPFTemplate template = XWPFTemplate.compile(FileUtils.Base64ToInputStream()).render(docxData);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            System.out.println("生成文件结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("生成文件失败");
        } finally {
            try {
                template.write(out);
                if (out != null) {
                    out.flush();
                    out.close();
                }
                template.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Map<String, Object> getData(String title, DocxRenderData tableList) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("tablelist", tableList);
        return data;
    }
}
