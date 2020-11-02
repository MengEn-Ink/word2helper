package com.pactera.mysql2wordhelper.template;

/**
 * @description: 数据库语句模板
 * @author: MengEn.Cao
 * @create: 2020-11-02 16:59
 **/
public class SqlTemplate {

    public final static String INFORMATION_SCHEMA_TABLES = "" +
            "SELECT \n" +
            "    table_name,\n" +
            "    table_type,\n" +
            "    ENGINE,\n" +
            "    table_collation,\n" +
            "    table_comment,\n" +
            "    create_options\n" +
            "FROM\n" +
            "    information_schema.TABLES\n" +
            "WHERE\n" +
            "    table_schema = ?";

    public final static String INFORMATION_SCHEMA_COLUMNS = "" +
            "SELECT \n" +
            "    ordinal_position,\n" +
            "    column_name,\n" +
            "    column_type,\n" +
            "    column_key,\n" +
            "    extra,\n" +
            "    is_nullable,\n" +
            "    column_default,\n" +
            "    column_comment,\n" +
            "    data_type,\n" +
            "    character_maximum_length\n" +
            "FROM\n" +
            "    information_schema.columns\n" +
            "WHERE\n" +
            "    table_schema = ? AND table_name = ?";
}
