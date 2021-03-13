package web;

import domain.Category;
import domain.NewProduct;
import service.ProductService;
import service.CategoryService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;




public class indexServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();
        CategoryService C_service = new CategoryService();
        //准备热门商品---List<Product>
        List<NewProduct> hotProductList=service.findHotProduct();
        //准备最新商品---List<Product>
        List<NewProduct> newProductList=service.findNewProduct();
        //准备分类数据
        List<Category> categoryList = C_service.findAllCategory();


        request.setAttribute("hotProductList",hotProductList);
        request.setAttribute("newProductList",newProductList);
        request.setAttribute("categoryList",categoryList);
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }
}

