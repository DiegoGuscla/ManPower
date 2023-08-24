/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import connection.ConnectionFactory;
import model.bean.Model;
import model.bean.Property;

/**
 *
 * @author diego
 */
public class ModelsDAO {

    public Integer saveModel(HashMap<String, ArrayList<Property>> propertiesList) throws SQLException {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        /**
         * Usuário novo chave padrão no valor de 999
         */
        Integer lastId = 0;
        if (propertiesList.get("new") != null) {
            stmt = con.prepareStatement("SELECT id FROM models "
                    + "WHERE id=(SELECT max(id) FROM models)");

            ResultSet rs = stmt.executeQuery();
            //if (rs.next()) {
                ArrayList properties = propertiesList.get("new");

                if (properties.size() < 3) {
                    throw new SQLException("Erro ao salvar colaborador! Erro: quantidade de "
                            + "campos a serem salvos é menor que a quantidade de"
                            + " campos da tabela.");
                }

                String sql = "INSERT INTO models (";

                Iterator it = properties.iterator();
                while (it.hasNext()) {
                    Property property = (Property) it.next();
                    sql = sql + property.getName();

                    if (it.hasNext()) {
                        sql = sql + ", ";
                    } else {
                        sql = sql + ") VALUES (?,?,?)";
                    }
                }
                stmt = con.prepareStatement(sql);

                it = properties.iterator();
                int count = 1;
                if(rs != null && rs.next()) {
                    lastId = rs.getInt("id");
                } else {
                    lastId = 0;
                }
                while (it.hasNext()) {
                    Property property = (Property) it.next();
                    if (property.getName().equals("id")) {
                        stmt.setInt(count, ++lastId);
                    } else {
                        if (property.getType().equals(Property.PropertyType.STRING)) {
                            stmt.setString(count, property.getStringValue());
                        } else if (property.getType().equals(Property.PropertyType.INTEGER)) {
                            stmt.setInt(count, property.getIntValue());
                        } else if (property.getType().equals(Property.PropertyType.DATE)) {
                            stmt.setDate(count, property.getDateValue());
                        } else if (property.getType().equals(Property.PropertyType.DOUBLE)) {
                            stmt.setDouble(count, property.getDoubleValue());
                        } else if (property.getType().equals(Property.PropertyType.TIME)) {
                            stmt.setTime(count, property.getTimeValue());
                        } else {
                            stmt.setBoolean(count, property.getBooleanValue());
                        }
                    }
                    count++;
                }
                stmt.executeUpdate();
            //} else {
                //Erro ao buscar última linha da tabela
            //    throw new SQLException("Erro ao salvar fabricante! Erro: ao buscar última "
            //            + "linha da tabela.");
            //}
        } else {
            for (Map.Entry<String, ArrayList<Property>> m : propertiesList.entrySet()) {
                String sql = "UPDATE models SET ";

                Iterator it = m.getValue().iterator();
                while (it.hasNext()) {
                    Property property = (Property) it.next();
                    sql = sql + property.getName() + "=?";

                    if (it.hasNext()) {
                        sql = sql + ", ";
                    }
                }

                sql = sql + " WHERE id=?";
                stmt = con.prepareStatement(sql);

                int count = 1;
                for (int i = 0; i < m.getValue().size(); i++) {
                    Property property = m.getValue().get(i);
                    if (property.getType().equals(Property.PropertyType.STRING)) {
                        stmt.setString(count, property.getStringValue());
                    } else if (property.getType().equals(Property.PropertyType.INTEGER)) {
                        stmt.setInt(count, property.getIntValue());
                    } else if (property.getType().equals(Property.PropertyType.DATE)) {
                        stmt.setDate(count, property.getDateValue());
                    } else if (property.getType().equals(Property.PropertyType.DOUBLE)) {
                        stmt.setDouble(count, property.getDoubleValue());
                    } else if (property.getType().equals(Property.PropertyType.TIME)) {
                        stmt.setTime(count, property.getTimeValue());
                    } else {
                        stmt.setBoolean(count, property.getBooleanValue());
                    }
                    count++;
                }
                stmt.setInt(count, Integer.valueOf(m.getKey()));
                stmt.executeUpdate();
            }
        }
        ConnectionFactory.closeConnection(con, stmt);
        return lastId;
    }
    
    public ArrayList<Model> getModels() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        stmt = con.prepareStatement("SELECT id, name, code "
                + "FROM models");
        ResultSet rs = stmt.executeQuery();       
        ArrayList<Model> models = new ArrayList<>();
        while (rs.next()) {
            Model model = new Model();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setCode(rs.getString("code"));            
            models.add(model);
        }
        ConnectionFactory.closeConnection(con, stmt, rs);
        return models;
    }

    public Model getModel(String cod) throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        stmt = con.prepareStatement("SELECT name, code FROM models WHERE id=?");
        stmt.setInt(1, Integer.parseInt(cod));
        ResultSet rs = stmt.executeQuery();
        Model model = null;
        while (rs.next()) {
            model = new Model();
            model.setId(Integer.valueOf(cod));
            model.setName(rs.getString("name"));
            model.setCode(rs.getString("code"));            
        }
        ConnectionFactory.closeConnection(con, stmt, rs);
        return model;
    }
}