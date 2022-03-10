package com.revature.dao;

import com.revature.dto.ReimbursementHistoryDTO;
import com.revature.models.Reimbursement;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ReimbursementDao {
    public boolean createReimbursementRecord(int userId, int managerId, String tittle, String description, int status,
                                             Timestamp submittedDate, Timestamp transactionDate, int typeId, double amount) throws SQLException;


    public List<ReimbursementHistoryDTO> getPendingReimbursement(int userId) throws SQLException;

    public List<ReimbursementHistoryDTO>  getResolvedReimbursement(int userId) throws SQLException;

    public List<ReimbursementHistoryDTO>  getResolvedReimbursementManager(int userId) throws SQLException;

    public List<ReimbursementHistoryDTO>  getPendingReimbursementManager(int userId) throws SQLException;

    public boolean approveReimbursement(int reimbursementId) throws SQLException;
    public boolean denyReimbursement(int reimbursementId) throws SQLException;

    public List<ReimbursementHistoryDTO> viewSpecificReimbursement(int userId) throws SQLException;

}
