package com.unrc.app;

import com.unrc.app.models.Vehicle;
import com.unrc.app.models.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.javalite.test.jspec.JSpec.the;

/**
 *
 * @author lucho
 */
public class VehicleTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("VehicleTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("VehicleTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        Vehicle vehicle = new Vehicle();

        the(vehicle).shouldNotBe("valid");
        the(vehicle.errors().get("name")).shouldBeEqual("value is missing");
        the(vehicle.errors().get("brand")).shouldBeEqual("value is missing");
        the(vehicle.errors().get("year")).shouldBeEqual("value is missing");

        vehicle.set("name", "Partner", "brand", "Peugeot", "year", "2011");

        the(vehicle).shouldBe("valid");
    }
    
    @Test
    public void oneVehicleBelongsToUser(){
        User user = new User();
        Vehicle vehicle = new Vehicle();
        
        the(vehicle).shouldNotBe("valid");
        
        user.set("first_name", "John", "last_name", "Doe", "pass", "12345", "email", "example@email.com", "address", "Street One 123", "city_postcode", "4800");
        user.saveIt();
        
        vehicle.set("name", "Partner", "brand", "Peugeot", "year", "2011");
        vehicle.setParent(user);
        vehicle.saveIt();
        
        the(vehicle).shouldBe("valid");
    }
}
