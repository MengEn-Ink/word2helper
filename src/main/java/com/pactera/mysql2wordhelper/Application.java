package com.pactera.mysql2wordhelper;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把数据库中的表结构导出word中
 * @author MOSHUNWEI
 * @version 1.0
 */
public class Application
{
    public static void main( String[] args ) throws IOException
    {
    	
    	
    	Map<String, String> map =new HashMap<>();
    	map.put("-n","hnlh-manager");
    	map.put("-u","root");
    	map.put("-p","Pactera@SLN_dig-1");
    	map.put("h","192.168.9.19");
		map.put("p","3306");
		map.put("-d","./");

		MySQL(map);
    }

    
    public static void MySQL(Map<String,String> map) throws IOException{
    	//默认生成的文件名
    	String outFile = map.get("-d")+"/数据库表结构(MySQL).docx";
    	//查询表的名称以及一些表需要的信息
    	String mysqlSql1 = "SELECT table_name, table_type , ENGINE,table_collation,table_comment, create_options FROM information_schema.TABLES WHERE table_schema='"+map.get("-n")+"'";
    	//查询表的结构信息
    	String mysqlSql2 = "SELECT ordinal_position,column_name,column_type, column_key, extra ,is_nullable, column_default, column_comment,data_type,character_maximum_length "
    			+ "FROM information_schema.columns WHERE table_schema='"+map.get("-n")+"' and table_name='";
		String jdbcUrl = String.format("jdbc:mysql://%s:%s", map.get("h"), map.get("p"));
		ResultSet rs = SqlUtils.getResultSet(SqlUtils.getConnnection(jdbcUrl,map.get("-u"), map.get("-p")),mysqlSql1);
		Connection con = SqlUtils.getConnnection(jdbcUrl,map.get("-u"), map.get("-p"));
		createDoc(rs,mysqlSql2,map,outFile,true,"MySQL数据库表结构",con);
		
    }
    
    
    private static void createDoc(ResultSet rs,String sqls,Map<String,String> map,String outFile,boolean type,String title,Connection con) throws IOException{
		System.out.println("开始生成文件");
		List<Map<String, String>> list = getTableName(rs);
		RowRenderData header = getHeader();
		Map<String,Object> datas = new HashMap<>();
		datas.put("title", title);
		List<Map<String,Object>> tableList = new ArrayList<Map<String,Object>>();
		int i = 0;
		for(Map<String, String> str : list){
			System.out.println(str);
			i++;
			String sql = sqls+str.get("table_name")+"'";
			ResultSet set = SqlUtils.getResultSet(con,sql);
			List<RowRenderData> rowList = getRowRenderData(set);
			Map<String,Object> data = new HashMap<>();
			data.put("no", ""+i);
			data.put("table_comment",str.get("table_comment")+"");
			data.put("engine",str.get("engine")+"");
			data.put("table_collation",str.get("table_collation")+"");
			data.put("table_type",str.get("table_type")+"");
			data.put("name", new TextRenderData(str.get("table_name"), POITLStyle.getHeaderStyle()));
			data.put("table", new MiniTableRenderData(header, rowList));
			tableList.add(data);
		}
	
		datas.put("tablelist", new DocxRenderData(FileUtils.Base64ToFile(outFile,type), tableList));
		XWPFTemplate template = XWPFTemplate.compile(FileUtils.Base64ToInputStream()).render(datas);
	
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outFile);
			System.out.println("生成文件结束");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("生成文件失败");
		}finally {
			try {
				template.write(out);
				out.flush();
				out.close();
				template.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    


	/**
     * table的表头
     * @return RowRenderData
     */
    private static RowRenderData getHeader(){
    	RowRenderData header = RowRenderData.build(
				new TextRenderData("序号", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段名称", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段描述", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段类型", POITLStyle.getHeaderStyle()),
				new TextRenderData("长度", POITLStyle.getHeaderStyle()),
				new TextRenderData("允许空", POITLStyle.getHeaderStyle()),
				new TextRenderData("缺省值", POITLStyle.getHeaderStyle()));
    	header.setRowStyle(POITLStyle.getHeaderTableStyle());
//		header.setStyle(POITLStyle.getHeaderTableStyle());

		return header;
    }
    
    /**
     * 获取一张表的结构数据
     * @return List<RowRenderData>
     */
    private static List<RowRenderData> getRowRenderData(ResultSet set) {
    	List<RowRenderData> result = new ArrayList<>();
    	
    	try {
    		int i = 0;
			while(set.next()){
				i++;
				RowRenderData row = RowRenderData.build(
						new TextRenderData(set.getString("ordinal_position")+""),
						new TextRenderData(set.getString("column_name")+""),
						new TextRenderData(set.getString("column_comment")+""),
						new TextRenderData(set.getString("data_type")+""),
						new TextRenderData(set.getString("character_maximum_length")+""),
						new TextRenderData(set.getString("is_nullable")+""),
						new TextRenderData(set.getString("column_default")+"")
						);
				if(i%2==0){
					row.setRowStyle(POITLStyle.getBodyTableStyle());
					result.add(row);
				}else{
					result.add(row);
				}
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
  
		return result;
	}

    /**
     * 获取数据库的所有表名及表的信息
     * @return list
     */
    private static List<Map<String,String>> getTableName(ResultSet rs){
    	List<Map<String,String>> list = new ArrayList<>();
    	
    	try {
			while(rs.next()){
				Map<String,String> result = new HashMap<>();
				result.put("table_name", rs.getString("table_name")+"");
				result.put("table_type", rs.getString("table_type")+"");
				result.put("engine", rs.getString("engine")+"");
				result.put("table_collation", rs.getString("table_collation")+"");
				result.put("table_comment", rs.getString("table_comment")+"");
				result.put("create_options", rs.getString("create_options")+"");
				list.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return list;
    }

}
