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
import model.bean.CarForProduction;
import model.bean.ProducedCar;
import model.bean.Property;

/**
 *
 * @author diego
 */
public class ProducedCarsDAO {
    
    public boolean saveProducedCarAndDeleteCarForProduction(ArrayList<ArrayList<Property>> propertiesList, 
            ArrayList<Integer> carsForProductionToDelete) throws SQLException {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt;

        /**
         * Usuário novo chave padrão no valor de 999
         */
        Integer lastId;
        for (int i = 0; i < propertiesList.size(); i++) {
            //ArrayList properties = propertiesList.get("new");
            stmt = con.prepareStatement("SELECT id FROM produced_cars "
                    + "WHERE id=(SELECT max(id) FROM produced_cars)");
            ResultSet rs = stmt.executeQuery();
            
            ArrayList properties = propertiesList.get(i);

            if (properties.size() < 10) {
                throw new SQLException("Erro ao salvar colaborador! Erro: quantidade de "
                        + "campos a serem salvos é menor que a quantidade de"
                        + " campos da tabela.");
            }

            String sql = "INSERT INTO produced_cars (";

            Iterator it = properties.iterator();
            while (it.hasNext()) {
                Property property = (Property) it.next();
                sql = sql + property.getName();

                if (it.hasNext()) {
                    sql = sql + ", ";
                } else {
                    sql = sql + ") VALUES (?,?,?,?,?,?,?,?,?,?)";
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
        }
        
        //Deleta carros para producao
        for (int i = 0; i < carsForProductionToDelete.size(); i++) {
            stmt = con.prepareStatement("DELETE FROM cars_for_production "
                    + "WHERE id=" + carsForProductionToDelete.get(i));
            stmt.executeQuery();
        }
        return true;
    }
    
    public ArrayList<ProducedCar> getProducedCars() throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        stmt = con.prepareStatement("SELECT id, chassis, color, clientName, productionDate, model, manufacturer, employee "
                + "FROM produced_cars ORDER BY productionDate DESC");
        ResultSet rs = stmt.executeQuery();       
        ArrayList<ProducedCar> producedCars = new ArrayList<>();
        while (rs.next()) {
            ProducedCar producedCar = new ProducedCar();
            producedCar.setId(rs.getInt("id"));
            producedCar.setChassis(rs.getString("chassis"));
            producedCar.setColor(rs.getString("color"));
            producedCar.setClientName(rs.getString("clientName"));
            producedCar.setProductionDate(rs.getDate("productionDate"));
            producedCar.setModel(rs.getString("model"));
            producedCar.setManufacturer(rs.getString("manufacturer"));
            producedCar.setEmployee(rs.getString("employee"));
            producedCars.add(producedCar);
        }
        ConnectionFactory.closeConnection(con, stmt, rs);
        return producedCars;
    }

    public CarForProduction getCarForProduction(String cod) throws SQLException {
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
    
    public CarForProduction getCarForProductionToDelete(String carIdentification) throws SQLException {
        Connection con = ConnectionFactory.getConnection();

        PreparedStatement stmt = con.prepareStatement("SELECT id, clientName, model, manufacturer "
                + "FROM cars_for_production WHERE carIdentification=" + carIdentification);
        ResultSet rs = stmt.executeQuery();
        CarForProduction carForProduction = null;
        if (rs.next()) {
            carForProduction = new CarForProduction();
            carForProduction.setId(rs.getInt("id"));
            carForProduction.setClientName(rs.getString("clientName"));
            carForProduction.setModel(rs.getString("model"));
            carForProduction.setManufacturer(rs.getString("manufacturer"));
        }
        return carForProduction;
    }
    
    public boolean checkRecord(String carIdentification) throws SQLException {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT carIdentification "
                + "FROM produced_cars WHERE carIdentification=" + carIdentification);        
        ResultSet rs = stmt.executeQuery();
        boolean hasRecord = false;
        if (rs.next()) {
            hasRecord = true;
        }
        ConnectionFactory.closeConnection(con, stmt, rs);
        return hasRecord;
    }
}