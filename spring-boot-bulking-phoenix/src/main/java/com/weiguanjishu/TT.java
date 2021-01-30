package com.weiguanjishu;

import java.sql.*;


public class TT {

    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection("jdbc:phoenix:thin:url=http://localhost:8765", "wuyiran", "jdpassword");

        PreparedStatement statement = conn.prepareStatement("select count(*) from zsc.proxy");

        ResultSet rset = statement.executeQuery();

        while (rset.next()) {

            System.out.println(rset.getString(1));

        }

    }
}
