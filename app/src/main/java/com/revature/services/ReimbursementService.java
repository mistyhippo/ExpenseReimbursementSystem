package com.revature.services;

import com.revature.dao.ReimbursementDao;
import com.revature.dto.ReimbursementHistoryDTO;
import com.revature.models.Reimbursement;

import javax.swing.plaf.PanelUI;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ReimbursementService {
    ReimbursementDao reimbursementDao;

    public ReimbursementService(ReimbursementDao reimbursementDao) {
        this.reimbursementDao = reimbursementDao;
    }

    public boolean createReimbursementRecord(int userId, int managerId, String tittle, String description, int status,
                                             Timestamp submittedDate, Timestamp transactionDate, int typeId, double amount) throws SQLException {
        return reimbursementDao.createReimbursementRecord(userId,managerId,tittle,description,status,submittedDate,transactionDate,typeId,amount);
    }

    public List<ReimbursementHistoryDTO> getPendingReimbursement(int userId) throws SQLException {
        return reimbursementDao.getPendingReimbursement(userId);
    }

    public List<ReimbursementHistoryDTO> getResolvedReimbursement(int userId) throws SQLException {
        return reimbursementDao.getResolvedReimbursement(userId);
    }

    public List<ReimbursementHistoryDTO> getResolvedReimbursementManager(int userId) throws SQLException {
        return reimbursementDao.getResolvedReimbursementManager(userId);
    }

    public List<ReimbursementHistoryDTO> getPendingReimbursementManager(int userId) throws SQLException {
        return reimbursementDao.getPendingReimbursementManager(userId);
    }
    public boolean approveReimbursement(int reimbursementId)  throws SQLException{
        return reimbursementDao.approveReimbursement(reimbursementId);
    }

    public boolean denyReimbursement(int reimbursementId)  throws SQLException{
        return reimbursementDao.denyReimbursement(reimbursementId);
    }

    public List<ReimbursementHistoryDTO> viewSpecificReimbursement(int userId)  throws SQLException{
        return reimbursementDao.viewSpecificReimbursement(userId);
    }



}
