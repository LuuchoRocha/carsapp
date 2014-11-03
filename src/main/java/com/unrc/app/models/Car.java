package com.unrc.app.models;

import java.util.List;

public class Car extends Vehicle {
    static {
        validatePresenceOf("name","brand","year", "plate", "passengers", "user_id");
    }

    @Override
    public boolean saveIt(){
        set("type", "car");
        return super.saveIt();
    }
    
    public static List<Car> all(){
        return Car.findAll();
    }
    
    public Car passengers(int i){
        this.set("passengers", i);
        return this;
    }
    
    public int passengers(){
        return this.getInteger("passengers");
    }
}
