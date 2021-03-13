package dao;
import java.sql.SQLException;
import java.util.List;

import domain.Category;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import domain.Product;
import utils.DataSourceUtils;

public class CategoryDao {
    public List<Category> findAllCategory() throws SQLException{
        return new QueryRunner(DataSourceUtils.getDataSource()).query("select * from category", new BeanListHandler<Category>(Category.class));
    }
}
