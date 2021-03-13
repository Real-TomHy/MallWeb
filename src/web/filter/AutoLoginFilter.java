package web.filter;

import domain.LoginUser;
import domain.User;
import service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AutoLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse reqp = (HttpServletResponse) servletResponse;
        //获得cookie信息
//        定义cookie_username与cookie_password
        String cookie_username = null;
        String cookie_password = null;
        Cookie[] cookies=req.getCookies();
        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                //获得cookie_username与cookie_password
                if ("cookie_username".equals(cookie.getName()) ){
                    cookie_username = cookie.getValue();
                }
                if ("cookie_password".equals(cookie.getName())){
                    cookie_password = cookie.getValue();
                }
            }
        }
        //判断username与password是否为空
        if(cookie_username!=null&&cookie_password!=null){
            //登录代码
            UserService service = new UserService();
            LoginUser user = null;
            try {
                user = service.login(cookie_username,cookie_password);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //将登录的用户的user对象存到session中
            HttpSession session = req.getSession();
            session.setAttribute("user",user);
        }
        //放行
        filterChain.doFilter(req,reqp);
    }

    @Override
    public void destroy() {

    }
}
