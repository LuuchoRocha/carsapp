package com.unrc.app;

import com.unrc.app.models.City;
import com.unrc.app.models.Truck;
import com.unrc.app.models.User;

import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author lucho
 */
public class TruckTest {
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

        Truck truck = new Truck();
        the(truck).shouldNotBe("valid");
        the(truck.errors().get("name")).shouldBeEqual("value is missing");
        the(truck.errors().get("brand")).shouldBeEqual("value is missing");
        the(truck.errors().get("year")).shouldBeEqual("value is missing");
        the(truck.errors().get("plate")).shouldBeEqual("value is missing");
        the(truck.errors().get("max_load")).shouldBeEqual("value is missing");

        //Please, first write the specific type of vehicle attributes(max_load)
        //and after the global attributes(name, brand, etc...)
        truck
            .maxLoad(2000)
            .brand("Volvo")
            .name("Actros")
            .year("2007")
            .plate("JHI702")
            .setParent(user);
        truck.saveIt();
        
        the(truck).shouldBe("valid");
    }
}
