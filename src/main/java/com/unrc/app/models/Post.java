package com.unrc.app.models;

import com.unrc.app.App;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.javalite.activejdbc.Model;

public class Post extends Model {
    static {
        validatePresenceOf("user_id", "vehicle_id", "text", "price");
    }

    public Post text(String s){
        this.set("text", s);
        return this;
    }

    @Override
    public String toString(){
        return this.getString("text");
    }

    public String text(){
        return this.getString("text");
    }

    public String author(){
        System.out.println((User.class).toString());
        return this.parent(User.class).toString();
    }

    public Post price(int i) {
        this.set("price", i);
        return this;
    }

    public int price(){
        return this.getInteger("price");
    }
    
    public String vehicle(){
        System.out.println((Vehicle.class).toString());
        return this.parent(Vehicle.class).toString();
    }

    public boolean addQuestion(String text, User user) {
        Question question = new Question();
        question.set("question", text);
        question.setParents(user, this);
        return question.saveIt();
    }

    public List<Question> questions(){
        return Question.find("post_id = ?", this.getId());
    }

    public Question question(int i) {
        return Question.findById(i);
    }

    public static List<Post> filter(String... args){
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
            return Post.find(query);
        } else return null;
    }
  
    public static List<Post> all(){
        return Post.findAll();
    }
          
    @Override
      public void afterCreate(){

      Map<String, Object> json = new HashMap<>();
      json.put("text", this.text());
      json.put("author", this.author());

      App.client.prepareIndex("posts", "post")
                  .setSource(json)
                  .execute()
                  .actionGet();
    }
}