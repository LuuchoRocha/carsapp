package com.unrc.app;

import com.unrc.app.models.User;
import com.unrc.app.models.City;
import com.unrc.app.models.Other;

import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OtherTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("OtherTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("OtherTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        User user = new User();
        Other other = new Other();
        City city = new City();
        
        city.set("name", "Rio IV", "state", "Cordoba", "country", "Argentina", "postcode", "5800");
        city.saveIt();
        
        user.set("first_name", "John", "last_name", "Doe", "pass", "12345", "email", "example@email.com", "address", "Street One 123");
        user.setParent(city);
        user.saveIt();

        the(other).shouldNotBe("valid");
        the(other.errors().get("name")).shouldBeEqual("value is missing");
        the(other.errors().get("brand")).shouldBeEqual("value is missing");
        the(other.errors().get("year")).shouldBeEqual("value is missing");

        other.set("name", "Raptor", "brand", "Yamaha", "year", "2007");
        other.setParent(user);
        
        the(other).shouldBe("valid");
    }
}
