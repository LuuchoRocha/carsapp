package com.unrc.app;

import com.unrc.app.models.Administrator;
import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdministratorTest{
    
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("PostTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("PostTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        Administrator admin = new Administrator();
        
        the(admin).shouldNotBe("valid");
        the(admin.errors().get("email")).shouldBeEqual("value is missing");
        the(admin.errors().get("pass")).shouldBeEqual("value is missing");
        
        admin
                .email("sample@hotmail.com")
                .pass("321123");
        
        the(admin).shouldBe("valid");
    }
}
