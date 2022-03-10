package com.revature.dao;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBC implements UserDao {
    ConnectionUtil connectionUtil = ConnectionUtil.getInstance();

    @Override
    public User getLoginUser(int id){
        User user = new User();
        try{
            Connection con = connectionUtil.getConnection();
            String sql = "select * from ers_users where user_id = ?;" ;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userId = rs.getInt("user_id");
                user.setUserId(userId);

                String email = rs.getString("email");
                user.setEmail(email);

                String first_name = rs.getString("first_name");
                user.setFirstName(first_name);

                String last_name = rs.getString("last_name");
                user.setLastName(last_name);

                Role role = Role.values()[rs.getInt("role")-1];
                user.setRole(role);

                String passWord = rs.getString("password");
                user.setPassWord(passWord);
            }
            //ps.setInt(1, 1);
        }catch(SQLException e){
            e.printStackTrace();
         }
        return user;
    }

    @Override
    public List<User> getAllGroupMember(int id){
        List<User> userList = new ArrayList<>();

        try{
            Connection con = connectionUtil.getConnection();
            String sql = "select * \n" +
                    "from ers_users\n" +
                    "join ers_employee_manager\n" +
                    "on ers_users.user_id = ers_employee_manager .employee_id\n" +
                    "where ers_employee_manager.manager_id = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                int userId = rs.getInt("user_id");
                user.setUserId(userId);

                String email = rs.getString("email");
                user.setEmail(email);

                String first_name = rs.getString("first_name");
                user.setFirstName(first_name);

                String last_name = rs.getString("last_name");
                user.setLastName(last_name);

                Role role = Role.values()[rs.getInt("role")-1];
                user.setRole(role);

                String passWord = rs.getString("password");
                user.setPassWord(passWord);

                userList.add(user);
            }
            //ps.setInt(1, 1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
    };

    @Override
    public void updateUserInfo(User user){

        try{
            System.out.println("updateUserInfo: "+user.getUserId());
            Connection con = connectionUtil.getConnection();

            String sql = "update ers_users set user_id =?, email=?, first_name=?, last_name=?, role=?,password=? where user_id = ? RETURNING user_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,user.getUserId());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getFirstName());
            ps.setString(4,user.getLastName());
            ps.setInt(5,user.getRole().ordinal()+1);
            ps.setString(6,user.getPassWord());
            ps.setInt(7,user.getUserId());

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("updateUserInfo-> UserID : " + rs.getInt(1));
            }



        } catch(SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public List<User> getAllEmployee(){
        List<User> userList = new ArrayList<>();

        try{
            Connection con = connectionUtil.getConnection();
            String sql = "SELECT * FROM ers_users where role=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                int id = rs.getInt("user_id");
                user.setUserId(id);

                String email = rs.getString("email");
                user.setEmail(email);

                String first_name = rs.getString("first_name");
                user.setFirstName(first_name);

                String last_name = rs.getString("last_name");
                user.setLastName(last_name);

                Role role = Role.values()[rs.getInt("role")-1];
                user.setRole(role);

                String passWord = rs.getString("password");
                user.setPassWord(passWord);

                userList.add(user);
            }
            //ps.setInt(1, 1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
    }


    @Override
    public List<User> getALlManager(){
        List<User> userList = new ArrayList<>();

        try{
            Connection con = connectionUtil.getConnection();
            String sql = "SELECT * FROM ers_users where role=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, 2);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User user = new User();
                int id = rs.getInt("user_id");
                user.setUserId(id);

                String email = rs.getString("email");
                user.setEmail(email);

                String first_name = rs.getString("first_name");
                user.setFirstName(first_name);

                String last_name = rs.getString("last_name");
                user.setLastName(last_name);

                Role role = Role.values()[rs.getInt("role")-1];
                user.setRole(role);

                String passWord = rs.getString("password");
                user.setPassWord(passWord);

                userList.add(user);
            }
            //ps.setInt(1, 1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
    };

    @Override
    public User readUserByEmail(String email) {
        User user = null;
        try{
            Connection con = connectionUtil.getConnection();

            String sql = "SELECT * FROM ers_users WHERE email=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                user = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                        Role.values()[rs.getInt(5)-1],rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public List<User> getAllUser(){
        List<User> users = new ArrayList<>();

        try{
            Connection con = connectionUtil.getConnection();
            String sql = "SELECT email, password FROM ers_users";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                String email = rs.getString("email");
                user.setEmail(email);
                String password = rs.getString("password");
                user.setPassWord(password);
                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

//    @Override
//    public List<User> getAllMemberManager2(){
//        List<User> userList = new ArrayList<>();
//
//        try{
//            Connection con = connectionUtil.getConnection();
//            String sql = "select * \n" +
//                    "from ers_users\n" +
//                    "join ers_employee_manager\n" +
//                    "on ers_users.user_id = ers_employee_manager .employee_id\n" +
//                    "where ers_employee_manager.manager_id = ?;";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, 2);
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()){
//                User user = new User();
//                int id = rs.getInt("user_id");
//                user.setUserId(id);
//
//                String email = rs.getString("email");
//                user.setEmail(email);
//
//                String first_name = rs.getString("first_name");
//                user.setFirstName(first_name);
//
//                String last_name = rs.getString("last_name");
//                user.setLastName(last_name);
//
//                Role role = Role.values()[rs.getInt("role")-1];
//                user.setRole(role);
//
//                String passWord = rs.getString("password");
//                user.setPassWord(passWord);
//
//                userList.add(user);
//            }
//            //ps.setInt(1, 1);
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return userList;
//    };
//
//
//
//    @Override
//    public void createUser(User user){
//        try{
//            Connection con = connectionUtil.getConnection();
//            String sql = "INSERT INTO ers_users VALUES('" + user.getEmail() + "','" +user.getFirstName() + "','" + user.getLastName() + "','" + user.getRole() + "','" + user.getPassWord() + "')";
//            System.out.println(sql);
//
//            Statement statement = con.createStatement();
//            statement.executeUpdate(sql);
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void createReimbursement(Reimbursement reimbursement){
//
//    }
}
