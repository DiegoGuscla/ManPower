/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bean;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 *
 * @author diego
 */
public class DataEntryDate {
    
    public static String getStringFromDate(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(date);
        } else {
            return "";
        }
    }
    
    public static Date getDateFromString(String sDate) {
        if (sDate.isEmpty()) {
            return null;
        }

        Date date = null;
        if (sDate.contains("/")) {
            String dataString[] = sDate.split("/");

            //DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
            date = java.sql.Date.valueOf(dataString[2] + "-"
                    + dataString[1] + "-" + dataString[0]);
        } else {
            date = java.sql.Date.valueOf(sDate);
        }
        return date;
    }
    
    /**Método que converte String em tipo de dado Time
     * @param sTime String com o valor do time no formato hh:mm:ss
     * @return 
     */
    public static Time getTimeFromString(String sTime) {
        if (sTime.isEmpty() || !sTime.contains(":") || sTime.length() < 5) {
            return null;
        }

        sTime = sTime + ":00";
        
        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        Time time = null;
        try {
            time = new Time(fmt.parse(sTime).getTime());
        } catch (ParseException ex) {           
        }
        return time;
    }

    public static String getStringFromTime(Time time) {
        if (time == null)
            return "";
        //DateFormat df = new SimpleDateFormat("HH:mm:ss");
        DateFormat df = new SimpleDateFormat("HH:mm");
        String x = df.format(time);
        return df.format(time);
    }
    
    public static String getStringFromTimestamp(Timestamp timestamp) {
        /*
        if (timestamp != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return df.format(timestamp);
        } else {
            return "";
        }
        */
        return getStringFromTimestamp(timestamp, null);
    }
    
    public static String getStringFromTimestamp(Timestamp timestamp, String format) {
        if (timestamp != null) {
            DateFormat df;
            if (format == null) {
                df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            } else {
                df = new SimpleDateFormat(format);
            }
            return df.format(timestamp);
        } else {
            return "";
        }
    }

    public static Date verifyDateInput(String date) throws Exception {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            return Date.valueOf(LocalDate.parse(date, parser));
        } catch (Exception ex) {
            throw new Exception("Data invalida! ");
        }
    }
}
