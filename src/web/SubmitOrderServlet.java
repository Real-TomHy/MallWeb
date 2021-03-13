package web;

import domain.*;
import service.ProductService;
import utils.CommonsUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SubmitOrderServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先判断是否登录
        HttpSession session=request.getSession();
//        //查看session
//        Enumeration<?> enumeration = session.getAttributeNames();
//// 遍历enumeration
//        while (enumeration.hasMoreElements()) {
//            // 获取session的属性名称
//            String name = enumeration.nextElement().toString();
//            // 根据键值取session中的值
//            Object value = session.getAttribute(name);
//            // 打印结果
//            System.out.println("----------" + name + "------------" + value );
//        }

        NewUser user = (NewUser)session.getAttribute("cartuser");
        if(user==null){
//            没有登录
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }

        //封装好一个Order对象 传递给service层
        Order order = new Order();
        String oid = CommonsUtils.getUid();
        order.setOid(oid);//封装订单号
        order.setOrdertime(new Date());//封装下单时间
        //获得session购物车
        Cart cart = (Cart) session.getAttribute("cart");
        double total = cart.getTotal();//订单金额
        order.setTotal(total);
        order.setState(0);//订单状态
        order.setAddr(null);//地址
        order.setName(null);//名字
        order.setTelephone(null);//电话
        order.setUser(user);//用户
        //获得购物车中的购物项的集合map
        Map<String, CartItem> cartItems = cart.getCartItems();
        for (Map.Entry<String,CartItem> entry:cartItems.entrySet()){
            //取出每一个购物项
            CartItem cartItem=entry.getValue();
            //创建新的订单项
            OrderItem orderitem = new OrderItem();
            orderitem.setItemid(CommonsUtils.getUid());
            orderitem.setCount(cartItem.getBuyNum());
            orderitem.setSubtotal(cartItem.getSubtotal());
            orderitem.setProduct(cartItem.getProduct());
            orderitem.setOrder(order);


            //将该订单项添加到订单的订单项集合中
            order.getOrderItems().add(orderitem);

        }
        //order对象封装完毕
        //传递数据到service层
        ProductService service = new ProductService();
        try {
            service.submitOrder(order);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        session.setAttribute("order",order);
        //页面跳转
        response.sendRedirect(request.getContextPath()+"/order_info.jsp");

    }
}

