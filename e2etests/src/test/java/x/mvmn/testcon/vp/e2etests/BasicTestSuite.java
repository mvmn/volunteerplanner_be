package x.mvmn.testcon.vp.e2etests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BasicTestSuite extends AbstractSeleniumTestSuite {

    @Test
    public void testLogin() throws InterruptedException {
        webDriver.get(sharedTestEnv.getVPInternalUrl());
        Thread.sleep(5000);
    }
}
