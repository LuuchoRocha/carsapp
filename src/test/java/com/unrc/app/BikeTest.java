package com.unrc.app;

import com.unrc.app.models.Bike;
import com.unrc.app.models.User;
import com.unrc.app.models.City;

import static org.javalite.test.jspec.JSpec.the;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BikeTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("BikeTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("BikeTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        City city = new City();
        city
            .name("Rio IV")
            .state("Cordoba")
            .country("Argentina")
            .postcode("5800")
            .saveIt();
        
        User user = new User();
        user
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@hotmail.com")
            .pass("123456")
            .address("Sobremonte 123")
            .setParent(city);
        user.saveIt();
        
        Bike bike = new Bike();
        
        the(bike).shouldNotBe("valid");
        the(bike.errors().get("name")).shouldBeEqual("value is missing");
        the(bike.errors().get("brand")).shouldBeEqual("value is missing");
        the(bike.errors().get("year")).shouldBeEqual("value is missing");
        the(bike.errors().get("displacement")).shouldBeEqual("value is missing");

        bike
            .displacement(250)
            .name("XTZ")
            .brand("Yamaha")
            .year("2007")
            .plate("ABC321")
            .setParent(user);
        bike.saveIt();
        
        the(bike).shouldBe("valid");
    }
}
