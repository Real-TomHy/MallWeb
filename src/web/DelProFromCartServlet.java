package web;

import domain.Cart;
import domain.CartItem;
import domain.NewProduct;
import domain.Product;
import service.ProductService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DelProFromCartServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得要删除的item的pid
        String pid = request.getParameter("pid");
        //删除Session中购物项集合中的item
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Map<String,CartItem> cartItems = cart.getCartItems();
        //修改总金额
        cart.setTotal(cart.getTotal()-cartItems.get(pid).getSubtotal());
        cartItems.remove(pid);
        cart.setCartItems(cartItems);

        session.setAttribute("cart",cart);
        //重新跳转回购物车页面
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
}

