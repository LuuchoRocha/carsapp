package com.unrc.app.models;

import java.util.List;
import org.javalite.activejdbc.Model;

public class City extends Model {
    static {
        validatePresenceOf("name", "state", "country", "postcode");
    }

    public static City findByPostCode(String zip) {
        return City.findFirst("postcode = ?", zip);
    }
    
    public City name(String s){
        this.set("name", s);
        return this;
    }
    
    public static List<City> all(){
        return City.findAll();
    }
    
    @Override
    public String toString(){
        return (this.getString("name") + "(" +this.getString("postcode") + ")");
    }
    
    public City state(String s){
        this.set("state", s);
        return this;
    }
    
    public City country(String s){
        this.set("country", s);
        return this;
    }
    
    public City postcode(String s){
        this.set("postcode", s);
        return this;
    }
    
    public String name(){
        return this.getString("name");
    }
    
    public String state(){
        return this.getString("state");
    }
    
    public String country(){
        return this.getString("country");
    }
    
    public String postcode(){
        return this.getString("postcode");
    }
}
