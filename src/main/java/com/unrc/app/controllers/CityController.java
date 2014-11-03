package com.unrc.app.controllers;

import com.unrc.app.models.City;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class CityController {
    public static ModelAndView getCities(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();
        List<City> cities = City.all();

        attributes.put("cities", cities);

        return new ModelAndView(attributes, "./moustache/cities.moustache");
    }
    
    public static ModelAndView getCitiesNew(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();
        List<City> cities = City.all();

        attributes.put("cities_count", cities.size());
        attributes.put("cities", cities);

        return new ModelAndView(attributes, "./moustache/newcity.moustache");
    }
    
    public static String postCities(Request request, Response response){
        String body = "";
        String name = request.queryParams("name");
        String state = request.queryParams("state");
        String country = request.queryParams("country");
        String postcode = request.queryParams("postcode");

        if (!((name.equals(""))
            || (state.equals(""))
            || (country.equals(""))
            || (null == Integer.valueOf(postcode)))) {
                City c = new City();
                c
                    .name(name)
                    .state(state)
                    .country(country)
                    .postcode(postcode);
                if (c.saveIt()) {
                    body += "<body><script type='text/javascript'>";
                    body += "alert('Ciudad correctamente registrada.'); document.location = '/';";
                    body += "</script></body>";
                } else {
                    body += "<body><script type='text/javascript'>";
                    body += "alert('El servidor rechazo el registro (Quizas ya existe la ciudad?).'); document.location = '/';";
                    body += "</script></body>";
                }
        } else {
                    body += "<body><script type='text/javascript'>";
                    body += "alert('No puede haber ningun campo vacio.'); document.location = '/';";
                    body += "</script></body>";
        }
        return body;
    }
}
