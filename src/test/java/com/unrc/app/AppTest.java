package com.unrc.app;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    AdministratorTest.class,
    AnswerTest.class,
    BikeTest.class,
    CarTest.class,
    CityTest.class,
    OtherTest.class,
    PhoneTest.class,
    PostTest.class,
    QuestionTest.class,
    RateTest.class,
    TruckTest.class,
    UserTest.class,
    VehicleTest.class,
})
public class AppTest {
    @BeforeClass
    public static void setUpClass() {
        System.out.println("Master setup");
    }

    @AfterClass public static void tearDownClass() {
        System.out.println("Master tearDown");
    }
}
