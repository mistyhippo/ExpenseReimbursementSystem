package com.revature.routes;
import com.revature.controllers.AuthController;
import io.javalin.Javalin;

import io.javalin.Javalin;
public class AuthRoutes extends Route {
    private AuthController authController;

    public AuthRoutes(AuthController authController){
        this.authController = authController;
    }

    @Override
    public void registerLocalRoutes(Javalin app) {
        app.post("/login", authController.login);
        app.get("/verify", authController.verify);
        app.get("/logout", authController.logout);
    }
}
