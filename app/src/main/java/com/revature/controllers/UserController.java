package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.UserService;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserController {
//    public final UserService userService = new UserService();

//    public void handleGetAllGroupMember(Context ctx){
//        String idParam = ctx.pathParam("userId");
//        int managerId = Integer.parseInt(idParam);
//        List<User> groupMember= userService.getAllGroupMember(managerId);
//        ctx.json(groupMember);
//    }


    private ObjectMapper mapper = new ObjectMapper();
    private UserService userService;

    //Constructor
    public UserController(UserService userService){
        this.userService = userService;
    }

    //Method
    public Handler handleGetLoginUser = (context) ->{
        String idParam ="";
        try {
            System.out.println(context.req.getSession().getId());
            idParam = context.req.getSession().getAttribute("id").toString();
            //System.out.println("role: " + context.req.getSession().getAttribute("role").toString());
            int userId = Integer.parseInt(idParam);
            User user = userService.getLoginUser(userId);
            context.json(user);
            //} catch (NullPointerException e) {
//            idParam = context.pathParam("id");
            if (idParam == null) {
                context.status(403);
                context.result("Username is not login");
            }
        }catch (NullPointerException e){
            context.result("Username is not login");
        }
    };

    public Handler handleGetAllGroupMember = (context) -> {
        List<User> users = new ArrayList<>();
        String idParam ="";
        try {
            System.out.println("Id of getSession" + context.req.getSession().getId());
            idParam = context.req.getSession().getAttribute("id").toString();
            //System.out.println("role: " + context.req.getSession().getAttribute("role").toString());
            int userId = Integer.parseInt(idParam);
            if (idParam != null) {
                if (userId == 1 || userId == 2) {
                    users = userService.getAllGroupMember(userId);
                    context.json(users);
                } else {
                    context.result("You do not have right to access this page.");
                }
            }
        }catch (NullPointerException e) {
            context.result("you are not log in yet.");
        };

    };

    public Handler updateUserInfo = (context) -> {
        User updatedUser = mapper.readValue(context.body(), User.class);
        System.out.println(updatedUser);

        userService.updateUserInfo(updatedUser);
    };


    public Handler getAllEmployee = (context) -> {
        context.result(mapper.writeValueAsString(userService.getAllEmployee()));
    };

    public Handler getAllManager = (context) -> {
        context.result(mapper.writeValueAsString(userService.getAllManager()));
    };




//    public Handler getAllMemberManager1 = (context) -> {
//        context.result(mapper.writeValueAsString(userService.getAllMemberManager1()));
//    };

//    public Handler getAllMemberManager2 = (context) -> {
//        context.result(mapper.writeValueAsString(userService.getAllMemberManager2()));
//    };
//
//    public Handler createUser = (context) -> {
//        User user = mapper.readValue(context.body(), User.class);
//
//        System.out.println(user);
//
//        userService.createNewUser(user.getUserId(),user.getEmail(),user.getFirstName(), user.getLastName(), user.getRole(), user.getPassWord());
//
//        context.result(mapper.writeValueAsString(user));
//
//    };
}
