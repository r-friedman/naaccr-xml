/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package org.naaccr.xml.entity;

public class Item {
    
    private String id;
    
    private Integer num;
    
    private String value;
    
    public Item() {
    }

    public Item(String idVal, String val) {
        id = idVal;
        value = val;
    }
    
    public Item(Integer numVal, String val) {
        num = numVal;
        value = val;
    }

    public String getId() {
        return id;
    }

    public void setId(String val) {
        id = val;
    }
    
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer val) {
        num = val;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String val) {
        value = val;
    }
    
}
