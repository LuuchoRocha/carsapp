package com.unrc.app;

import com.unrc.app.models.City;
import com.unrc.app.models.Phone;
import com.unrc.app.models.Phone.PhoneType;
import com.unrc.app.models.User;
import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhoneTest{
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
            .address("Sobremonte 123");
        user.setParent(city);
        user.saveIt();
        
        Phone phone = new Phone();
        
        the(phone).shouldNotBe("valid");
        the(phone.errors().get("type")).shouldBeEqual("value is missing");
        the(phone.errors().get("num")).shouldBeEqual("value is missing");
        the(phone.errors().get("user_id")).shouldBeEqual("value is missing");
        
        phone
            .type(PhoneType.home)
            .num("4628083")
            .setParent(user);
        phone.saveIt();
        
        the(phone).shouldBe("valid");
    }
}
