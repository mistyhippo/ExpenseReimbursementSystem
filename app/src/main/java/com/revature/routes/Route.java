package com.revature.routes;

import com.revature.loggingwith4j.LoggingUtil;
import io.javalin.Javalin;

public abstract class Route {

    public static void establishRoutes(Javalin app, Route ... routes){

        for(int i=0; i<routes.length; i++){
            Route r = routes[i];
            r.registerLocalRoutes(app);
        }
    }


    abstract public void registerLocalRoutes(Javalin app);

}
