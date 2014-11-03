package com.unrc.app.models;

import java.util.List;
import org.javalite.activejdbc.Model;

public class Rate extends Model {
    static {
        validatePresenceOf("rate", "post_id", "user_id");
    }
    
    public Rate rate(int i){
        this.set("rate", i);
        return this;
    }
    
    public int rate(){
        return this.getInteger("rate");
    }
    public static List<Rate> all(){
        return Rate.findAll();
    }
}
