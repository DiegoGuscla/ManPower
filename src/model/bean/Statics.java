/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bean;

/**
 *
 * @author Dell
 */
public class Statics {
    public static final boolean ACTIVE = true;
    
    public static final String getManufacturerFromChassis(String chassis) {
        return chassis.length() == 17 ? chassis.substring(0, 3) : "";
    }
    
    public static final String getModelFromChassis(String chassis) {
        return chassis.length() == 17 ? chassis.substring(6, 8) : "";
    }
}
