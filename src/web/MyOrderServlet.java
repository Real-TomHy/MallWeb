package web;
import java.io.IOException;
import domain.*;
import org.apache.commons.beanutils.BeanUtils;
import service.ProductService;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class MyOrderServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        NewUser user = (NewUser)session.getAttribute("cartuser");
        if(user==null){
//            没有登录
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }
        //查询该用户德所有订单信息
        ProductService service = new ProductService();
        //集合中德每一个Order对象的数据是不完整的
        List<Order> orderList = null;
        try {
            orderList = service.findAllOrders(user.getUid());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //循环所有的订单为每一个订单填充订单项集合信息
        if(orderList!=null){
            for(Order order:orderList){
                //获得每一个订单的oid
                String oid=order.getOid();
                //查询该订单的所有订单项---maplist封装多个订单项和多个订单项的商品信息。实则就是多表查询
                List<Map<String, Object>> MapList =service.findAllOrderItemByOid(oid);
                //将MapLIst转换为List<OrderItem> orderitemlist
                for(Map<String,Object> map:MapList){
                //从map中取出count subtotal 封装到OrderItem中
                OrderItem item = new OrderItem();
                //从map中取出pimage等封装到Procut中
                Product product=new Product();
                //将orderitem封装到order中的orderItemList中
                //传统写法--复杂
                //item.setCount(Integer.parseInt(map.get("count").toString()));
                    try {
                        BeanUtils.populate(item,map);
                        BeanUtils.populate(product,map);
                        //将product封装到OrderItem
                        item.setProduct(product);
                        //将orderitem封装到order中的orderItemList中
                        order.getOrderItems().add(item);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        //orderList封装完毕
        //把orderList放到域中，并重定向或者转发
        request.setAttribute("orderList",orderList);
        request.getRequestDispatcher("/order_list.jsp").forward(request,response);
    }
}

