package x.mvmn.testcon.vp.e2etests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import x.mvmn.testcon.vp.e2etests.web.pages.WelcomePage;

import java.nio.charset.StandardCharsets;

import static x.mvmn.testcon.vp.e2etests.util.SQLUtil.convertResultSetToList;

@Slf4j
public class BasicTestSuite extends AbstractSeleniumTestSuite {

    @Test
    public void testLogin() throws Exception {
        Assertions.assertEquals(1,
                                queryDb(stmt -> convertResultSetToList(stmt.executeQuery(
                                        "select phone_number from \"user\""))).size());
        var response = callVP("POST",
                              "/api/v1/users",
                              "{\"phoneNumber\":\"123456789012\",\"displayName\":\"testuser\",\"password\":\"12345\",\"role\":\"root\"}".getBytes(
                                      StandardCharsets.UTF_8));
        Assertions.assertEquals(201, response.statusCode(), new String(response.body(), StandardCharsets.UTF_8));
        var userData = queryDb(stmt -> convertResultSetToList(stmt.executeQuery(
                "SELECT phone_number, display_name FROM \"user\"")));
        Assertions.assertEquals(2, userData.size());
        Assertions.assertEquals("123456789012", userData.get(1).get(0));
        Assertions.assertEquals("testuser", userData.get(1).get(1));

        var welcomePage = new WelcomePage(poCtx());
        welcomePage.open();
        welcomePage.gotoLogin().login("123456789012", "12345");
    }
}
