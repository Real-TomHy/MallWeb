package web;

import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;


public class QuitUserServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取cookies的数据,是一个数组
        cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("cookie_username") || cookie.getName().equals("cookie_password")) {
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                    System.out.println(cookie.getName());
                }
            }
        }
        request.setAttribute("logininfo", "重新登录");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}