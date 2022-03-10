package com.revature.routes;

import com.revature.controllers.ReimbursementController;
import com.revature.controllers.UserController;
import com.revature.loggingwith4j.LoggingUtil;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class ReimbursementRoutes extends Route{

    private ReimbursementController reimbursementController;
   // private LoggingUtil loggingUtil = new LoggingUtil();

    public ReimbursementRoutes(ReimbursementController reimbursementController){
        this.reimbursementController = reimbursementController;
    }

    @Override
    public void registerLocalRoutes(Javalin app) {
        app.post("/reimbursement", reimbursementController.handleCreateReimbursementRecord);
        app.get("/reimbursement/pending",reimbursementController.handlerGetPendingReimbursement);
        app.get("/reimbursement/resolved",reimbursementController.handlerGetResolvedReimbursement);

        app.get("/reimbursement/pending/users",reimbursementController.handlerGetPendingReimbursementManager);
        app.get("/reimbursement/resolved/users",reimbursementController.handlerGetResolvedReimbursementManager);

        app.put("/reimbursement/approve",reimbursementController.handleUpdateApproveReimbursement);
        app.put("/reimbursement/deny",reimbursementController.handleUpdateDenyReimbursement);
        app.get("/reimbursement/employee/{userId}",reimbursementController.handleViewSpecificReimbursement);
        //ApiBuilder.before("*",loggingUtil::logRequest);

    }
}
