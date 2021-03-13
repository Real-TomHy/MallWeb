package web.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");//POST不用在解码,GET需要
        filterChain.doFilter(servletRequest,servletResponse);

//
//        //GET:将getParameter方法增强
//        //使用装饰者模式(包装) 进行增强
//        /*
//        1.增强类与被增强类要实现统一接口
//        2.在增强类中传入被增强的类
//        3.需增强的方法重写 不需要增强的方法调用被增强对象的
//         */
//        //被增强的对象
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        //增强对象
//        EnhanceRequest enhanceRequest = new EnhanceRequest(req);
//
//        filterChain.doFilter(enhanceRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
//class EnhanceRequest extends HttpServletRequestWrapper {
//    private HttpServletRequest req;
//
//    public EnhanceRequest(HttpServletRequest request) {
//        super(request);
//        this.req = request;
//    }
//    //对getParameter增强
//    @Override
//    public String getParameter(String name){
//        String parameter = null;
//        parameter = req.getParameter(name);//乱码
//        try {
//            parameter = new String(parameter.getBytes("iso8859-1"), StandardCharsets.UTF_8);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return parameter;
//    }
//
//}