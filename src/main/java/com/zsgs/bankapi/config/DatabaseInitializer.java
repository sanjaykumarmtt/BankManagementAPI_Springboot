//package com.zsgs.bankapi.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseInitializer {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @PostConstruct
//    public void init() {
//        createStoredProcedure();
//    }
//
//    private void createStoredProcedure() {
//        String procedureQuery = "CREATE PROCEDURE IF NOT EXISTS sp_CreateUserAndAccount("
//                + "    IN p_userName VARCHAR(255),"
//                + "    IN p_address VARCHAR(255),"
//                + "    IN p_phoneNumber VARCHAR(20),"
//                + "    IN p_email VARCHAR(255),"
//                + "    IN p_accountNumber BIGINT,"
//                + "    IN p_balance DOUBLE,"
//                + "    IN p_accountType VARCHAR(50),"
//                + "    IN p_isActive TINYINT(1)) "
//                + "BEGIN "
//                + "    DECLARE v_userId INT; "
//                + "    DECLARE EXIT HANDLER FOR SQLEXCEPTION "
//                + "    BEGIN "
//                + "        ROLLBACK; "
//                + "    END; "
//                + "    START TRANSACTION; "
//                + "    INSERT INTO user (user_name, address, phone_number, email) VALUES (p_userName, p_address, p_phoneNumber, p_email); "
//                + "    SET v_userId = LAST_INSERT_ID(); "
//                + "    INSERT INTO account(user_id, account_number, balance, account_type, is_active) VALUES (v_userId, p_accountNumber, p_balance, p_accountType, p_isActive); "
//                + "    COMMIT; "
//                + "END";
//        
//        try {
//            jdbcTemplate.execute(procedureQuery);
//            System.out.println("Stored procedure 'sp_CreateUserAndAccount' verified/created successfully.");
//        } catch (Exception e) {
//            System.err.println("Error creating stored procedure: " + e.getMessage());
//        }
//    }
//}
