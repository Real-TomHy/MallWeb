package web;

import java.io.IOException;
import domain.Category;
import redis.clients.jedis.Jedis;
import service.CategoryService;
import utils.JedisPoolUtils;

import java.util.List;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class CatergoryListServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryService C_service = new CategoryService();
//        //1.先从缓存中查询category，如果没有，从数据库中查询，查询后把数据发在缓存中
//        Jedis jedis = JedisPoolUtils.getJedis();
//        String categoryListJson = jedis.get("categoryListJson");
//        //2.判断categoryListJson是否为空
//        if(categoryListJson==null){
//            System.out.println("缓存中没有数据");
//            //准备分类数据
//            List<Category> categoryList = C_service.findAllCategory();
//            Gson gson = new Gson();
//            categoryListJson = gson.toJson(categoryList);
//            jedis.set("categoryListJson",categoryListJson);
//        }

        List<Category> categoryList = C_service.findAllCategory();
        Gson gson = new Gson();
        String categoryListJson = gson.toJson(categoryList);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryListJson);
    }
}

