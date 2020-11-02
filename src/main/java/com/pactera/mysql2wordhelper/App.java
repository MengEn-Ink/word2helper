package com.pactera.mysql2wordhelper;

import com.pactera.mysql2wordhelper.execute.DataHandler;
import com.pactera.mysql2wordhelper.utils.PoiTlUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws IOException, SQLException {
        //封装数据
        List<Map<String, Object>> tableList = DataHandler.geTableList();

        PoiTlUtil.XWPFUtil(tableList);

    }
}
