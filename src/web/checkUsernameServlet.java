package web;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Product;
import service.ProductService;
import service.UserService;
import vo.PageBean;

public class checkUsernameServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            //获得要校验的用户名
            String username = request.getParameter("username");
            //传递值到service
            UserService service = new UserService();
            boolean isExit=service.checkUsername(username);
            response.getWriter().write("{\"isExit\":"+isExit+"}");
    }
}
