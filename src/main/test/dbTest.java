import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.pactera.mysql2wordhelper.execute.DataHandler;
import com.pactera.mysql2wordhelper.utils.PoiTlUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @description: 数据库操作测试
 * @author: MengEn.Cao
 * @create: 2020-11-02 16:34
 **/
public class dbTest {

    final static Log log = LogFactory.get();

    public static void main(String[] args) throws SQLException, IOException {


        //封装数据
        List<Map<String, Object>> tableList = DataHandler.geTableList();

        PoiTlUtil.XWPFUtil(tableList);


    }


}
