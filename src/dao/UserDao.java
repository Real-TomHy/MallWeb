package dao;

import domain.NewUser;
import domain.User;
import domain.LoginUser;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.DataSourceUtils;

import java.sql.SQLException;

public class UserDao {
    public Long checkUsername(String username) {
        QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select count(*) from user where username=?";
        try {
            Long query = (Long) runner.query(sql,new ScalarHandler(),username);
            return query;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    public LoginUser login(String username, String password) throws SQLException{
        QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from user where username =? and password =?";
//        return runner.query(sql,new BeanHandler<User>(User.class),username,password);
        return runner.query(sql,new BeanHandler<LoginUser>(LoginUser.class),username,password);
    }

    public int register(User user) throws SQLException{
        QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        int update=runner.update(sql,user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
        return update;
    }

    public void active(String activeCode) throws SQLException{
        QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update user set state =? where code =?";
        runner.update(sql,1,activeCode);
    }

    public NewUser newlogin(String username, String password) throws SQLException{
        QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from user where username =? and password =?";
//        return runner.query(sql,new BeanHandler<User>(User.class),username,password);
        return runner.query(sql,new BeanHandler<NewUser>(NewUser.class),username,password);
    }
}
