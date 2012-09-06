package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.RowHandler;
import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.User;
import com.sinosoft.one.data.jade.model.User1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * User: Chunliang.Han
 * Time: 12-9-5[上午10:55]
 */
public interface UserSelectDao extends UserDao {
    //4.1.1
    User selectUserWithSqlQueryById(String id);

    //4.1.2
    @SQL("select name from t_user where id= ?1")
    String selectUserNameWithAnnoById(String id);

    //4.1.3
    int selectUserAgeWithSqlQueryById(String id);

    //4.1.4
    @SQL("select age from t_user where id= ?1")
    Integer selectUserAgeWithAnnoById(String id);

    //4.1.5
    Date selectUserBirthdayWithSqlQueryById(String id);

    //4.1.6
    @SQL("select money from t_user where id= ?1")
    Long selectUserMoneyWithAnnoById(String id);

    //4.1.7
    long selectUserMoneyWithSqlQUeryById(String id);

    //4.2.1
    @SQL("select * from t_user where groupIds in (?1)")
    List<User> selectUserWithAnnoByGroupid(Set<String> groups);

    //4.2.2
    @SQL("select * from t_user where groupIds in (?1)")
    List<User> selectUserWithAnnoByGroupid(List<String> groups);

    //4.2.3
    @SQL("select * from t_user where groupIds in (?1)")
    List<User> selectUserWithAnnoByGroupid(String[] groups);

    //4.2.4
    @SQL("select * from t_user where id = :1[id] and name = :1[name]")
    User selectUserWithAnnoByIdAndName(Map<String,String> idAndName);

    //4.2.5
    @SQL("select * from t_user where id = :1[id] and name = :1[name]")
    List<User> selectUsersWithAnnoByIdAndName(Map<String,String> idAndName);

    //4.2.6
    @SQL("select u.id id,u.name from t_user u,t_code_group g where u.groupIds = g.id(+) and g.name = :1[gName] and u.birthday = :1[userBirthday]")
    List<User> selectUsersWithAnnoByGnameAndUbirthday(Object[] params);

    //4.2.7
    @SQL("select u.id id,u.name from t_user u,t_code_group g where u.groupIds = g.id(+) and g.name = :1[gName] and u.birthday = :1[userBirthday]")
    User selectUserWithAnnoByGnameAndUbirthday(Object[] params);

    //4.3.1
    Page selectUsersWithSqlQueryForPage(Pageable pageable);

    //4.3.2
    @SQL("select id, name from t_user where groupIds = ?1")
    @RowHandler(rowMapper = UserRowMapper.class)
    List<User1> selectUsersWithAnnoByGroupid(String groupId);
}
class UserRowMapper implements RowMapper {

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<User1> users = new ArrayList<User1>();
        while(rs.next()){
            User1 user = new User1();
            user.setId(rs.getString(1));
            user.setName(rs.getString(2));
            users.add(user);
            rowNum++;
        }
        return users ;
    }
}