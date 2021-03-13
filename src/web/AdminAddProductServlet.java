package web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import domain.Category;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import domain.Product;
import service.AdminProductService;
import utils.BeanFactory;
import utils.CommonsUtils;

public class AdminAddProductServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		//目的：收集表单的数据 封装一个Product实体 将上传图片存到服务器磁盘上

		Product product = new Product();

		//收集数据的容器
		Map<String,Object> map = new HashMap<String,Object>();

		try {
			//创建磁盘文件项工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//创建文件上传核心对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			//解析request获得文件项对象集合

			List<FileItem> parseRequest = upload.parseRequest(request);
			for(FileItem item : parseRequest){
				//判断是否是普通表单项
				boolean formField = item.isFormField();
				if(formField){
					//普通表单项 获得表单的数据 封装到Product实体中
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");

					map.put(fieldName, fieldValue);

				}else{
					//文件上传项 获得文件名称 获得文件的内容
					String fileName = item.getName();
//					冗余代码
//					String path = this.getServletContext().getRealPath("upload");
//					System.out.println(path+fileName);
//					InputStream in = item.getInputStream();
//					OutputStream out = new FileOutputStream(path+"/"+fileName);//I:/xxx/xx/xxx/xxx.jpg
//					IOUtils.copy(in, out);
//					in.close();
//					out.close();
//					item.delete();

					map.put("pimage", "upload/"+fileName);

				}

			}

			BeanUtils.populate(product, map);
			//是否product对象封装数据完全
			//private String pid;
			product.setPid(CommonsUtils.getUid());
			//private Date pdate;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String pdate = format.format(new Date());
			product.setPdate(pdate);
			//private int pflag;
			product.setPflag(0);
			//private Category category;
			Category category = new Category();
			category.setCid(map.get("cid").toString());
			product.setCid(String.valueOf(category.getCid()));

			//将product传递给service层
			AdminProductService service=new AdminProductService();
//			AdminProductService service = (AdminProductService) BeanFactory.getBean("adminService");
			service.addProduct(product);





		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		没有pimage的上传数据
//		//1、获取数据
//		Map<String, String[]> properties = request.getParameterMap();
//		//2、封装数据
//		Product product = new Product();
//		try {
//			BeanUtils.populate(product, properties);
//		} catch (IllegalAccessException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//
//		//此位置Product已经封装完毕----将表单的数据封装完毕
//		//手动设置表单中没有数据
//		//1）、private String pid;
//		product.setPid(UUID.randomUUID().toString());
//		//2）、private String pimage;
//		product.setPimage("products/1/c_0033.jpg");
//		//3）、private String pdate;//上架日期
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String pdate = format.format(new Date());
//		product.setPdate(pdate);
//		//4）、private int pflag;//商品是否下载 0代表未下架
//		product.setPflag(0);
//
//		//3、传递数据给service层
//		AdminProductService service = new AdminProductService();
//		try {
//			service.addProduct(product);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
		
		//跳转到列表页面
		response.sendRedirect(request.getContextPath()+"/adminProductList");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}