package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import vo.PageBean;
import domain.Product;
import service.ProductService;


public class ProductListByCidServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //获得cid
        String currentPageStr = request.getParameter("currentPage");
        if(currentPageStr==null) currentPageStr="1";
        int currentPage = Integer.parseInt(currentPageStr);//接口是int
        //认为每页显示12条
        int currentCount = 12;
        String cid = request.getParameter("cid");
        ProductService service = new ProductService();
        PageBean<Product> pageBean=null;
        try {
            pageBean=service.findProductListByCid(currentPage,currentCount ,cid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.setAttribute("pageBean", pageBean);
        request.setAttribute("cid", cid);
        //定义一个记录历史商品信息的集合
        List<Product> historyProductList = new ArrayList<Product>();
        //获得客户端携带名字叫pids的cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("pids".equals(cookie.getName())){
                    String pids = cookie.getValue();//3-2-1
                    String[] split = pids.split("-");
                    for(String pid : split){
                        Product pro = service.findProductByPid(pid);
                        historyProductList.add(pro);
                    }
                }
            }}
        //将历史记录的集合放到域中
        request.setAttribute("historyProductList", historyProductList);
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }
}

