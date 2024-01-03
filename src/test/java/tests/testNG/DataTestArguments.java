package tests.testNG;

import org.testng.annotations.DataProvider;

public class DataTestArguments {
    @DataProvider(name = "argsForCalc")
    public Object[][] calcData(){
        return new Object[][]{
                {1,2,3},
                {2,2,4},
                {3,3,6},
                {1,1,20}
        };
    }

    @DataProvider(name = "diffArgs")
    public Object[][] diffArgsObject(){
        return new Object[][]{
                {1, "one"},
                {5, "five"}
        };
    }
}
