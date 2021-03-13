package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import domain.NewProduct;
import domain.Order;
import domain.OrderItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import domain.Product;
import utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findAllProduct() throws SQLException {
		return new QueryRunner(DataSourceUtils.getDataSource()).query("select * from product", new BeanListHandler<Product>(Product.class));
	}

	//获得全部的商品条数
	public int getTotalCount() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product";
		Long query = (Long) runner.query(sql, new ScalarHandler());
		return query.intValue();
	}

	//获得分页的商品数据
	public List<Product> findProductListForPageBean(int index,int currentCount) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product limit ?,?";//第一个?是初始值，第二个？是查多少数据
		return runner.query(sql, new BeanListHandler<Product>(Product.class), index,currentCount);
	}

    public List<Object> findProductByWord(String word) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pname like ? limit 0,5";//第一个?是初始值，第二个？是查多少数据
		List<Object> query = runner.query(sql, new ColumnListHandler("pname"), "%"+word+"%");
		return query;
	}

    public List<NewProduct> findHotProductList() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		return runner.query(sql,new BeanListHandler<NewProduct>(NewProduct.class),1,0,9);
    }

	public List<NewProduct> findNewProductList() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate limit ?,?";
		return runner.query(sql,new BeanListHandler<NewProduct>(NewProduct.class),0,9);
	}


	public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";//第一个?是初始值，第二个？是查多少数据
		return runner.query(sql, new BeanListHandler<Product>(Product.class),cid, index,currentCount);
	}

	public Product findProductByPid(String pid) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid =?";
		return  runner.query(sql,new BeanHandler<Product>(Product.class),pid);

	}
	//向orderitem表插入数据
    public void addOrderItem(Order order) throws SQLException{
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem item : orderItems){
			runner.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
		}
    }
	//向orders表插入数据
	public void addOrders(Order order) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		runner.update(conn,sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
				order.getAddr(),order.getName(),order.getTelephone(),order.getUser().getUid());
	}
	//更新orders
    public void updateOrderAdrr(Order order) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address=?,name=?,telephone=? where oid=?";
		runner.update(sql, order.getAddr(),order.getName(),order.getTelephone(),order.getOid());

    }
    //将orders中state的状态改变
	public void updateOrderState(String r6_Order) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set state=? where oid=?";
		runner.update(sql, 1,r6_Order);
	}

    public List<Order> findAllOrders(String uid) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid =?";
		return  runner.query(sql,new BeanListHandler<Order>(Order.class),uid);
    }

	public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid=?";
		return  runner.query(sql,new MapListHandler(),oid);
	}
}
