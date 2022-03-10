package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.ReimbursementCreateDTO;
import com.revature.dto.ReimbursementHistoryDTO;
import com.revature.dto.ReimbursementIdDTO;
import com.revature.dto.ReimbursementSpecificDTO;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementController {

    private ObjectMapper mapper = new ObjectMapper();
    private ReimbursementService reimbursementService;

    //Constructor
    public ReimbursementController(ReimbursementService reimbursementService){
        this.reimbursementService = reimbursementService;
    }

    public Handler handleCreateReimbursementRecord= (context) -> {
//        Reimbursement reimbursement = mapper.readValue(context.body(), Reimbursement.class);
        ReimbursementCreateDTO dto = mapper.readValue(context.body(),ReimbursementCreateDTO.class);
        System.out.println(dto);
        String idParam ="";
        try {
            System.out.println(context.req.getSession().getId());
            idParam = context.req.getSession().getAttribute("id").toString();
            System.out.println(context.req.getSession().getId());
            //System.out.println("role: " + context.req.getSession().getAttribute("role").toString());
            int userId = Integer.parseInt(idParam);

            System.out.println("userId : " + userId);
            dto.setUserId(userId);
            boolean success = reimbursementService.createReimbursementRecord(dto.getUserId(),dto.getManagerId(),dto.getTitle(),
                    dto.getDescription(),dto.getStatus(),dto.getSubmittedDate(),dto.getTransactionDate(),dto.getTypeId(),dto.getAmount());

            //Let's set the session to know that the person is logged in
            context.header("Access-Control-Allow-Origin", "http://localhost:8080");
            context.header("Access-Control-Allow-Credentials", "true");
            context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
            context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
            System.out.println(context.req.getSession().getId());
            // prepare response
            if(success){
                context.status(200);
                context.result("Success");
            }else{
                context.status(400);
                context.result("Failed");
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
    };

    public Handler handlerGetPendingReimbursement = (context) -> {
        List<ReimbursementHistoryDTO> pendingLists;
        String idParam ="";
//try{
            System.out.println(context.req.getSession().getId());
            idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
            System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
            int userId = Integer.parseInt(idParam);
        System.out.println("test");
            System.out.println("userId : " + userId);

            pendingLists = reimbursementService.getPendingReimbursement(userId);
            context.json(pendingLists);

            context.header("Access-Control-Allow-Origin", "http://localhost:8080");
            context.header("Access-Control-Allow-Credentials", "true");
            context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
            context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
            System.out.println(context.req.getSession().getId());
            // prepare response

//        } catch (NullPointerException e) {
//            e.getMessage();
//        }
    };

    public Handler handlerGetResolvedReimbursement = (context) -> {
        List<ReimbursementHistoryDTO> resolvedLists;
        String idParam ="";

        System.out.println(context.req.getSession().getId());
        idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
        System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
        int userId = Integer.parseInt(idParam);
        System.out.println("userId : " + userId);

        resolvedLists = reimbursementService.getResolvedReimbursement(userId);
        System.out.println("resolvedLists:  " + resolvedLists);
        context.json(resolvedLists);

        context.header("Access-Control-Allow-Origin", "http://localhost:8080");
        context.header("Access-Control-Allow-Credentials", "true");
        context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
        context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
        System.out.println(context.req.getSession().getId());

    };



//    ======================Manager Pages=====================


    public Handler handlerGetPendingReimbursementManager = (context) -> {
        List<ReimbursementHistoryDTO> managerPendingLists;
        String idParam ="";

        System.out.println(context.req.getSession().getId());
        idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
        System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
        int userId = Integer.parseInt(idParam);
        System.out.println("userId : " + userId);

        managerPendingLists = reimbursementService.getPendingReimbursementManager(userId);
        System.out.println("resolvedLists:  " + managerPendingLists);
        context.json(managerPendingLists);

        context.header("Access-Control-Allow-Origin", "http://localhost:8080");
        context.header("Access-Control-Allow-Credentials", "true");
        context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
        context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
        System.out.println(context.req.getSession().getId());
    };

    public Handler handlerGetResolvedReimbursementManager = (context) -> {
        List<ReimbursementHistoryDTO> managerResolvedLists;
        String idParam ="";

        System.out.println(context.req.getSession().getId());
        idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
        System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
        int userId = Integer.parseInt(idParam);
        System.out.println("userId : " + userId);

        managerResolvedLists = reimbursementService.getResolvedReimbursementManager(userId);
        System.out.println("resolvedLists:  " + managerResolvedLists);
        context.json(managerResolvedLists);

        context.header("Access-Control-Allow-Origin", "http://localhost:8080");
        context.header("Access-Control-Allow-Credentials", "true");
        context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
        context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
        System.out.println(context.req.getSession().getId());

    };

    public Handler handleUpdateApproveReimbursement = (context) -> {
        String idParam = "";
        System.out.println(context.req.getSession().getId());
        idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
        System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
        int managerId = Integer.parseInt(idParam);
        System.out.println("managerId : " + managerId);
        ReimbursementIdDTO dto = mapper.readValue(context.body(),ReimbursementIdDTO.class);
        System.out.println(dto);
        int reimbursementId = dto.getReimbursementId();
        boolean success = reimbursementService.approveReimbursement(reimbursementId);
        // prepare response
        if(success){
            context.status(200);
            context.result("Success");
        }else{
            context.status(400);
            context.result("Failed");
        }
    };

    public Handler handleUpdateDenyReimbursement = (context) -> {
        String idParam = "";
        System.out.println(context.req.getSession().getId());
        idParam = context.req.getSession().getAttribute("id").toString();
        System.out.println("test");
        System.out.println("context.req.getSession().getId(): " + context.req.getSession().getId());
        int managerId = Integer.parseInt(idParam);
        System.out.println("managerId : " + managerId);
        ReimbursementIdDTO dto = mapper.readValue(context.body(),ReimbursementIdDTO.class);
        System.out.println(dto);
        int reimbursementId = dto.getReimbursementId();
        boolean success = reimbursementService.denyReimbursement(reimbursementId);
        // prepare response
        if(success){
            context.status(200);
            context.result("Success");
        }else{
            context.status(400);
            context.result("Failed");
        }
    };

    public Handler handleViewSpecificReimbursement = (context) ->{
        List<ReimbursementHistoryDTO> specificReimbursementLists;
        int userId = Integer.parseInt(context.pathParam("userId"));
//        ReimbursementSpecificDTO dto = mapper.readValue(context.body(),ReimbursementSpecificDTO.class);
//        System.out.println(dto);
//        int userId = dto.getUserId();
        specificReimbursementLists = reimbursementService.viewSpecificReimbursement(userId);
        System.out.println("resolvedLists:  " + specificReimbursementLists);
        context.json(specificReimbursementLists);

        context.header("Access-Control-Allow-Origin", "http://localhost:8080");
        context.header("Access-Control-Allow-Credentials", "true");
        context.header("Access-Control-Allow-Headers", "Cookie,X-Next-Page");
        context.header("Access-Control-Expose-Headers", "Set-Cookie,X-Next-Page");
        System.out.println(context.req.getSession().getId());

    };
};
