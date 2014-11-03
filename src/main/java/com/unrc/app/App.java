package com.unrc.app;

import com.unrc.app.controllers.ElasticSearchController;
import com.unrc.app.controllers.AdministratorController;
import com.unrc.app.controllers.CityController;
import com.unrc.app.controllers.PhoneController;
import com.unrc.app.controllers.PostController;
import com.unrc.app.controllers.UserController;
import com.unrc.app.controllers.VehicleController;
import com.unrc.app.controllers.VisitorController;
import org.javalite.activejdbc.Base;
import spark.Spark;
import static spark.Spark.*;

public class App {
    
    private static final MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        
    public static void main(String[] args) { 
        externalStaticFileLocation("./public"); // Static files 
        ElasticSearchController.client(); //This starts ElasticSearch server (creating a Client object)
        
        // <editor-fold desc="Spark filters">
        Spark.before((request, response) -> {
            if (!Base.hasConnection()) 
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_development", "root", "");
        });
        
        Spark.after((request, response) -> {
            if (Base.hasConnection()) 
            Base.close();
        });
        //</editor-fold>
        // <editor-fold desc="Sparks for visitor and session management">
        get("/", 
                (request, response) -> VisitorController.getIndex(request, response),
                templateEngine
        );                
        
        get("/login",
                (request, response) -> VisitorController.getLogin(request, response),
                templateEngine
        );
        
        post("/login", 
                (request, response) -> VisitorController.postLogin(request, response)
        );
        
        get("/logout", 
                (request, response) -> VisitorController.getLogout(request, response)
        );
        //</editor-fold>
        // <editor-fold desc="Sparks for users">
        get("/users", 
                (request, response) -> UserController.getUsers(request, response), 
                templateEngine
        );
        
        get("/users/new", 
                (request, response) -> UserController.getUsersNew(request, response),
                templateEngine
        ); 
        
        post("/users", 
                (request, response) ->  UserController.postUsers(request, response)
        );
        
        get("/users/del", 
                (request, response) -> UserController.getUsersDel(request, response),
                templateEngine
        );  

        delete("/users/:id", 
                (request, response) -> UserController.deleteUsers(request, response)
        );
        
        get("/users/search", 
                (request, response) -> UserController.getUsersSearch(request, response),
                templateEngine
        );
        
        get("/users/search/response", 
                (request, response) -> UserController.getUsersSearchResponse(request, response),
                templateEngine
        );
        //</editor-fold>
        // <editor-fold desc="Sparks for vehicles">
        get("/vehicles",
                (request, response) -> VehicleController.getVehicles(request, response),
                templateEngine
        );
        
        get("/myvehicles",
                (request, response) -> VehicleController.getMyvehicles(request, response),
                templateEngine
        );
        
        get("/vehicles/new", 
                (request, response) -> VehicleController.getVehiclesNew(request, response),
                templateEngine
        );
        
        post("/vehicles", 
                (request, response) -> VehicleController.postVehicles(request, response)
        );     
        
        get("/vehicles/del", 
                (request, response) -> VehicleController.getVehicleDel(request, response),
                templateEngine
        );
        
        delete("/vehicles/:id", 
                (request, response) -> VehicleController.deleteVehicles(request, response));
        
        get("/vehicles/search", 
                (request, response) -> VehicleController.getVehicleSearch(request, response),
                templateEngine
        );
        
        get("/vehicles/search/response", 
                (request, response) -> VehicleController.getVehicleSearchResponse(request, response),
                templateEngine
        );
        // </editor-fold>
        // <editor-fold desc="Sparks for posts">
        get("/posts",
                (request, response) -> PostController.getPosts(request, response),
                templateEngine
        );
        
        get("/myposts",
                (request, response) -> PostController.getMyposts(request, response),   
                templateEngine
        );
        
        get("/posts/new", 
                (request, response) -> PostController.getPostsNew(request, response),
                templateEngine
        );
        
        post("/posts", 
                (request, response) -> PostController.postPosts(request, response)
        );
        
        get("/posts/search", 
                (request, response) -> PostController.getPostsSearch(request, response),
                templateEngine
        );
        
        get("/posts/search/response", 
                (request, response) -> PostController.getPostsSearchResponse(request, response),
                templateEngine
        );
        
        get("/posts/del", 
                (request, response) -> PostController.getPostsDel(request, response),
                templateEngine
        );
        
        delete("/posts/:id", 
                (request, response) -> PostController.deletePosts(request, response)
        );
        //</editor-fold>
        // <editor-fold desc="Sparks for administrators">
        get("/administrators/new", 
                (request, response) -> AdministratorController.getAdminNew(request, response),
                templateEngine
        );
        
        post("/administrators",
                (request, response) -> AdministratorController.postAdmin(request, response)
        );
        
        get("/administrators/del", 
                (request, response) -> AdministratorController.getAdminDel(request, response),
                templateEngine
        );
        
        get("/administrators/search", 
                (request, response) -> AdministratorController.getAdminSearch(request, response),
                templateEngine
        );
        
        get("/administrators/search/response", 
                (request, response) -> AdministratorController.getAdminSearchResponse(request, response),
                templateEngine
        );
        
        delete("/administrators/:id", 
                (request, response) -> AdministratorController.deleteAdmin(request, response)
        );
        //</editor-fold>
        // <editor-fold desc="Sparks for city">
        get("/cities",
                (request, response) -> CityController.getCities(request, response),
                templateEngine
        );
        
        get("/cities/new", 
                (request, response) -> CityController.getCitiesNew(request, response),
                templateEngine
        );
        
        post("/cities", (request, response) -> 
                CityController.postCities(request, response)
        );
        // </editor-fold>
        // <editor-fold desc="Sparks for phones">
        get("/phones",
                (request, response) -> PhoneController.getPhones(request, response),
                templateEngine
        );
        
        get("/myphones",
                (request, response) -> PhoneController.getMyphones(request, response),
                templateEngine
        );
        
        get("/phones/new", 
                (request, response) -> PhoneController.getPhonesNew(request, response),
                templateEngine
        );
                
        post("/phones", 
                (request, response) -> PhoneController.postPhones(request, response)
        );
        //</editor-fold>
        // <editor-fold desc="Spark for ElasticSearch">
        get("/elastic/clear", 
                (req, res) -> ElasticSearchController.clearIndex(req, res)
        );
        //</editor-fold>
    }
}
