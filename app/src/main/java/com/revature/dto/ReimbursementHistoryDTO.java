package com.revature.dto;

import com.revature.models.Reimbursement;

import java.sql.Date;
import java.sql.Timestamp;

public class ReimbursementHistoryDTO {
    private int userId;
//    Reimbursement reimbursement;
    private int reimbursementId;
    private String firstName;
    private String lastName;
    private String status;
    private Timestamp submittedDate;
    private String title;
    private String reimbursementType;
    private double amount;
    private Timestamp transactionDate;
    private Timestamp approvedDate;


    public ReimbursementHistoryDTO (){};
    public ReimbursementHistoryDTO (int userId,int reimbursementId,String firstName,String lastName,String status,
                                    Timestamp submittedDate,String title,String reimbursementType,double amount,Timestamp transactionDate,Timestamp approvedDate){
        this.userId = userId;
        this.reimbursementId = reimbursementId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.submittedDate = submittedDate;
        this.title = title;
        this.reimbursementType = reimbursementType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.approvedDate = approvedDate;

    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(int reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReimbursementType() {
        return reimbursementType;
    }

    public void setReimbursementType(String reimbursementType) {
        this.reimbursementType = reimbursementType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Timestamp getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }
}
