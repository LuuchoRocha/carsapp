package com.unrc.app.models;

import org.javalite.activejdbc.Model;
/**
 *
 * @author lucho
 */
public class City extends Model {
    static {
        validatePresenceOf("name", "state", "country", "postcode");
    }
    
    public City name(String s){
        this.set("name", s);
        return this;
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
