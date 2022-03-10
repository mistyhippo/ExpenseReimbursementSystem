package com.revature.controllers;

import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.UUID;

public class AuthController {

    private AuthService authService; //loginUser() check user login is true of false
    private UserService userService; // getUserByEmail() return login user object
    private ObjectMapper mapper = new ObjectMapper();

    //Conrtructors
    public AuthController() {}

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }


    //Methods
    public Handler login = (context) -> {
        LoginObject loginObject = mapper.readValue(context.body(),LoginObject.class);
        System.out.println(loginObject.email + " , " + loginObject.password);
    if (!authService.loginUser(loginObject.email, loginObject.password)) {
        context.status(403);
        context.result("Username or password is incorrect.");
        System.out.println("Username or password is incorrect.");
        return;
    }

    User user = userService.getUserByEmail(loginObject.email);

    //Let's set the session to know that the person is logged in
    context.req.getSession().setAttribute("id", "" + user.getUserId());
    context.req.getSession().setAttribute("loggedIn", user.getEmail());
    context.result(mapper.writeValueAsString(user));
    context.header("Access-Control-Allow-Origin", "http://localhost:8080");
    context.header("Access-Control-Allow-Credentials", "true");
    context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
    context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
    System.out.println(context.req.getSession().getId());
    if (user.getUserId() == 1 || user.getUserId() == 2) {
        context.header("X-Next-Page", "http://localhost:8080/html/manager_index.html?userId=" + user.getUserId());

    } else {
        context.header("X-Next-Page", "http://localhost:8080/html/employee_index.html?userId=" + user.getUserId());

    }
    System.out.println(user.getUserId());
    context.status(200);
        //context.header("Set-Cookie", UUID.randomUUID().toString());
    };

    public Handler verify = (context) -> {
        context.header("Access-Control-Expose_Headers", "*");
        System.out.println(context.req.getSession().getAttribute("id"));

        if(context.req.getSession().getAttribute("id") == null){
            context.status(403);
            context.result("User not logged in.");
        }else{
            context.header("userId", "" + context.req.getSession().getAttribute("id"));
            context.result("User was verified as logged in.");
        }
    };

    public Handler logout = (context) -> {
        context.req.getSession().invalidate();
        context.status(200);
        context.result("User logged out");
    };

}

class LoginObject{
    public String email;
    public String password;

//    private AuthService authService; //loginUser() check user login is true of false
//    private UserService userService; // getUserByEmail() return login user object



//    public void handleLogin(Context ctx) {
//        LoginObject loginObject = ctx.bodyAsClass(LoginObject.class);
//
//        if (!authService.loginUser(loginObject.email, loginObject.password)) {
//            ctx.status(403);
//            ctx.result("Username or password is incorrect.");
//        }
//
//        User user = userService.getUserByEmail(loginObject.email);
//        //Let's set the session to know that the person is logged in
//        ctx.req.getSession().setAttribute("id", "" + user.getUserId());
//        ctx.req.getSession().setAttribute("loggedIn", user.getEmail());
//        ctx.json(user);
//        //ctx.result(mapper.writeValueAsString(user));
//        ctx.header("Access-Control-Allow-Origin", "*");
//
//        if (user.getUserId() == 1 || user.getUserId() == 2) {
//            ctx.redirect("http://localhost:8080/html/manager_index.html?userId=" + user.getUserId());
//            System.out.println(user.getUserId());
//        } else {
//            ctx.redirect("http://localhost:8080/html/employee_index.html?userId=" + user.getUserId());
//            System.out.println(user.getUserId());
//        }
//        ctx.json(user);
//    }
//
//    public void handleVerify(Context ctx) {
//        ctx.header("Access-Control-Expose_Headers", "*");
//        System.out.println(ctx.req.getSession().getAttribute("id"));
//
//        if (ctx.req.getSession().getAttribute("id") == null) {
//            ctx.status(403);
//            ctx.result("User not logged in.");
//        } else {
//            ctx.header("userId", "" + ctx.req.getSession().getAttribute("id"));
//            ctx.result("User was verified as logged in.");
//        }
//    }
//
//    public void handleLogout(Context ctx) {
//        ctx.req.getSession().invalidate();
//        ctx.status(200);
//        ctx.result("User logged out");
//    }


  }