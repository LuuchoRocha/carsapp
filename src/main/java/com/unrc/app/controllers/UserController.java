package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import com.unrc.app.models.City;
import com.unrc.app.models.User;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.javalite.activejdbc.DBException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class UserController {
    
    public static ModelAndView getUsers(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();
        List<User> users = User.findAll();

        attributes.put("users", users);

        return new ModelAndView(attributes, "./moustache/users.moustache");
    }

    public static ModelAndView getUsersNew(Request request, Response response){
        if (VisitorController.sessionLevel(request.session(false)) == 0) {
            Map<String, Object> attributes = new HashMap<>();
            List<City> cities = City.all();
            
            attributes.put("cities", cities);

            return new ModelAndView(attributes, "./moustache/newuser.moustache");
        } else {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", request.session(false).attribute("email").toString());
            return new ModelAndView(attributes, "./moustache/alreadylogged.moustache");
        }
    }

    public static String postUsers(Request request, Response response){
        String first_name = request.queryParams("first_name");
        String last_name = request.queryParams("last_name");
        String pass = request.queryParams("pass");
        String email = request.queryParams("email");
        String address = request.queryParams("address");
        String postcode = request.queryParams("postcode");

        String body = "";

        if (!((first_name.equals(""))
            || (last_name.equals(""))
            || (pass.equals(""))
            || (email.equals(""))
            || (address.equals(""))
            || (null == postcode))) {
            City c = City.findByPostCode(postcode);
            User u = new User();
            u
                .firstName(first_name)
                .lastName(last_name)
                .email(email)
                .address(address)
                .pass(pass)
                .setParent(c);

            Boolean res = false;
            try {
                res = u.saveIt();
            } catch (DBException e) {}
            if (res) {
                body += "<body><script type='text/javascript'>";
                body += "alert('Usuario correctamente registrado.'); document.location = '/';";
                body += "</script></body>";
            }
            else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El usuario no pudo ser registrado.'); document.location = '/';";
                body += "</script></body>";
            }
        } else {
                body += "<body><script type='text/javascript'>";
                body += "alert('No puede haber campos vacios.'); document.location = '/';";
                body += "</script></body>";
        }
        return body;
    }

    public static ModelAndView getUsersDel(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 2) {
            Map<String, Object> attributes = new HashMap<>();

            List<User> user = User.findAll();

            attributes.put("users", user);

            return new ModelAndView(attributes, "./moustache/userdel.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }

    public static String deleteUsers(Request request, Response response){
        String body = "";
        User user = User.findById(request.params(":id"));
        if(null != user){
            user.deleteCascade();
            body += "El usuario fue correctamente eliminado";
        } else {
            body += "El usuario no fue encontrado en la base de datos!";
        }
        return body;
    }

    public static ModelAndView getUsersSearch(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 1) {
            return new ModelAndView(null, "./moustache/usersearch.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }

    public static ModelAndView getUsersSearchResponse(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 1) {
            Map<String, Object> attributes = new HashMap<>();

            Client client = ElasticSearchController.client();

            String search_text = request.queryParams("search_text").toLowerCase();

            if (null == search_text ? true : search_text.equals(""))
                return new ModelAndView(attributes, "./moustache/emptysearch.moustache");

            SearchResponse searchResponse = client.prepareSearch("users")
                    .setQuery(QueryBuilders.wildcardQuery("name", "*" + search_text + "*"))
                    .setSize(10)
                    .execute()
                    .actionGet();

            List<User> users = new LinkedList<>();

            searchResponse
                    .getHits()
                    .forEach(
                        (SearchHit h) -> {
                            users.add(User.findById(h.getId()));
                        }
                    );

            attributes.put("users", users);

            return new ModelAndView(attributes, "./moustache/usersearch_response.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }
}
