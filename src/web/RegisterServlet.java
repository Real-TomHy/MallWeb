package web;

import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import service.UserService;
import utils.CommonsUtils;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import utils.MailUtils;
public class RegisterServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得表单数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            //String转Date
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object value) {
                    //将string转成Date
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse =null;
                    try {
                        parse = format.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            //映射封装
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
//        private String uid;
        user.setUid(CommonsUtils.getUid());
//        private String telephone;
        user.setTelephone(CommonsUtils.getTelephone());
//        private int state;//是否激活
        user.setState(0);
//        private String code;//验证码
//        user.setCode(CommonsUtils.getUid());
        String activeCode = CommonsUtils.getUid();
        user.setCode(activeCode);
        //将user传递给service层
        UserService service = new UserService();
        boolean isRegisterSuccess=service.register(user);
        //判断是否注册成功
        if(isRegisterSuccess){
            //注册成功
            //发送激活邮件
            String message = "注册成功,注意激活!"+ "<a>链接</a>"
                    + "http://localhost:8080/MallWeb_war_exploded/active?activeCode="+activeCode;
            try {
                MailUtils.sendMail(user.getEmail(),message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //重定向
            response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
        }
        else {
            //注册失败
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }
}
