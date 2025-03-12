package listener;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RetryListenerTestNG implements IRetryAnalyzer, ITestListener {

    private final int MAX_RETRIES = 2;
    private int count = 0;
    private static Set<String> failedTestNames = new HashSet<>();

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (count < MAX_RETRIES) {
            count++;
            return true;
        }
        return false;
    }

    private void addFailureSet(ITestResult result){
        String testClass = result.getTestClass().getName();
        String testName = result.getName();
        String testToWrite = String.format("--tests %s.%s", testClass, testName);
        failedTestNames.add(testToWrite);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        addFailureSet(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        addFailureSet(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        addFailureSet(result);
    }

    @SneakyThrows
    public static void saveFailedTest(){
        String output = System.getProperty("user.dir") + "/src/test/resources/FailedTest.txt";
        String result = String.join(" ", failedTestNames);
        FileUtils.writeStringToFile(new File(output), result);
    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}