/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connection.ConnectionFactory;
import model.bean.Contributor;
import model.bean.Statics;

/**
 *
 * @author RG Digital
 */
public class LoginDAO {

    public Contributor checkUserParameters(String user) throws SQLException {
        try (Connection con = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT "
                    + "     id,"
                    + "     user,"
                    + "     password,"
                    + "     name,"
                    + "     type "
                    + "FROM "
                    + "     contributors "
                    + "WHERE "
                    + "     user=? "
                    + "AND "
                    + "     situation=?")) {
                stmt.setString(1, user);
                stmt.setBoolean(2, Statics.ACTIVE);

                Contributor contributor = null;
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        contributor = new Contributor();
                        contributor.setId(rs.getInt("id"));
                        contributor.setUser(rs.getString("user"));
                        contributor.setPassword(rs.getString("password"));
                        contributor.setType(rs.getString("type"));
                        contributor.setName(rs.getString("name"));
                    }
                }
                return contributor;
            }
        }
    }
}
