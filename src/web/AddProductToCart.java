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


public class AddProductToCart extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        ProductService service = new ProductService();
        //要获得放到购物车的商品的pid
        String pid = request.getParameter("pid");
        //获得该商品的购买数量
        int buyNum = Integer.parseInt(request.getParameter("buyNum"));
        //把商品封装为购物项
        Product product = service.findProductByPid(pid);
        //计算金额
        double subtotal = product.getShop_price()*buyNum;
        //封装CartItem
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setBuyNum(buyNum);
        item.setSubtotal(subtotal);
        //获得购物车---判断是否已经存在购物车
        Cart cart=(Cart) session.getAttribute("cart");
        if(cart==null){
            cart = new Cart();
        }
        //将购物项放在Session---key是pid
        //判断购物车是否包含该购物项---依据key
        Map<String,CartItem> cartItems = cart.getCartItems();
        double newsubtotal=0.0;
        //如果已经存在
        if(cartItems.containsKey(pid)){
            CartItem cartItem = cartItems.get(pid);
            int oldBuyNum=cartItem.getBuyNum();
            oldBuyNum+=buyNum;
            cartItem.setBuyNum(oldBuyNum);
            cart.setCartItems(cartItems);
            //修改金额
            double oldsubtotal =cartItem.getSubtotal();
            newsubtotal = buyNum*product.getShop_price();
            cartItem.setSubtotal(oldsubtotal+newsubtotal);
            }
        //如果不存在
        else{
            cart.getCartItems().put(product.getPid(),item);
            newsubtotal=buyNum*product.getShop_price();
        }

        //计算总金额
        double total = cart.getTotal()+newsubtotal;
        cart.setTotal(total);
        //将车再次访问Session
        session.setAttribute("cart",cart);
        //直接跳转到购物车页面
        //request.getRequestDispatcher("/cart.jsp").forward(request,response);
        //转发的时候，转发一次，购买一次。所以应该重定向
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
}

