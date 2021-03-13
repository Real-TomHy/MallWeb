package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.ProductDao;
import domain.NewProduct;
import domain.Order;
import domain.OrderItem;
import domain.Product;
import utils.DataSourceUtils;
import vo.PageBean;

public class ProductService {

	public List<Product> findAllProduct() throws SQLException {
		ProductDao dao = new ProductDao();
		return dao.findAllProduct();
	}

	//分页操作
	public PageBean findPageBean(int currentPage,int currentCount) throws SQLException  {
		
		ProductDao dao = new ProductDao();
		
		//目的：就是想办法封装一个PageBean 并返回
		PageBean pageBean = new PageBean();
		//1、当前页private int currentPage;
		pageBean.setCurrentPage(currentPage);
		//2、当前页显示的条数private int currentCount;
		pageBean.setCurrentCount(currentCount);
		//3、总条数private int totalCount;
		int totalCount = dao.getTotalCount();
		pageBean.setTotalCount(totalCount);
		//4、总页数private int totalPage;
		/*
		 * 总条数		当前页显示的条数	总页数
		 * 10		4				3
		 * 11		4				3
		 * 12		4				3
		 * 13		4				4
		 * 
		 * 公式：总页数=Math.ceil(总条数/当前显示的条数)
		 * 
		 */
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5、每页显示的数据private List<T> productList = new ArrayList<T>();
		/*
		 * 页数与limit起始索引的关系
		 * 例如 每页显示4条
		 * 页数		其实索引		每页显示条数
		 * 1		0			4
		 * 2		4			4
		 * 3		8			4
		 * 4		12			4
		 * 
		 * 索引index = (当前页数-1)*每页显示的条数
		 * 
		 */
		int index = (currentPage-1)*currentCount;
		
		List<Product> productList = dao.findProductListForPageBean(index,currentCount);
		pageBean.setProductList(productList);
		
		return pageBean;
	}


//	根据关键字查询商品
	public List<Object> findProductByWord(String word) throws SQLException{
		ProductDao dao =new ProductDao();
		return dao.findProductByWord(word);
}
//获得热门商品
	public List<NewProduct> findHotProduct() {
		ProductDao dao = new ProductDao();
		List<NewProduct> hotProductList =null;
		try {
			hotProductList= dao.findHotProductList();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return hotProductList;
	}
//获得最新商品
	public List<NewProduct> findNewProduct() {
		ProductDao dao = new ProductDao();
		List<NewProduct> NewProductList =null;
		try {
			NewProductList= dao.findNewProductList();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return NewProductList;
	}

	public PageBean<Product> findProductListByCid(int currentPage,int currentCount,String cid) throws SQLException{
		ProductDao dao = new ProductDao();
		//封装一个PageBean 返回Web层
		PageBean<Product> pageBean = new PageBean<>();

		//1.封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、当前页显示的条数private int currentCount;
		pageBean.setCurrentCount(currentCount);
		//3、总条数private int totalCount;
		int totalCount = dao.getTotalCount();
		pageBean.setTotalCount(totalCount);
		//4、总页数private int totalPage;
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//5、每页显示的数据
		int index = (currentPage-1)*currentCount;
		List<Product> list=null;
		list=dao.findProductByPage(cid,index,currentCount);
		pageBean.setProductList(list);
		return pageBean;
	}

	public Product findProductByPid(String pid) {
		ProductDao dao = new ProductDao();
		Product product= null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return product;
	}
//提交订单 将订单的数据和订单项的数据存储到数据库中
    public void submitOrder(Order order) throws SQLException {
		ProductDao dao = new ProductDao();
		try {
			//1.开启事务
			DataSourceUtils.startTransaction();
			//2.调用dao存储order表数据的方法
			dao.addOrders(order);
			//3. 调用dao存储到orderitem数据方法
			dao.addOrderItem(order);
		} catch (SQLException e) {
			DataSourceUtils.rollback();
			e.printStackTrace();
		}finally {
			DataSourceUtils.commitAndRelease();
		}
	}

    public void updateOrderAdrr(Order order) throws SQLException{
		ProductDao dao = new ProductDao();
		dao.updateOrderAdrr(order);
		dao.updateOrderState(order.getOid());
    }
    //获得指定用户的订单集合
    public List<Order> findAllOrders(String uid) throws SQLException{
		ProductDao dao = new ProductDao();
		List<Order> orderList=dao.findAllOrders(uid);
		return orderList;
    }

	public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> orderItemList= null;
		try {
			orderItemList = dao.findAllOrderItemByOid(oid);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return orderItemList;
	}
}
