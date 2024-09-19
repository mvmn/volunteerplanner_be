package x.mvmn.testcon.vp.e2etests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import x.mvmn.testcon.vp.e2etests.web.pages.WelcomePage;

import java.nio.charset.StandardCharsets;

@Slf4j
public class BasicTestSuite extends AbstractSeleniumTestSuite {


    @Test
    public void testLogin() throws Exception {
        var response = callVP("POST",
                              "/api/v1/users",
                              "{\"phoneNumber\":\"123456789012\",\"displayName\":\"testuser\",\"password\":\"12345\",\"role\":\"root\"}".getBytes(
                                      StandardCharsets.UTF_8));
        Assertions.assertEquals(201, response.statusCode(), new String(response.body(), StandardCharsets.UTF_8));

        var welcomePage = new WelcomePage(poCtx());
        welcomePage.open();
        welcomePage.gotoLogin().login("123456789012", "12345");
        System.out.println("Done!");
    }
}
