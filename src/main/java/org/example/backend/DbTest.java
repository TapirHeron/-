package org.example.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTest {
    public static void main(String[] args) {
        // 示例：检查数据库连接配置
        String dbUrl = "jdbc:mysql://localhost:3306/mysql";
        String dbUser = "root";
        String dbPassword = "root";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
