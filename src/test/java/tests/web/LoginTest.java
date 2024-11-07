package tests.web;

import com.ideas2it.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testPositive() {

        int a = 1, b = 2;
        Assert.assertEquals(a + b, 4);
    }

    @Test
    public void testNegative() {
        int a = 1, b = 2;
        Assert.assertEquals(a + b, 3);
    }
}
