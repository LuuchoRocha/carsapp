package com.unrc.app.models;

import java.util.List;

public class Bike extends Vehicle {
    static {
        validatePresenceOf("name", "brand", "year", "plate", "displacement", "user_id");
    }

    @Override
    public boolean saveIt(){
        super.set("type", "bike");
        return super.saveIt();
    }
    
    public static List<Bike> all(){
        return Bike.findAll();
    }
    public Bike displacement(int i) {
        this.set("displacement", i);
        return this;
    }
    
    public int displacement(){
        return this.getInteger("displacement");
    }
}
