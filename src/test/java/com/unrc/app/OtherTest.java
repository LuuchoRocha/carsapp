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
        
        Other other = new Other();

        the(other).shouldNotBe("valid");
        the(other.errors().get("name")).shouldBeEqual("value is missing");
        the(other.errors().get("brand")).shouldBeEqual("value is missing");
        the(other.errors().get("year")).shouldBeEqual("value is missing");

        other
            .name("Raptor")
            .brand("Yamaha")
            .year("2007")
            .plate("ABC123")
            .setParent(user);
        other.saveIt();
        
        the(other).shouldBe("valid");
    }
}
