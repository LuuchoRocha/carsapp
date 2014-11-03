package com.unrc.app.models;

import java.util.List;

public class Truck extends Vehicle {
    static {
        validatePresenceOf("name","brand","year", "plate", "max_load", "user_id");
    }

    @Override
    public boolean saveIt(){
        super.set("type", "truck");
        return super.saveIt();
    }
  
    public static List<Truck> all(){
        return Truck.findAll();
    }
    
    public Truck maxLoad(int i) {
        this.set("max_load", i);
        return this;
    }
    
    public int maxLoad() {
        return this.getInteger("max_load");
    }
}
