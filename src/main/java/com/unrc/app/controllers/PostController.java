package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import com.unrc.app.models.Post;
import com.unrc.app.models.User;
import com.unrc.app.models.Vehicle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class PostController {
    
    public static ModelAndView getPosts(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();

        List<Post> posts = Post.findAll();

        attributes.put("posts", posts);

        return new ModelAndView(attributes, "./moustache/posts.moustache");
    }
    
    public static ModelAndView getMyposts(Request request, Response response){
        if (null != VisitorController.existsSession(request)) {
            Map<String, Object> attributes = new HashMap<>();
            String email = request.session(false).attribute("email");
            List<Post> posts =  User.findByEmail(email).getAll(Post.class);

            attributes.put("posts", posts);

            return new ModelAndView(attributes, "./moustache/myposts.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static ModelAndView getPostsNew(Request request, Response response){
        if(null != VisitorController.existsSession(request)) {
            Map<String, Object> attributes = new HashMap<>();

            String email = request.session(false).attribute("email");

            List<Vehicle> vehicles = User.findByEmail(email).getAll(Vehicle.class);

            attributes.put("vehicles", vehicles);

            return new ModelAndView(attributes, "./moustache/newpost.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static String postPosts(Request request, Response response){
        String price = request.queryParams("price");
        String text = request.queryParams("text");
        String vehicle_id = request.queryParams("vehicle_id");
        Integer user_id = request.session(false).attribute("user_id");

        User u = User.findById(user_id);
        Vehicle v = Vehicle.findById(vehicle_id);

        String body = "";
        boolean exit = text.equals("") || null == Integer.valueOf(price) || null == u || null == v;

        if (!exit) {
            Post p = new Post();
            p
                .price(price)
                .text(text)
                .setParents(u, v);
            if (p.saveIt()) {                    
                body += "<body><script type='text/javascript'>";
                body += "alert('Anuncio correctamente publicado'); document.location = '/';";
                body += "</script></body>";
            }
            else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El anuncio no pudo ser publicado'); document.location = '/';";
                body += "</script></body>";
            }
        } else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El anuncio no pudo ser publicado porque alguno de los campos estaba vacio'); document.location = '/';";
                body += "</script></body>";
        }
        return body;
    }
    
    public static ModelAndView getPostsSearch(Request request, Response response){
        return new ModelAndView(null, "./moustache/postsearch.moustache");
    }
    
    public static ModelAndView getPostsSearchResponse(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();

        Client client = ElasticSearchController.client();

        String search_text = request.queryParams("search_text").toLowerCase();

        if (null == search_text ? true : search_text.equals(""))
            return new ModelAndView(attributes, "./moustache/emptysearch.moustache");

        SearchResponse searchResponse = client.prepareSearch("posts")
                .setQuery(QueryBuilders.multiMatchQuery(search_text, "text", "author", "vehicle"))
                .setSize(10)
                .execute()
                .actionGet();

        List<Post> posts = new LinkedList<>();

        searchResponse
                .getHits()
                .forEach(
                    (SearchHit h) -> {
                        posts.add(Post.findById(h.getId()));
                    }
                );

        attributes.put("posts", posts);

        return new ModelAndView(attributes, "./moustache/postsearch_response.moustache");
    }
    
    public static ModelAndView getPostsDel(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 2) {
            Map<String, Object> attributes = new HashMap<>();

            List<Post> posts = Post.findAll();

            attributes.put("posts", posts);

            return new ModelAndView(attributes, "./moustache/postdel.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }
    
    public static String deletePosts(Request request, Response response){
        String body = "";

        Post p = Post.findById(request.params(":id"));

        if (null != p) {
            p.deleteCascade();
            body += "El anuncio fue correctamente eliminado";
        } else {
            body += "El anuncio no fue encontrado en la base de datos!";
        }
        return body;
    }
}
