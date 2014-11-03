package com.unrc.app;

import com.unrc.app.models.User;
import com.unrc.app.models.City;
import com.unrc.app.models.Vehicle;
import com.unrc.app.models.Post;
import com.unrc.app.models.Question;
import com.unrc.app.models.Answer;

import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnswerTest {
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
        
        Vehicle vehicle = new Vehicle();
        vehicle
            .brand("Ford")
            .name("Ka")
            .year("2007")
            .plate("GDQ202")
            .setParent(user);
        vehicle.saveIt();
        
        Post post = new Post();
        post
            .text("Vendo Peugeot Partner 2011")
            .price("28000")
            .setParents(user, vehicle);
        post.saveIt();
        
        Question question = new Question();
        
        the(question).shouldNotBe("valid");
        the(question.errors().get("question")).shouldBeEqual("value is missing");
        the(question.errors().get("post_id")).shouldBeEqual("value is missing");
        the(question.errors().get("user_id")).shouldBeEqual("value is missing");
        
        question
            .question("Me lo vendes?")
            .setParents(user, post);
        question.saveIt();
        
        Answer answer = new Answer();
        
        the(answer).shouldNotBe("valid");
        the(answer.errors().get("answer")).shouldBeEqual("value is missing");
        the(answer.errors().get("question_id")).shouldBeEqual("value is missing");
        
        answer
            .answer("Si, por supuesto.")
            .setParent(question);
        answer.saveIt();
        
        the(answer).shouldBe("valid");
    }
}
