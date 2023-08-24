/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bean;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author diego
 */
public class Property {
    
    private String name;
    private Object value;
    private PropertyType type;

    public enum PropertyType {
        STRING, DATE, INTEGER, DOUBLE, TIME, BOOLEAN, TIMESTAMP
    }
    
    public void setProperty(String name, PropertyType type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }
    
    public boolean getBooleanValue() {
        return value.toString().equals("true");
    }
    
    public String getStringValue() {
         if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    public int getIntValue() {
        return Integer.parseInt(value.toString());
    }
    
    public double getDoubleValue() {
        if(value.toString().contains(","))
            value = value.toString().replace(",", ".");
        
        return Double.parseDouble(value.toString());
    }
    
    public Date getDateValue() {
        /*
        if (value.toString().isEmpty()) {
            return null;
        }

        Date date = null;
        if (value.toString().contains("/")) {
            String dataString[] = value.toString().split("/");

            //DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
            date = java.sql.Date.valueOf(dataString[2] + "-"
                    + dataString[1] + "-" + dataString[0]);
        } else {
            date = java.sql.Date.valueOf(value.toString());
        }
        */
        return DataEntryDate.getDateFromString(value.toString());
    }
    
    public Time getTimeValue() {
        /*
        if (value.toString().isEmpty()) {
            return null;
        }

        Date date = null;
        if (value.toString().contains("/")) {
            String dataString[] = value.toString().split("/");

            //DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
            date = java.sql.Date.valueOf(dataString[2] + "-"
                    + dataString[1] + "-" + dataString[0]);
        } else {
            date = java.sql.Date.valueOf(value.toString());
        }
        */
        return DataEntryDate.getTimeFromString(value.toString());
    }
    
    public Timestamp getTimeStamp() {
        if (value == null) {
            return null;
        }
        return Timestamp.valueOf(value.toString());
    }
}
