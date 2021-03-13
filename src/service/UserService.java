package service;

import dao.UserDao;
import domain.NewUser;
import domain.User;
import domain.LoginUser;

import java.sql.SQLException;

public class UserService {

    public boolean checkUsername(String username) {
        UserDao dao = new UserDao();
        Long isExit=dao.checkUsername(username);
        return isExit>0?true:false;
    }


    public LoginUser login(String username, String password) throws SQLException{
        UserDao dao = new UserDao();
        return dao.login(username,password);
        
    }

    public boolean register(User user) {
        UserDao dao = new UserDao();
        int row = 0;
        try {
            row = dao.register(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  row>0?true:false;
    }
        //用户激活
    public void active(String activeCode) {
        UserDao dao =new UserDao();
        try {
            dao.active(activeCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public NewUser newlogin(String username, String password) throws SQLException{
        UserDao dao = new UserDao();
        return dao.newlogin(username,password);
    }
}
