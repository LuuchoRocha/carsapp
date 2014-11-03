package com.unrc.app.models;

import com.unrc.app.controllers.ElasticSearchController;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Vehicle extends Model {
    static {
        validatePresenceOf("name", "brand", "year", "plate", "user_id");
    }

    @Override
    public String toString() {
        return (this.getString("name") + " " + this.getString("brand") + "(" + this.getString("year")+ ")");
    } 

    public String owner(){
        return this.parent(User.class).toString();
    }
  
    @Override
    public boolean saveIt(){
        if (this.get("type") == null) {
            this.set("type", "other");
        }
        return super.saveIt();
    }

    public Vehicle name(String s){
        this.set("name", s);
        return this;
    }

    public Vehicle brand(String s){
        this.set("brand", s);
        return this;
    }

    public Vehicle year(String i){
        this.set("year", i);
        return this;
    }

    public Vehicle plate(String s){
        this.set("plate", s);
        return this;
    }

    public String name(){
        return this.getString("name");
    }

    public String brand(){
        return this.getString("brand");
    }

    public int year(){
        return this.getInteger("year");
    }

    public String plate(){
        return this.getString("plate");
    }

    public static List<Vehicle> filter(String... args){
        if ((args.length>0) && (args.length % 2 == 0)) {
            String query = "";
            String attribute;
            String value;
            int i = 0;
            attribute = args[i];
            i++;
            value = args[i];
            i++;
            query += attribute + " = '" + value + "'";
            while (i<args.length-1) {
                attribute = args[i];
                i++;
                value = args[i];
                i++;
                query += " AND " + attribute + " = '" + value + "'";
            }
            return Vehicle.find(query);
        } else return null;
    }

    public static List<Vehicle> vehiclesFrom(City city) {
        List<User> users = city.getAll(User.class);
        List<Vehicle> vehicles = new LinkedList();
        for(User u : users) {
            vehicles.addAll(u.getAll(Vehicle.class));
        }
        return vehicles;
    }
    
    @Override
    public final void afterCreate(){
        Map<String, Object> json = new HashMap<>();
        
        json.put("brand", this.brand());
        json.put("name", this.name());
        json.put("year", this.year());
        
        ElasticSearchController.client().prepareIndex()
                .setIndex("vehicles")
                .setType("vehicle")
                .setId(this.getId().toString())
                .setSource(json)
                .execute()
                .actionGet();
    }  
    
    @Override
    public final void beforeDelete(){
        super.beforeDelete();
        
        ElasticSearchController.client()
                .prepareDelete()
                .setIndex("vehicles")
                .setType("vehicle")
                .setId(this.getId().toString())
                .execute()
                .actionGet();
    }
}
