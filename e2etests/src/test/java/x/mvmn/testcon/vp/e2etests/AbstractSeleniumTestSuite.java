package x.mvmn.testcon.vp.e2etests;

import com.github.terma.javaniotcpproxy.StaticTcpProxyConfig;
import com.github.terma.javaniotcpproxy.TcpProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.VncRecordingContainer;
import x.mvmn.testcon.vp.e2etests.web.pages.PageObjectContext;

import java.io.File;
import java.util.List;

@Slf4j
public abstract class AbstractSeleniumTestSuite extends AbstractTestSuite {

    protected RemoteWebDriver webDriver;

    @Value("${videorecording.enable:true}")
    private boolean enableVideoRecording = true;

    @Value("${seleniumhub.port:}")
    private String seleniumHubPort;

    private BrowserWebDriverContainer<?> webDriverContainer = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions().addArguments("--disable-dev-shm-usage"))
            .withNetworkAliases("chrome")
            .withSharedMemorySize(0L);

    @BeforeEach
    public void setupBeforeTest() {
        if (webDriver == null) {
            webDriverContainer.withNetwork(sharedTestEnv.getNetwork());
            if (enableVideoRecording) {
                webDriverContainer = webDriverContainer.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
                                                                          new File("target"),
                                                                          VncRecordingContainer.VncRecordingFormat.MP4);
            } else {
                webDriverContainer.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP,
                                                     new File("target"));
            }
            if (seleniumHubPort != null && !seleniumHubPort.isBlank()) {
                webDriverContainer.setPortBindings(List.of("" + Integer.parseInt(seleniumHubPort) + ":4444"));
            }
            webDriverContainer.start();
        }
        webDriver = webDriverContainer.getWebDriver();
        webDriver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        webDriver.close();
    }

    protected PageObjectContext poCtx() {
        return PageObjectContext.of(webDriver, sharedTestEnv.getVPInternalUrl());
    }
}
