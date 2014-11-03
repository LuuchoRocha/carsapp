package com.unrc.app;

import com.unrc.app.models.User;
import com.unrc.app.models.City;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.javalite.test.jspec.JSpec.the;

public class CityTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        City city = new City();

        the(city).shouldNotBe("valid");
        the(city.errors().get("name")).shouldBeEqual("value is missing");
        the(city.errors().get("state")).shouldBeEqual("value is missing");
        the(city.errors().get("country")).shouldBeEqual("value is missing");
        the(city.errors().get("postcode")).shouldBeEqual("value is missing");
        
        city
            .name("Rio IV")
            .state("Cordoba")
            .country("Argentina")
            .postcode("5800")
            .saveIt();
        
        the(city).shouldBe("valid");
    }
}
