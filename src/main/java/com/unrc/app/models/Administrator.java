package com.unrc.app.models;

import com.unrc.app.controllers.ElasticSearchController;
import java.util.HashMap;
import java.util.Map;
import org.javalite.activejdbc.Model;

public class Administrator extends Model {
    static {
        validatePresenceOf("pass", "email");
    }
    
    @Override
    public String toString(){
        return this.email();
    }
    
    public Administrator email(String s){
        this.set("email", s);
        return this;
    }
    
    public Administrator pass(String s){
        this.set("pass", s);
        return this;
    }
    
    public String email(){
        return this.get("email").toString();
    }
    
    public String pass(){
        return this.get("pass").toString();
    }
    
    public static boolean deleteUser(String email) {
        return User.findFirst("email = ?", email).delete();
    }
    
    public Post editPost(Post post, String... args){
        if ((args.length>2) && (args.length % 2 == 0)) {
            post.set(args);
        }
        return post;
    }
    
    public static Administrator findByEmail(String email) {
        return Administrator.findFirst("email = ?", email);
    }
    
    @Override
    public final void afterCreate(){
        super.afterCreate();
        
        Map<String, Object> json = new HashMap<>();
        json.put("email", this.email());

        ElasticSearchController.client()
                .prepareIndex()
                .setIndex("admins")
                .setType("admin")
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
                .setIndex("admins")
                .setType("admin")
                .setId(this.getId().toString())
                .execute()
                .actionGet();
    }
}
