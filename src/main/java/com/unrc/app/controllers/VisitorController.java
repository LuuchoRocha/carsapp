package com.unrc.app.controllers;

import com.unrc.app.models.Administrator;
import com.unrc.app.models.User;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class VisitorController {
    
    public static ModelAndView getIndex(Request request, Response response){
        Map<String, Object> attributes = new HashMap<>();
        Session session = existsSession(request);
        String email;
        int sessionLevel = sessionLevel(session);
        switch(sessionLevel) {
            case 0:
                return new ModelAndView(attributes, "./moustache/index_guest.moustache");
            case 1:
                email = session.attribute("email");
                attributes.put("email", email);
                attributes.put("username", User.findByEmail(email).toString());
                return new ModelAndView(attributes, "./moustache/index_user.moustache");
            case 2:
                email = session.attribute("email");
                attributes.put("email", email);
                attributes.put("username", Administrator.findByEmail(email).toString());
                return new ModelAndView(attributes, "./moustache/index_admin.moustache");
            case 3:
                email = session.attribute("email");
                attributes.put("email", email);
                attributes.put("username", Administrator.findByEmail(email).toString());
                return new ModelAndView(attributes, "./moustache/index_webmaster.moustache");
        }
        return new ModelAndView(attributes, "./moustache/index_guest.moustache");
    }
    
    public static ModelAndView getLogin(Request request, Response response){
        if(null == existsSession(request)) { 
            return new ModelAndView(null, "./moustache/login.moustache");
        } else {
            Map<String, Object> attributes = new HashMap<>();
            String email = existsSession(request).attribute("email");
            attributes.put("email", email);
            return new ModelAndView(null, "./moustache/alreadylogged.moustache");
        }
    }
    
    public static String postLogin(Request request, Response response){
        String body = "";
        String email = request.queryParams("email");
        String pwd = request.queryParams("pwd");
        User u = User.findByEmail(email);
        if (u != null ? u.pass().equals(pwd) : false) {
            Session session = request.session(true);
            session.attribute("email", email);
            session.attribute("user_id", u.getId());
            session.attribute("level", 1);
            session.maxInactiveInterval(120);
            body += "<body><script type='text/javascript'>";
            body += "alert('Bienvenido " + u + "!'); document.location = '/';";
            body += "</script></body>";
            return body;
        } else {
            Administrator a = Administrator.findByEmail(email);
            if (a != null ? a.pass().equals(pwd) : false) {
                Session session = request.session(true);
                session.attribute("email", email);

                if (email.equals("admin"))
                    session.attribute("level", 3);
                else
                    session.attribute("level", 2);

                session.maxInactiveInterval(120);
                body += "<body><script type='text/javascript'>";
                body += "alert('Bienvenido administrador'); document.location = '/';";
                body += "</script></body>";
                return body;
            } else {
                body += "<body><script type='text/javascript'>";
                body += "alert('El email indicado no existe o la contraseña no coincide.'); document.location = '/';";
                body += "</script></body>";
                return body;
            }
        }
    }
    
    public static String getLogout(Request request, Response response){
        String body = "";
        Session session = VisitorController.existsSession(request);
        if (null == session) {
                body += "<body><script type='text/javascript'>";
                body += "alert('No hay ninguna sesion abierta.'); document.location = '/';";
                body += "</script></body>";
        } else {
            session.invalidate();
                body += "<body><script type='text/javascript'>";
                body += "alert('Adios! Regresa pronto.'); document.location = '/';";
                body += "</script></body>";
        }
        return body;
    }

    /**
     * Metodo auxiliar utilizado para comprobar
     * si una sesion es de un usuario administrator
     * Es necesitada para poder ocultar y mostrar ciertas
     * opciones en las templates moustache
     * @param session the possible administrator session
     * @return true if exists an admin with the email of
     * the current user, else return false.
     */
    public static int sessionLevel(Session session) {
        if (null != session) {
            return session.attribute("level");
        }
        return 0;
    }

    /**
     * Metodo auxiliar para controlar si un
     * usuario ya inicio sesion en el sitio.
     * @param request the user request
     * @return iff session exists, returns the session
     * else, returns null.
     */
    public static Session existsSession(Request request) {
        return request.session(false);
    }
}
