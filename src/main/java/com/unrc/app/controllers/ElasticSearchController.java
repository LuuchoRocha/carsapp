package com.unrc.app.controllers;

import static com.unrc.app.controllers.VisitorController.existsSession;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.node.Node;
import spark.Request;
import spark.Response;

public class ElasticSearchController {    
    private static final Node node = org.elasticsearch.node.NodeBuilder
                                            .nodeBuilder()
                                            .clusterName("carsapp")
                                            .local(true)
                                            .node();
    
    public static Client client(){
        return node.client();
    }
    
    public static void deleteAllIndexs(){
        Client c = client();
        try {
            c.admin().indices().delete(new DeleteIndexRequest("admins")).actionGet();
        } catch (IndexMissingException e) {
        } finally {
            c.admin().indices().create(new CreateIndexRequest("admins"));
        }
        try {
            c.admin().indices().delete(new DeleteIndexRequest("users")).actionGet();
        } catch (IndexMissingException e) {
        } finally {
            c.admin().indices().create(new CreateIndexRequest("users"));
        }
        try {
            c.admin().indices().delete(new DeleteIndexRequest("posts")).actionGet();
        } catch (IndexMissingException e) {
        } finally {
            c.admin().indices().create(new CreateIndexRequest("posts"));
        }
        try {
            c.admin().indices().delete(new DeleteIndexRequest("vehicles")).actionGet();
        } catch (IndexMissingException e) {
        } finally {
            c.admin().indices().create(new CreateIndexRequest("vehicles"));
        }
    }
    
    public static String clearIndex(Request request, Response response){
        if(VisitorController.sessionLevel(existsSession(request)) == 3) {
            ElasticSearchController.deleteAllIndexs();
            return "Indices borrados";
        } else {
            return "Está usted perdido???";
        }
    }
}
