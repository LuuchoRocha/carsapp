package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import com.unrc.app.models.Phone;
import com.unrc.app.models.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class PhoneController {
    
    public static String postPhones(Request request, Response response){
        String type = request.queryParams("type");
        Phone.PhoneType phonetype = Phone.PhoneType.personal;
        String num = request.queryParams("number");
        Integer user_id = request.session(false).attribute("user_id");

        String body = "";

        switch(type) {
            case "home":
                phonetype = Phone.PhoneType.home;
                break;
            case "personal":
                phonetype = Phone.PhoneType.personal;
                break;
            case "work":
                phonetype = Phone.PhoneType.work;
                break;
        }

        if (null != (Integer.valueOf(num))) {
            User u = User.findById(user_id);
            Phone p = new Phone();
            p
                .num(num)
                .type(phonetype)
                .setParent(u);

            if (p.saveIt()) {
                body += "<body><script type='text/javascript'>";
                body += "alert('El telefono fue correctamente agregado.'); document.location = '/';";
                body += "</script></body>";
            }
            else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El telefono no pudo ser agregado.'); document.location = '/';";
                body += "</script></body>";
            }
        } else {
            body += "<body><script type='text/javascript'>";
            body += "alert('El telefono no pudo ser agregado porque algun campo estaba vacio.'); document.location = '/';";
            body += "</script></body>";
        }
        return body;
    }
    public static ModelAndView getPhones(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();
        List<Phone> phones = Phone.all();

        attributes.put("phones", phones);

        return new ModelAndView(attributes, "./moustache/phones.moustache");
    }
    
    public static ModelAndView getMyphones(Request request, Response response){
        if (null != VisitorController.existsSession(request)) {
            Map<String, Object> attributes = new HashMap<>();
            String email = request.session(false).attribute("email");
            List<Phone> phones = User.findByEmail(email).getAll(Phone.class);

            attributes.put("phones", phones);

            return new ModelAndView(attributes, "./moustache/myphones.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
    
    public static ModelAndView getPhonesNew(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) == 1) {
            return new ModelAndView(null, "./moustache/newphone.moustache");
        } else {
            return new ModelAndView(null, "./moustache/notlogged.moustache");
        }
    }
}
