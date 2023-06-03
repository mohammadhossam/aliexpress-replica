//package com.msa.configManager.properties;
//
//import java.util.HashMap;
//
//public class Property {
//    private String propertyName; // spring.data.redis.port
//    private String val; // should contain placeholders for the values of external resources for example: {users-service-cache.port}
//
//    public Property(String propertyName, String val) {
//        this.propertyName = propertyName;
//        this.val = val;
//    }
//
//    public String toString() {
//        return "--" + propertyName + "=" + getVal();
//    }
//
//    public String getVal() {
//        return val;
//    }
//
//    public String getPropertyName() {
//        return propertyName;
//}
//
//    public void setVal(String val) {
//        this.val = val;
//    }
//}
