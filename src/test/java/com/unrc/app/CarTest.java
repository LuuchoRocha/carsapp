package com.unrc.app;

import com.unrc.app.models.Car;
import com.unrc.app.models.City;
import com.unrc.app.models.User;

import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CarTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("CarTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("CarTest tearDown");
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
        
        Car car = new Car();

        the(car).shouldNotBe("valid");
        the(car.errors().get("name")).shouldBeEqual("value is missing");
        the(car.errors().get("brand")).shouldBeEqual("value is missing");
        the(car.errors().get("year")).shouldBeEqual("value is missing");
        the(car.errors().get("passengers")).shouldBeEqual("value is missing");

        car
            .passengers(4)
            .name("Ka")
            .brand("Ford")
            .year("2007")
            .plate("GDQ202")
            .setParent(user);
        car.saveIt();
        
        the(car).shouldBe("valid");
    }
}
