package org.jeecg;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: DruidSQLParserTest
 * Description: <br/>
 * date: 2021/7/2 16:04
 *
 * @author 赵宝龙
 * @since JDK 1.8
 */
@Slf4j
public class DruidSQLParserTest {

    @Test
    public void testParserDDL(){
        String sql="create table `jeecg-boot`.ces_field_kongj\n" +
                "(\n" +
                "\tid varchar(36) not null comment '主键'\n" +
                "\t\tprimary key,\n" +
                "\tcreate_by varchar(50) null comment '创建人',\n" +
                "\tcreate_time datetime null comment '创建日期',\n" +
                "\tupdate_by varchar(50) null comment '更新人',\n" +
                "\tupdate_time datetime null comment '更新日期',\n" +
                "\tsys_org_code varchar(64) null comment '所属部门',\n" +
                "\tname varchar(32) null comment '用户名',\n" +
                "\tsex varchar(32) null comment '下拉框',\n" +
                "\tradio varchar(32) null comment 'radio',\n" +
                "\tcheckbox varchar(32) null comment 'checkbox',\n" +
                "\tsel_mut varchar(32) null comment '下拉多选',\n" +
                "\tsel_search varchar(32) null comment '下拉搜索',\n" +
                "\tbirthday datetime null comment '时间',\n" +
                "\tpic varchar(1000) null comment '图片',\n" +
                "\tfiles varchar(1000) null comment '文件',\n" +
                "\tremakr text null comment 'markdown',\n" +
                "\tfuwenb text null comment '富文本',\n" +
                "\tuser_sel varchar(200) null comment '选择用户',\n" +
                "\tdep_sel varchar(200) null comment '选择部门',\n" +
                "\tddd double(10,0) null comment 'DD类型'\n" +
                ");\n" +
                "\n";
        String sql2="create table if not exists `jeecg-boot`.test_order\n" +
                "(\n" +
                "\tcolumn_1 varchar(36) null,\n" +
                "\tcolumn_2 int null,\n" +
                "\tcolumn_3 int null,\n" +
                "\tconstraint test_order_ces_order_main_id_fk\n" +
                "\t\tforeign key (column_1) references `jeecg-boot`.ces_order_main (id)\n" +
                ");";

        String sql3="select * from table1 t1 left join table2 t2 on t1.id=t2.id";
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, dbType);
        SchemaStatVisitor schemaStatVisitor = SQLUtils.createSchemaStatVisitor(dbType);
        for (SQLStatement sqlStatement : sqlStatements) {
            log.info("sql语句:{}",sql);

//            sqlStatement.accept(schemaStatVisitor);
//            Collection<TableStat.Column> columns = schemaStatVisitor.getColumns();
            log.info("表字段属性");
            List<SQLObject> children = sqlStatement.getChildren();
            List<SQLCommentHint> headHintsDirect = sqlStatement.getHeadHintsDirect();
            List<SQLTableElement> tableElementList = ((MySqlCreateTableStatement) sqlStatement).getTableElementList();
            for (SQLTableElement sqlTableElement : tableElementList) {
                log.info(sqlTableElement.toString());
            }
//            for (TableStat.Column column : columns) {
//                log.info(column.getName(),column.getAttributes());
//            }
        }
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql3, dbType);

        for (SQLStatement sqlStatement : statementList) {
            log.info("sql语句:{}",sql3);
            sqlStatement.accept(schemaStatVisitor);
            Set<TableStat.Relationship> relationships = schemaStatVisitor.getRelationships();
            for (TableStat.Relationship relationship : relationships) {
                TableStat.Column left = relationship.getLeft();
                TableStat.Column right = relationship.getRight();
                log.info("左表字段:{}",left.getName());
              log.info("右表字段:{}",right.getName());
                log.info("左表表名:{}",left.getTable());
                log.info("右表字段:{}",right.getTable());
            }
            Map<TableStat.Name, TableStat> tables = schemaStatVisitor.getTables();
            Set<Map.Entry<TableStat.Name, TableStat>> entries = tables.entrySet();
            for (Map.Entry<TableStat.Name, TableStat> entry : entries) {
                System.out.println(entry);
            }
        }

    }
}
