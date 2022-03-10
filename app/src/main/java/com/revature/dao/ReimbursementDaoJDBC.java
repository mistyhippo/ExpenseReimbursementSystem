package com.revature.dao;

import com.revature.dto.ReimbursementHistoryDTO;
import com.revature.models.Reimbursement;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDaoJDBC implements ReimbursementDao{
    java.sql.Timestamp  sqlDate;

    private String pattern = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    ConnectionUtil connectionUtil = ConnectionUtil.getInstance();

    @Override
    public boolean createReimbursementRecord(int userId, int managerId, String tittle, String description, int status,
                                             Timestamp submittedDate, Timestamp transactionDate,int typeId, double amount) throws SQLException {
        String sql = "insert into ers_reimbursement(user_id, date_submitted,status_id,manager_id,date_approved_denied,title,description,type_id ,cost,transaction_date) \n" +
                "values(?,?,?,?,?,?,?,?,?,?) RETURNING reimbursement_id";

        //Because default of autoCommit is trueConnection
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            if(userId < 7){
                managerId = 1;
            }else{
                managerId = 2;
            }
            PreparedStatement ps = con.prepareStatement(sql);
            //Set type as param for all indexes
            ps.setInt(1, userId);
            ps.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
            ps.setInt(3,2);
            ps.setInt(4, managerId);
            ps.setTimestamp(5,null);
            ps.setString(6, tittle);
            ps.setString(7, description);
            ps.setInt(8,typeId);
            ps.setDouble(9,amount);
            ps.setTimestamp(10,transactionDate);

            ResultSet rs = ps.executeQuery();

            int returnReimbursementId = 0;
            if (rs.next()) {
                returnReimbursementId = rs.getInt(1);

                System.out.println("reimbursementId: " + returnReimbursementId);

                //Second statement
                //Prepare for another statement set value into ers_reimbursement_history
                String sql2 = "insert into ers_reimbursement_history (user_id,reimbursement_id," +
                        "create_date,date_submitted,status_id,date_approved_denied)\n" +
                        "values(?,?,null,?,?,?);";
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, userId);
                ps2.setInt(2, returnReimbursementId);

                ps2.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
                ps2.setInt(4,2);
                ps2.setTimestamp(5,null);

                ps2.execute();

            }

            if (returnReimbursementId != 0) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }
        }finally{
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }

    @Override
    public List<ReimbursementHistoryDTO> getPendingReimbursement(int userId) throws SQLException {
        List<ReimbursementHistoryDTO> pendingLists = new ArrayList<>();
        ReimbursementHistoryDTO pendingItem;
        String firstName ="";
        String lastName ="";
        //String reimbursementType = "TRAVEL";
        int typeId = 0;
        int reimbursementId = 0;
        int userID = 0;
        Timestamp submittedDate = null;
        String status = "PENDING";
        String title = "";
        double amount = 0;
        Timestamp transactionDate = null;
        Timestamp resolvedDate = null;
        String reimbursementType = "FOOD";

        String sql1 = "select first_name, last_name from ers_users where user_id = ?;";
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, userId);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                firstName = rs1.getString(1);
                lastName = rs1.getString(2);
            }

            //Second statement
            String sql2 = "select reimbursement_id,user_id,date_submitted,date_approved_denied,title," +
                    "type_id,cost,transaction_date " +
                    "from ers_reimbursement where user_id = ? and status_id = 2";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, userId);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                reimbursementId = rs2.getInt(1);
                System.out.println("reimbursementId: "+ reimbursementId);

                userID = rs2.getInt(2);
                submittedDate = rs2.getTimestamp(3);
                title = rs2.getString(5);
                typeId = rs2.getInt(6);
                amount = rs2.getDouble(7);
                transactionDate = rs2.getTimestamp(8);
                resolvedDate = rs2.getTimestamp(4);


//                String sql3 = "select reimbursement_type from ers_reimbursement_type where reimbursement_type_id = ?";
//                PreparedStatement ps3 = con.prepareStatement(sql3);
//                ps3.setInt(1,typeId);
//                ResultSet rs3 = ps3.executeQuery();
//                while (rs3.next()) {
                switch (typeId){
                    case 1:
                        reimbursementType = "LODGING";
                        break;
                    case 2:
                        reimbursementType = "TRAVEL";
                        break;
                    case 3:
                        reimbursementType = "FOOD";
                        break;
                    case 4:
                        reimbursementType = "OTHER";
                        break;
                }
                System.out.println("reimbursementType: "+ reimbursementType);
                pendingItem = new ReimbursementHistoryDTO(userId, reimbursementId, firstName, lastName, status, submittedDate,
                        title, reimbursementType, amount, transactionDate, resolvedDate);
                pendingLists.add(pendingItem);
                //}
                System.out.println("reimbursementType2: "+ reimbursementType);
            }
           con.commit();

        } finally {
            if (con != null && !con.isClosed())
                con.close();
        }
        return pendingLists;
    };

    @Override
    public List<ReimbursementHistoryDTO>  getResolvedReimbursement(int userId) throws SQLException{
        List<ReimbursementHistoryDTO> resolvedLists = new ArrayList<>();
        ReimbursementHistoryDTO resolvedList;
        String firstName ="";
        String lastName ="";
        int typeId = 0;
        int reimbursementId = 0;
        int userID = 0;
        Timestamp submittedDate = null;
        String status = "";
        String title = "";
        double amount = 0;
        Timestamp transactionDate = null;
        Timestamp resolvedDate = null;
        String reimbursementType = "";

        String sql1 = "select first_name, last_name from ers_users where user_id = ?;";
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, userId);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                firstName = rs1.getString(1);
                lastName = rs1.getString(2);
            }

            //Second statement
            String sql2 = "select reimbursement_id,user_id,date_submitted,status_id,date_approved_denied,title," +
                    "type_id,cost,transaction_date " +
                    "from ers_reimbursement where user_id = ? and status_id = 3 or user_id = ? and status_id = 4";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, userId);
            ps2.setInt(2, userId);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                reimbursementId = rs2.getInt(1);
                System.out.println("reimbursementId: "+ reimbursementId);
                userID = rs2.getInt(2);
                submittedDate = rs2.getTimestamp(3);
                int statusId = rs2.getInt(4);
                title = rs2.getString(6);
                typeId = rs2.getInt(7);
                amount = rs2.getDouble(8);
                transactionDate = rs2.getTimestamp(9);
                resolvedDate = rs2.getTimestamp(5);
                switch (statusId){
                    case 3:
                        status = "APPROVED";
                        break;
                    case 4:
                        status = "DENIED";
                        break;
                }

                switch (typeId){
                    case 1:
                        reimbursementType = "LODGING";
                        break;
                    case 2:
                        reimbursementType = "TRAVEL";
                        break;
                    case 3:
                        reimbursementType = "FOOD";
                        break;
                    case 4:
                        reimbursementType = "OTHER";
                        break;
                }
                System.out.println("reimbursementType: "+ reimbursementType);
                resolvedList = new ReimbursementHistoryDTO(userId, reimbursementId, firstName, lastName, status, submittedDate,
                        title, reimbursementType, amount, transactionDate, resolvedDate);
                resolvedLists.add(resolvedList);
                //}
                System.out.println("reimbursementType2: "+ reimbursementType);
            }
            con.commit();

        } finally {
            if (con != null && !con.isClosed())
                con.close();
        }
        return resolvedLists;
    }

//    ===================================Manager Pages=======================

    @Override

    public List<ReimbursementHistoryDTO>  getPendingReimbursementManager(int managerId) throws SQLException{
        System.out.println("Hellllooooooo-------");
        List<ReimbursementHistoryDTO> managerPendingLists = new ArrayList<>();
        ReimbursementHistoryDTO managerPendingList;
        String firstName ="";
        String lastName ="";
        int typeId = 0;
        int reimbursementId = 0;
        int userId = 0;
        Timestamp submittedDate = null;
        int statusId = 0;

        String title = "";
        double amount = 0;
        Timestamp transactionDate = null;
        Timestamp resolvedDate = null;
        String reimbursementType = "";


        //_------------connect database------
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            String sql2 = "select reimbursement_id,user_id,date_submitted,status_id,date_approved_denied,title," +
                    "type_id,cost,transaction_date " +
                    "from ers_reimbursement where manager_id = ? and status_id = 2";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, managerId);

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                reimbursementId = rs2.getInt(1);
                System.out.println("reimbursementId: "+ reimbursementId);
                userId = rs2.getInt(2);
                submittedDate = rs2.getTimestamp(3);
                statusId = rs2.getInt(4);
                title = rs2.getString(6);
                typeId = rs2.getInt(7);
                amount = rs2.getDouble(8);
                transactionDate = rs2.getTimestamp(9);
                resolvedDate = rs2.getTimestamp(5);
                String status = "";
                switch (statusId){
                    case 2:
                        status = "PENDING";
                        break;
                    case 3:
                        status = "APPROVED";
                        break;
                    case 4:
                        status = "DENIED";
                        break;
                }

                switch (typeId){
                    case 1:
                        reimbursementType = "LODGING";
                        break;
                    case 2:
                        reimbursementType = "TRAVEL";
                        break;
                    case 3:
                        reimbursementType = "FOOD";
                        break;
                    case 4:
                        reimbursementType = "OTHER";
                        break;
                }

                //Statement 3
                String sql3 = "select first_name , last_name from ers_users where user_id = ?;";
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.setInt(1,userId);
                ResultSet rs3 = ps3.executeQuery();

                while(rs3.next()){
                    firstName = rs3.getString(1);
                    lastName = rs3.getString(2);
                }



                System.out.println("reimbursementType: "+ reimbursementType);
                managerPendingList = new ReimbursementHistoryDTO(userId, reimbursementId, firstName, lastName, status, submittedDate,
                        title, reimbursementType, amount, transactionDate, resolvedDate);
                managerPendingLists.add(managerPendingList);
                //}

            }
            con.commit();

        } finally {
            if (con != null && !con.isClosed())
                con.close();
        }
        return managerPendingLists;
    }


    @Override
    public List<ReimbursementHistoryDTO>  getResolvedReimbursementManager(int managerId) throws SQLException{
        System.out.println("Hellllooooooo-------");
        List<ReimbursementHistoryDTO> managerResolvedLists = new ArrayList<>();
        ReimbursementHistoryDTO managerResolvedList;
        String firstName ="";
        String lastName ="";
        int typeId = 0;
        int reimbursementId = 0;
        int userId = 0;
        Timestamp submittedDate = null;
        String status = "";
        String title = "";
        double amount = 0;
        Timestamp transactionDate = null;
        Timestamp resolvedDate = null;
        String reimbursementType = "";


        //_------------connect database------
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            String sql2 = "select reimbursement_id,user_id,date_submitted,status_id,date_approved_denied,title," +
                    "type_id,cost,transaction_date " +
                    "from ers_reimbursement where manager_id = ? and status_id = 3 or manager_id = ? and status_id = 4";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, managerId);
            ps2.setInt(2, managerId);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                reimbursementId = rs2.getInt(1);
                System.out.println("reimbursementId: "+ reimbursementId);
                userId = rs2.getInt(2);
                submittedDate = rs2.getTimestamp(3);
                int statusId = rs2.getInt(4);
                title = rs2.getString(6);
                typeId = rs2.getInt(7);
                amount = rs2.getDouble(8);
                transactionDate = rs2.getTimestamp(9);
                resolvedDate = rs2.getTimestamp(5);
                switch (statusId){
                    case 3:
                        status = "APPROVED";
                        break;
                    case 4:
                        status = "DENIED";
                        break;
                }

                switch (typeId){
                    case 1:
                        reimbursementType = "LODGING";
                        break;
                    case 2:
                        reimbursementType = "TRAVEL";
                        break;
                    case 3:
                        reimbursementType = "FOOD";
                        break;
                    case 4:
                        reimbursementType = "OTHER";
                        break;
                }

                //Statement 3
                String sql3 = "select first_name , last_name from ers_users where user_id = ?;";
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.setInt(1,userId);
                ResultSet rs3 = ps3.executeQuery();

                while(rs3.next()){
                    firstName = rs3.getString(1);
                    lastName = rs3.getString(2);
                }



                System.out.println("reimbursementType: "+ reimbursementType);
                managerResolvedList = new ReimbursementHistoryDTO(userId, reimbursementId, firstName, lastName, status, submittedDate,
                        title, reimbursementType, amount, transactionDate, resolvedDate);
                managerResolvedLists.add(managerResolvedList);
                //}
                System.out.println("reimbursementType2: "+ reimbursementType);
            }
            con.commit();

        } finally {
            if (con != null && !con.isClosed())
                con.close();
        }
        return managerResolvedLists;
    }

    public boolean approveReimbursement(int reimbursementId) throws SQLException{
        String sql1 = "update ers_reimbursement " +
                "set status_id = 3, date_approved_denied = NOW() " +
                "where reimbursement_id = ? RETURNING user_id;";
        Connection con = connectionUtil.getConnection();
        try{
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql1);
            //ps.setInt(1,3);
            //ps.setDate(2,java.sql.Date.valueOf(simpleDateFormat.format(LocalDate.now())));
            ps.setInt(1,reimbursementId);
            ResultSet rs1 = ps.executeQuery();

            int user_id = 0;
            while(rs1.next()){
                user_id = rs1.getInt(1);
                System.out.println("userID-> reimbursement status: " + rs1.getInt(1));
            }

            //Second statement
            String sql2 = "update ers_reimbursement_history " +
                    "set status_id = 3, date_approved_denied = NOW() " +
                    "where reimbursement_id = ?;";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            //ps2.setInt(1,3);
            //ps2.setDate(2,java.sql.Date.valueOf(simpleDateFormat.format(LocalDate.now())));
            ps2.setInt(1,reimbursementId);
            ps2.execute();

            if (user_id != 0) {
                con.commit();

                return true;
            } else {
                con.rollback();
                return false;
            }
        }finally {
            if (con != null && !con.isClosed())
                con.close();
        }
    }


    public boolean denyReimbursement(int reimbursementId) throws SQLException{
        String sql1 = "update ers_reimbursement " +
                "set status_id = 4, date_approved_denied = NOW() " +
                "where reimbursement_id = ? RETURNING user_id;";
        Connection con = connectionUtil.getConnection();
        try{
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql1);
            //ps.setInt(1,3);
            //ps.setDate(2,java.sql.Date.valueOf(simpleDateFormat.format(LocalDate.now())));
            ps.setInt(1,reimbursementId);
            ResultSet rs1 = ps.executeQuery();

            int user_id = 0;
            while(rs1.next()){
                user_id = rs1.getInt(1);
                System.out.println("userID-> reimbursement status: " + rs1.getInt(1));
            }

            //Second statement
            String sql2 = "update ers_reimbursement_history " +
                    "set status_id = 4, date_approved_denied = NOW() " +
                    "where reimbursement_id = ?;";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            //ps2.setInt(1,3);
            //ps2.setDate(2,java.sql.Date.valueOf(simpleDateFormat.format(LocalDate.now())));
            ps2.setInt(1,reimbursementId);
            ps2.execute();

            if (user_id != 0) {
                con.commit();

                return true;
            } else {
                con.rollback();
                return false;
            }
        }finally {
            if (con != null && !con.isClosed())
                con.close();
        }
    }

    @Override
    public List<ReimbursementHistoryDTO> viewSpecificReimbursement(int userId) throws SQLException {
        List<ReimbursementHistoryDTO> specificLists = new ArrayList<>();
        ReimbursementHistoryDTO specificList;
        String firstName ="";
        String lastName ="";
        int typeId = 0;
        int reimbursementId = 0;
        Timestamp submittedDate = null;
        int statusId = 0;

        String title = "";
        double amount = 0;
        Timestamp transactionDate = null;
        Timestamp resolvedDate = null;
        String reimbursementType = "";


        //_------------connect database------
        Connection con = connectionUtil.getConnection();
        con.setAutoCommit(false);
        try {
            String sql = "select reimbursement_id,user_id,date_submitted,status_id,date_approved_denied,title,type_id,cost,transaction_date\n" +
                    "from ers_reimbursement\n" +
                    "where user_id = ? and status_id  = 2;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reimbursementId = rs.getInt(1);
                System.out.println("reimbursementId: "+ reimbursementId);
                userId = rs.getInt(2);
                submittedDate = rs.getTimestamp(3);
                statusId = rs.getInt(4);
                title = rs.getString(6);
                typeId = rs.getInt(7);
                amount = rs.getDouble(8);
                transactionDate = rs.getTimestamp(9);
                resolvedDate = rs.getTimestamp(5);
                String status = "";
                switch (statusId){
                    case 2:
                        status = "PENDING";
                        break;
                    case 3:
                        status = "APPROVED";
                        break;
                    case 4:
                        status = "DENIED";
                        break;
                }

                switch (typeId){
                    case 1:
                        reimbursementType = "LODGING";
                        break;
                    case 2:
                        reimbursementType = "TRAVEL";
                        break;
                    case 3:
                        reimbursementType = "FOOD";
                        break;
                    case 4:
                        reimbursementType = "OTHER";
                        break;
                }

                //Statement 3
                String sql3 = "select first_name , last_name from ers_users where user_id = ?;";
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.setInt(1,userId);
                ResultSet rs3 = ps3.executeQuery();

                while(rs3.next()){
                    firstName = rs3.getString(1);
                    lastName = rs3.getString(2);
                }

                specificList = new ReimbursementHistoryDTO(userId, reimbursementId, firstName, lastName, status, submittedDate,
                        title, reimbursementType, amount, transactionDate, resolvedDate);
                specificLists.add(specificList);

            }

        } finally {
            if (con != null && !con.isClosed())
                con.close();
        }
        return specificLists;
    }
}
