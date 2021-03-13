package service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CategoryDao;
import domain.Category;

import vo.PageBean;

public class CategoryService {
    public List<Category> findAllCategory() {
        CategoryDao dao = new CategoryDao();
        List<Category> categoryList= null;
        try {
            categoryList = dao.findAllCategory();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categoryList;
    }
}
