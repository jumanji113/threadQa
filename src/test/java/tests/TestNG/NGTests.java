package tests.TestNG;

import calc.CalcSteps;
import listener.RetryListenerTestNG;
import models.People;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;


@Listeners(RetryListenerTestNG.class)
public class NGTests {

    @BeforeSuite
    public void setAnalyzer(ITestContext context){
        for (ITestNGMethod testMethod : context.getAllTestMethods()) {
            testMethod.setRetryAnalyzerClass(RetryListenerTestNG.class);
        }
    }

    @AfterSuite
    public void saveFailed(){
        RetryListenerTestNG.saveFailedTest();
    }

    @Test(groups = {"sum1"})
    public void sumTestNG(){
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-10));
    }

    @Test(groups = {"sum2"})
    public void sumTestNG2(){
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-20));
    }

    @DataProvider(name = "testUser")
    public Object[] dataWithUsers(){
        People stas = new People("stas", 25, "male");
        People kate = new People("kte", 29, "female");
        People alex = new People("alex", 30, "male");
        return new Object[]{stas, kate, alex};
    }

    @Test(dataProvider = "testUser")
    public void testUsersWithRole(People people){
        System.out.println(people.getName());
        Assert.assertTrue(people.getAge() > 18);
        //some magic
        Assert.assertTrue(people.getName().contains("a"));

    }

    @DataProvider(name = "ips")
    public Object[] testIpAdress(){
        List<String> ips = new ArrayList<>();
        ips.add("127.0.0.1");
        ips.add("localhost");
        ips.add("58.43.92.1");
//        return ips.stream()
//                .map(x -> new Object[]{x})
//                .toArray(Object[][]::new);
        return ips.toArray();
    }

    @Test(dataProvider = "ips")
    public void ipTest(String ip){
        System.out.println(ip);
        Assert.assertTrue(ip.matches("^([0-9]+(\\.|$)){4}"));
    }

    @Test(dataProviderClass = DataTestArguments.class, dataProvider = "argsForCalc")
    public void calcTest(int a, int b, int c){
        Assert.assertEquals(c, a + b);
    }

    @Test(dataProviderClass = DataTestArguments.class, dataProvider = "diffArg")
    public void someMagic(int a, String str){
        Assert.assertEquals(str, convertInt(a));
    }

    private String convertInt(int a){
        switch (a){
            case 1:
                return "one";
            case 2:
                return "two";
            case 5:
                return "five";
            default:
                return null;
        }
    }
}