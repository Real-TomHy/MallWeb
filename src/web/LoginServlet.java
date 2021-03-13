package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import domain.LoginUser;
import domain.NewUser;
import service.UserService;


public class LoginServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username"); //得到jsp页面传过来的参数
        String password = request.getParameter("password");
        UserService service = new UserService();
        LoginUser user = null;
        NewUser newUser=null;
        try {
            user = service.login(username,password);
            newUser = service.newlogin(username,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(user!=null){
            //登录成功
            //判断用户是否勾选自动登录
            String autoLogin = request.getParameter("autoLogin");
            if(autoLogin!=null){
                Cookie cookie_username = new Cookie("cookie_username",username);
                Cookie cookie_password = new Cookie("cookie_password",password);
                //设置cookie的持久化时间
                cookie_username.setMaxAge(6*6);
                cookie_password.setMaxAge(6*6);
                //设置cookie的携带路径
                cookie_username.setPath(request.getContextPath());
                cookie_password.setPath(request.getContextPath());
                //发送cookie
                response.addCookie(cookie_username);
                response.addCookie(cookie_password);
            }
            //将登录的用户的user对象存到session中
            session.setAttribute("user",user);
            session.setAttribute("cartuser",newUser);
            //重定向
            response.sendRedirect(request.getContextPath());
        }
        else {
            //登录失败，转发到登录页面
            request.setAttribute("logininfo","用户名或密码失败");
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }


    }
}

