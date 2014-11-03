package com.unrc.app.models;

import java.util.List;
import org.javalite.activejdbc.Model;

public class Phone extends Model {
    static {
        validatePresenceOf("type", "num", "user_id");
    }

    public static List<Phone> filter(String... args) {
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
          return Phone.find(query);
      } else return null;
    }
    
    @Override
    public boolean saveIt(){
        return super.saveIt();
    }
    
    public enum PhoneType {
        personal, home, work;
    }
    
    public Phone type(PhoneType t) {
        this.set("type", t.name());
        return this;
    }
    
    public String type(){
        String type = this.getString("type");
        switch(type){
            case "home":
                return "Casa";
            case "work":
                return "Trabajo";
            case "personal":
                return "Celular";
        }
        return "Celular";
    }
    
    public Phone num(String i) {
        this.set("num", i);
        return this;
    }
    
    public int num(){
        return this.getInteger("num");
    }
    
    public String toString(){
        return this.getString("type") + ": " + this.getString("num");
    }
    
    public String owner(){
        return this.parent(User.class).toString();
    }
    
    public static List<Phone> all(){
        return Phone.findAll();
    }
}
