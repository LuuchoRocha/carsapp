package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import com.unrc.app.models.Administrator;
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
import spark.Session;

public class AdministratorController {
    
    public static ModelAndView getAdminNew(Request request, Response response){
        Session s = VisitorController.existsSession(request);
        if(null != s ? s.attribute("email").equals("admin") : false) {
            return new ModelAndView(null, "./moustache/newadmin.moustache");
        } else {
            if (null == s) {
                return new ModelAndView(null, "./moustache/notlogged.moustache");
            } else {
                return new ModelAndView(null, "./moustache/notadmin.moustache");
            }
        }
    }
    
    public static String postAdmin(Request request, Response response){
        String body = "";
        String email = request.queryParams("email");
        String pass = request.queryParams("pass");
        if (null == email || null == pass) {
            body += "<body><script type='text/javascript'>";
            body += "alert('No puede haber ningun campo vacio.'); document.location = '/';";
            body += "</script></body>";
        } else {
            Boolean res = false;
            Administrator admin = new Administrator();
            try {
                res = admin
                        .email(email)
                        .pass(pass)
                        .saveIt();
            } catch (DBException e) {}
            if (res) {
                body += "<body><script type='text/javascript'>";
                body += "alert('Administrador correctamente registrado!.'); document.location = '/';";
                body += "</script></body>";
            } else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El administrador no pudo ser registrado!.'); document.location = '/';";
                body += "</script></body>";
            }
        }
        return body;
    }
    
    public static ModelAndView getAdminDel(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) == 3) {
            Map<String, Object> attributes = new HashMap<>();

            List<Administrator> admins = Administrator.findAll();
            admins = admins.subList(1, admins.size());

            attributes.put("administrators", admins);

            return new ModelAndView(attributes, "./moustache/admindel.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }

    public static ModelAndView getAdminSearch(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 2) {
            return new ModelAndView(null, "./moustache/adminsearch.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }
    
    public static ModelAndView getAdminSearchResponse(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 2) {
            Map<String, Object> attributes = new HashMap<>();

            Client client = ElasticSearchController.client();

            String search_text = request.queryParams("search_text").toLowerCase();

            if (null == search_text ? true : search_text.equals(""))
                return new ModelAndView(attributes, "./moustache/emptysearch.moustache");

            SearchResponse searchResponse = client.prepareSearch("admins")
                    .setQuery(QueryBuilders.wildcardQuery("email", "*" + search_text + "*"))
                    .setSize(10)
                    .execute()
                    .actionGet();

            List<Administrator> admins = new LinkedList<>();

            searchResponse
                    .getHits()
                    .forEach(
                        (SearchHit h) -> {
                            admins.add(Administrator.findById(h.getId()));
                        }
                    );

            attributes.put("administrators", admins);

            return new ModelAndView(attributes, "./moustache/adminsearch_response.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notadmin.moustache");
        }
    }
    
    public static String deleteAdmin(Request request, Response response){
        String body = "";
        if(VisitorController.sessionLevel(existsSession(request)) == 3) {
            Administrator admin = Administrator.findById(request.params(":id"));
            if(null != admin){
                admin.deleteCascade();
                body += "El administrador fue correctamente eliminado";
            } else {
                body += "El administrador no fue encontrado en la base de datos!";
            }
        } else {
            body += "Usted no tiene permisos para acceder al sitio que intenta";
        }
        return body;
    }
}
