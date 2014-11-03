package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import com.unrc.app.models.Bike;
import com.unrc.app.models.Car;
import com.unrc.app.models.Other;
import com.unrc.app.models.Truck;
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

public class VehicleController {
    
    public static ModelAndView getVehicles(Request request, Response response){
            Map<String, Object> attributes = new HashMap<>();
            List<Vehicle> vehicles = Vehicle.findAll();

            attributes.put("vehicles", vehicles);

            return new ModelAndView(attributes, "./moustache/vehicles.moustache");
    }
    
    public static ModelAndView getMyvehicles(Request request, Response response){
        if (null != VisitorController.existsSession(request)) {
            Map<String, Object> attributes = new HashMap<>();
            String email = request.session(false).attribute("email");
            List<Vehicle> vehicles = User.findByEmail(email).getAll(Vehicle.class);

            attributes.put("vehicles", vehicles);

            return new ModelAndView(attributes, "./moustache/myvehicles.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static ModelAndView getVehiclesNew(Request request, Response response){
        if(null != VisitorController.existsSession(request)) {
            return new ModelAndView(null, "./moustache/newvehicle.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static String postVehicles(Request request, Response response){
        String name = request.queryParams("name");
        String brand = request.queryParams("brand");
        String year = request.queryParams("year");
        String plate = request.queryParams("plate");
        String type = request.queryParams("type");
        Integer user_id = request.session(false).attribute("user_id");

        String body = "";
        boolean exit = (name.equals("") || brand.equals("") || (null == Integer.valueOf(year)) || plate.equals(""));

        if (!exit){
            switch(type) {
                case "car":
                    Integer passengers = Integer.valueOf(request.queryParams("passengers"));
                    Car c = new Car();
                    c
                        .passengers(passengers)
                        .name(name)
                        .year(year)
                        .brand(brand)
                        .plate(plate)
                        .setParent(User.findById(user_id));
                    exit = c.saveIt();
                    break;
                case "bike":
                    Integer displacement = Integer.valueOf(request.queryParams("displacement"));
                    Bike b = new Bike();
                    b
                        .displacement(displacement)
                        .name(name)
                        .year(year)
                        .brand(brand)
                        .plate(plate)
                        .setParent(User.findById(user_id));
                    exit = b.saveIt();
                    break;
                case "truck":
                    Integer max_load = Integer.valueOf(request.queryParams("max_load"));
                    Truck t = new Truck();
                    t
                        .maxLoad(max_load)
                        .name(name)
                        .year(year)
                        .brand(brand)
                        .plate(plate)
                        .setParent(User.findById(user_id));
                    exit = t.saveIt();
                    break;
                case "other":
                    Other o = new Other();
                    o
                        .name(name)
                        .year(year)
                        .brand(brand)
                        .plate(plate)
                        .setParent(User.findById(user_id));
                    exit = o.saveIt();
                    break;
            }
            if (exit == true) {
                body += "<body><script type='text/javascript'>";
                body += "alert('El vehiculo fue agregado.'); document.location = '/';";
                body += "</script></body>";
            }
            else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El vehiculo no pudo ser agregado.'); document.location = '/';";
                body += "</script></body>";
            }
        } else {
            body += "<body><script type='text/javascript'>";
            body += "alert('El registro no pudo completarse porque algun campo estaba vacio!'); document.location = '/';";
            body += "</script></body>";
        }
        return body;
    }
    
    public static ModelAndView getVehicleDel(Request request, Response response){
        if(null != VisitorController.existsSession(request)) {
            Map<String, Object> attributes = new HashMap<>();

            String email = request.session(false).attribute("email");

            List<Vehicle> vehicles = User.findByEmail(email).getAll(Vehicle.class);

            attributes.put("vehicles", vehicles);

            return new ModelAndView(attributes, "./moustache/vehicledel.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static String deleteVehicles(Request request, Response response){
        String body = "";

        Vehicle v = Vehicle.findById(request.params(":id"));

        if (null != v) {
            v.deleteCascade();
            body += "El vehiculo fue correctamente eliminado";
        } else {
            body += "El vehiculo no fue encontrado en la base de datos!";
        }
        return body;
    }
    
    public static ModelAndView getVehicleSearch(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 1) {
            return new ModelAndView(null, "./moustache/vehiclesearch.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static ModelAndView getVehicleSearchResponse(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) >= 1) {
            Map<String, Object> attributes = new HashMap<>();

            Client client = ElasticSearchController.client();

            String search_text = request.queryParams("search_text").toLowerCase();

            if (null == search_text ? true : search_text.equals(""))
                return new ModelAndView(attributes, "./moustache/emptysearch.moustache");

            SearchResponse searchResponse = client.prepareSearch("vehicles")
                    .setQuery(QueryBuilders.multiMatchQuery(search_text, "name", "brand"))
                    .setSize(10)
                    .execute()
                    .actionGet();

            List<Vehicle> vehicles = new LinkedList<>();

            searchResponse
                    .getHits()
                    .forEach(
                        (SearchHit h) -> {
                            vehicles.add(Vehicle.findById(h.getId()));
                        }
                    );

            attributes.put("vehicles", vehicles);

            return new ModelAndView(attributes, "./moustache/vehiclesearch_response.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
}
