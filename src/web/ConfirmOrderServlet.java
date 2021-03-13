package web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import domain.LoginUser;
import domain.NewUser;
import domain.Order;
import org.apache.commons.beanutils.BeanUtils;
import service.ProductService;
import service.UserService;

public class ConfirmOrderServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("wd");
        //获取到参数
        //1、更新收货人信息
        Map<String, String[]> properties = request.getParameterMap();
        Order order = new Order();
        try {
            BeanUtils.populate(order, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        ProductService service = new ProductService();
        try {
            service.updateOrderAdrr(order);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("<h1>付款成功！等待商城进一步操作！等待收货...</h1>");
    }
}

