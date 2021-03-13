package web;

import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

public class ActiveServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得激活码
        String activeCode = request.getParameter("activeCode");

        //执行service
        UserService service = new UserService();
        service.active(activeCode);
        //跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");


    }
}


