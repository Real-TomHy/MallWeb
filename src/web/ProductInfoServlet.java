package web;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import domain.Product;
import service.ProductService;





public class ProductInfoServlet extends HttpServlet {  //需要继承HttpServlet  并重写doGet  doPost方法

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);  //将信息使用doPost方法执行   对应jsp页面中的form表单中的method
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得当前页
        String currentPage = request.getParameter("currentPage");
        //获得商品类别
        String cid = request.getParameter("cid");
        //获得要查询商品的pid
        String pid = request.getParameter("pid");

        ProductService service = new ProductService();
        Product product=service.findProductByPid(pid);
        request.setAttribute("product",product);
        request.setAttribute("currentPage",currentPage);
        request.setAttribute("cid",cid);
        //获得客户端携带的cookie--名字是pids的cookie
        Cookie[] cookies=request.getCookies();
        String pids = pid;
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if("pids".equals(cookie.getName())){
                    pids=cookie.getValue();
                    //将pids拆成一个数组
                    String[] split=pids.split("-");
                    List<String> aList =Arrays.asList(split);
                    LinkedList<String> list = new LinkedList<String>(aList);
                    //判断集合中是否存在当前pid
                    if(list.contains(pid)){
                        //包含当前查看商品的pid
                        list.remove(pid);
                        list.addFirst(pid);
                    }else{
                        //不包含当前查看商品的pid 直接将该pid放到头上
                        list.addFirst(pid);
                    }
                    //将[3,1,2]转成3-1-2字符串
                    StringBuffer sb = new StringBuffer();
                    for(int i=0;i<list.size()&&i<7;i++){
                        sb.append(list.get(i));
                        sb.append("-");//3-1-2-
                    }
                    //去掉3-1-2-后的-
                    pids = sb.substring(0, sb.length()-1);
                }
            }
        }
        //第一次访问时,在转发之前，需要创建cookie存储pid
        Cookie cookie_pids = new Cookie("pids",pids);
        response.addCookie(cookie_pids);
        request.getRequestDispatcher("/product_info.jsp").forward(request,response);


    }
}

