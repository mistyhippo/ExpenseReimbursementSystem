package com.revature.dao;

import com.revature.models.Reimbursement;
import com.revature.models.User;

import java.util.List;

public interface UserDao {

    public User getLoginUser(int id);
    public List<User> getAllGroupMember(int id);
    public User readUserByEmail(String email);
    public void updateUserInfo(User user);
    public List<User> getAllUser();


    public List<User> getAllEmployee();
    public List<User> getALlManager();

    //public List<User> getAllMemberManager1();
//    public List<User> getAllMemberManager2();
//    public void createUser(User user);



}
