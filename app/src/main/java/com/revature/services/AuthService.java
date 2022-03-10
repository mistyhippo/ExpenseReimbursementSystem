package com.revature.services;

import com.revature.dao.UserDao;
import com.revature.models.User;

import java.util.List;

public class AuthService {
    private UserDao userDao;

    //Constructor
    public AuthService(UserDao userDao){
        this.userDao = userDao;
    }

    //Methods
    public boolean loginUser(String email, String password){
        //List<User> userList = userDao.getAllUser();
        User login = userDao.readUserByEmail(email);

        if(login == null || !login.getPassWord().equals(password)){
            return false;
        }
        return true;
    }

}
