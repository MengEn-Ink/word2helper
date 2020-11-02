package com.pactera.mysql2wordhelper.execute;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.pactera.mysql2wordhelper.template.SqlTemplate;
import com.pactera.mysql2wordhelper.utils.POITLStyle;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据处理
 */
public class DataHandler {


    /**
     * 数据库
     */
    static Db use = Db.use();


    @NotNull
    public static List<Map<String, Object>> geTableList() throws SQLException {
        //库名
        String catalog = use.getConnection().getCatalog();
        //表结构
        List<Entity> entityList = use.query(SqlTemplate.INFORMATION_SCHEMA_TABLES, catalog);
        //封装返回集合
        List<Map<String, Object>> tableList = new ArrayList<>();
        //表头
        RowRenderData header = getHeader();
        for (Entity entity : entityList) {
            String tableName = entity.getStr("table_name");
            List<RowRenderData> rowRenderData = DataHandler.getRowRenderData(catalog, tableName);
            Map<String, Object> data = new HashMap<>();
            data.put("no", entityList.indexOf(entity)+1);
            data.put("table_comment", entity.get("table_comment"));
            data.put("engine", entity.get("engine"));
            data.put("table_collation", entity.get("table_collation"));
            data.put("table_type", entity.get("table_type"));
            data.put("name", new TextRenderData(tableName, POITLStyle.getHeaderStyle()));
            data.put("table", new MiniTableRenderData(header, rowRenderData));
            tableList.add(data);
        }
        return tableList;
    }

    /**
     * 获取一张表的结构数据
     *
     * @return List<RowRenderData>
     */
    public static List<RowRenderData> getRowRenderData(String catalog, String table_name) throws SQLException {
        List<Entity> tables = use.query(SqlTemplate.INFORMATION_SCHEMA_COLUMNS, catalog, table_name);
        List<RowRenderData> result = new ArrayList<>();
        for (Entity table : tables) {
            RowRenderData row = RowRenderData.build(
                    new TextRenderData(table.getStr("ordinal_position")),
                    new TextRenderData(table.getStr("column_name")),
                    new TextRenderData(table.getStr("column_comment")),
                    new TextRenderData(table.getStr("data_type")),
                    new TextRenderData(table.getStr("character_maximum_length")),
                    new TextRenderData(table.getStr("is_nullable")),
                    new TextRenderData(table.getStr("column_default"))
            );
            result.add(row);
        }
        return result;
    }

    /**
     * table的表头
     * @return RowRenderData
     */
    public static RowRenderData getHeader(){
        RowRenderData header = RowRenderData.build(
                new TextRenderData("序号", POITLStyle.getHeaderStyle()),
                new TextRenderData("字段名称", POITLStyle.getHeaderStyle()),
                new TextRenderData("字段描述", POITLStyle.getHeaderStyle()),
                new TextRenderData("字段类型", POITLStyle.getHeaderStyle()),
                new TextRenderData("长度", POITLStyle.getHeaderStyle()),
                new TextRenderData("允许空", POITLStyle.getHeaderStyle()),
                new TextRenderData("缺省值", POITLStyle.getHeaderStyle()));
        header.setRowStyle(POITLStyle.getHeaderTableStyle());
        return header;
    }
}
