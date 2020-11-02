import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.deepoove.poi.data.DocxRenderData;
import com.pactera.mysql2wordhelper.execute.SqlTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @description: 数据库操作测试
 * @author: MengEn.Cao
 * @create: 2020-11-02 16:34
 **/
public class dbTest {

    public static void main(String[] args) throws SQLException {
        Db use = Db.use();

        String catalog = use.getConnection().getCatalog();
        //表信息
        List<Entity> catalogs = use.query(SqlTemplate.INFORMATION_SCHEMA_TABLES, catalog);
        /**
         * {
         * 	"table_comment": "",
         * 	"create_options": "",
         * 	"table_collation": "utf8_general_ci",
         * 	"table_name": "work_user_license",
         * 	"engine": "InnoDB",
         * 	"table_type": "BASE TABLE"
         * }
         */
        for (Entity entity : catalogs) {
            String table_name = entity.getStr("table_name");
            List<Entity> query = use.query(SqlTemplate.INFORMATION_SCHEMA_COLUMNS, catalog, table_name);
            query.stream().forEach(new Consumer<Entity>() {
                @Override
                public void accept(Entity entity) {
                    entity.set("no",query.indexOf(entity));
                    entity.set("name",table_name);
                    entity.set("table",entity);
                }
            });
            System.out.println(JSONUtil.parse(query));
        }
    }

    public Map<String, Object> getData(String title, DocxRenderData tableList) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("tablelist", tableList);
        return data;
    }
}
