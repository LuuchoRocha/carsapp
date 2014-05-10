package com.unrc.app.models;

import org.javalite.activejdbc.Model;

/**
 *
 * @author lucho
 */
public class Car extends Vehicle {
    static {
        validatePresenceOf("name","brand","year", "passengers");
    }

    @Override
    public boolean saveIt(){
        set("type", "car");
        return super.saveIt();
    }
}
