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
import java.util.Iterator;
import connection.ConnectionFactory;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bean.CarForProduction;
import model.bean.Property;

/**
 *
 * @author diego
 */
public class CarsForProductionDAO {
    
    private Property property;

    /*
    public Integer saveCarForProduction(HashMap<String, ArrayList<Property>> propertiesList) throws SQLException {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        Integer lastId = 0;
        if (propertiesList.get("new") != null) {
            stmt = con.prepareStatement("SELECT id FROM cars_for_production "
                    + "WHERE id=(SELECT max(id) FROM cars_for_production)");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ArrayList properties = propertiesList.get("new");

                if (properties.size() < 21) {
                    throw new SQLException("Erro ao salvar colaborador! Erro: quantidade de "
                            + "campos a serem salvos é menor que a quantidade de"
                            + " campos da tabela.");
                }

                String sql = "INSERT INTO cars_for_production (";

                Iterator it = properties.iterator();
                while (it.hasNext()) {
                    Property property = (Property) it.next();
                    sql = sql + property.getName();

                    if (it.hasNext()) {
                        sql = sql + ", ";
                    } else {
                        sql = sql + ") VALUES (?,?,?,?,?,?,?)";
                    }
                }
                stmt = con.prepareStatement(sql);

                it = properties.iterator();
                int count = 1;
                lastId = rs.getInt("id");
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
            } else {
                //Erro ao buscar última linha da tabela
                throw new SQLException("Erro ao salvar colaborador! Erro: ao buscar última "
                        + "linha da tabela.");
            }
        } else {
            for (Map.Entry<String, ArrayList<Property>> m : propertiesList.entrySet()) {
                String sql = "UPDATE cars_for_production SET ";

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
        return lastId;
    }
    */
    
    /*
    public Integer saveCarForProduction(HashMap<String, ArrayList<Property>> propertiesList) throws SQLException {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        Integer lastId = 0;
        if (propertiesList.get("new") != null) {
            stmt = con.prepareStatement("SELECT id FROM cars_for_production "
                    + "WHERE id=(SELECT max(id) FROM cars_for_production)");

            ResultSet rs = stmt.executeQuery();
            //if (rs.next()) {
            for (Map.Entry<String, ArrayList<Property>> m : propertiesList.entrySet()) {
                //ArrayList properties = propertiesList.get("new");
                ArrayList properties = m.getValue();

                if (properties.size() < 7) {
                    throw new SQLException("Erro ao salvar colaborador! Erro: quantidade de "
                            + "campos a serem salvos é menor que a quantidade de"
                            + " campos da tabela.");
                }

                String sql = "INSERT INTO cars_for_production (";

                Iterator it = properties.iterator();
                while (it.hasNext()) {
                    Property property = (Property) it.next();
                    sql = sql + property.getName();

                    if (it.hasNext()) {
                        sql = sql + ", ";
                    } else {
                        sql = sql + ") VALUES (?,?,?,?,?,?,?)";
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
           //    throw new SQLException("Erro ao salvar colaborador! Erro: ao buscar última "
            //            + "linha da tabela.");
            //}
            }
        } else {
            for (Map.Entry<String, ArrayList<Property>> m : propertiesList.entrySet()) {
                String sql = "UPDATE cars_for_production SET ";

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
        return lastId;
    }
    */
    
    public String addCarsForProduction(ArrayList<ArrayList<Property>> propertiesList) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        String message = "";
        for (int i = 0; i < propertiesList.size(); i++) {
            ArrayList properties = propertiesList.get(i);
            message = addCarForProduction(properties, con, stmt);
        }
        if (message.contains(";")) {
            message = "Os seguintes registros estao duplicados na lista: " + message.replaceAll(";", " ");
        }
        ConnectionFactory.closeConnection(con, stmt);
        return message;
    }

    private String addCarForProduction(ArrayList<Property> properties, Connection con,
            PreparedStatement stmt) {
        try {
            Integer lastId;
            stmt = con.prepareStatement("SELECT id FROM cars_for_production "
                    + "WHERE id=(SELECT max(id) FROM cars_for_production)");
            ResultSet rs = stmt.executeQuery();

            String sql = "INSERT INTO cars_for_production (";

            Iterator it = properties.iterator();
            while (it.hasNext()) {
                property = (Property) it.next();
                
                //Checa se ja esta adicionado
                if (property.getName().equals("chassis")) {
                    if (checkRecord(property.getStringValue(), con, stmt)) {
                        return "";
                    }
                }                    
                    
                sql = sql + property.getName();

                if (it.hasNext()) {
                    sql = sql + ", ";
                } else {
                    sql = sql + ") VALUES (?,?,?,?,?,?,?)";
                }
            }
            stmt = con.prepareStatement(sql);

            it = properties.iterator();
            int count = 1;
            if (rs != null && rs.next()) {
                lastId = rs.getInt("id");
            } else {
                lastId = 0;
            }
            while (it.hasNext()) {
                property = (Property) it.next();
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
        } catch (SQLException ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                return "; " + property.getStringValue();
            } else {
                return "Erro ao salvar propriedades: " + ex.getMessage();
            }
        }
        return "";
    }
    
    public ArrayList<CarForProduction> getCarsForProduction() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        
        PreparedStatement stmt = con.prepareStatement("SELECT id, chassis, clientName, deliveryDate, model, manufacturer, fileName "
                + "FROM cars_for_production ORDER BY deliveryDate ASC");
        ResultSet rs = stmt.executeQuery();       
        ArrayList<CarForProduction> carsForProduction = new ArrayList<>();
        while (rs.next()) {
            CarForProduction carForProduction = new CarForProduction();
            carForProduction.setId(rs.getInt("id"));
            carForProduction.setChassis(rs.getString("chassis"));
            carForProduction.setClientName(rs.getString("clientName"));
            carForProduction.setDeliveryDate(rs.getDate("deliveryDate"));
            carForProduction.setModel(rs.getString("model"));
            carForProduction.setManufacturer(rs.getString("manufacturer"));
            carForProduction.setFileName(rs.getString("fileName"));
            carsForProduction.add(carForProduction);
        }
        ConnectionFactory.closeConnection(con, stmt, rs);
        return carsForProduction;
    }

    public CarForProduction getCarForProductions(String cod) throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        stmt = con.prepareStatement("SELECT chassis, color, clientName, deliveryDate, model, manufacturer "
                + "FROM cars_for_production WHERE id=?");
        stmt.setInt(1, Integer.valueOf(cod));
        ResultSet rs = stmt.executeQuery();
        CarForProduction carForProduction = null;
        while (rs.next()) {
            carForProduction = new CarForProduction();
            carForProduction.setId(rs.getInt("id"));
            carForProduction.setChassis(rs.getString("chassis"));
            carForProduction.setClientName(rs.getString("clientName"));
            carForProduction.setDeliveryDate(rs.getDate("deliveryDate"));
            carForProduction.setModel(rs.getString("model"));
            carForProduction.setManufacturer(rs.getString("manufacturer"));
        }
        return carForProduction;
    }
    
    private boolean checkRecord(String chassis, Connection con, PreparedStatement stmt) throws SQLException {
        //Connection con = ConnectionFactory.getConnection();
        stmt = con.prepareStatement("SELECT chassis FROM cars_for_production WHERE chassis=?");
        stmt.setString(1, chassis);
        ResultSet rs = stmt.executeQuery();
        boolean hasRecord = false;
        if (rs.next()) {
            hasRecord = true;
        }
        //ConnectionFactory.closeConnection(con, stmt, rs);
        return hasRecord;
    }
}