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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@Slf4j
public abstract class AbstractSeleniumTestSuite extends AbstractTestSuite {

    protected RemoteWebDriver webDriver;

    protected int waitTimeSeconds = 60;

    @Value("${vncproxy.enable:false}")
    private boolean enableVncProxy;

    @Value("${videorecording.enable:true}")
    private boolean enableVideoRecording = true;

    @Container
    private BrowserWebDriverContainer<?> webDriverContainer = new BrowserWebDriverContainer<>().withCapabilities(new ChromeOptions().addArguments("--disable-dev-shm-usage")).withNetworkAliases("chrome").withNetwork(Network.SHARED).withSharedMemorySize(0L);

    private static TcpProxy proxy;

    @BeforeEach
    public void setupBeforeTest() {
        if (enableVideoRecording) {
            webDriverContainer = webDriverContainer.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"), VncRecordingContainer.VncRecordingFormat.MP4);
        } else {
            webDriverContainer.withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, new File("target"));
        }
        webDriver = webDriverContainer.getWebDriver();
        webDriver.manage().window().maximize();

        if (enableVncProxy) {
            StaticTcpProxyConfig config = new StaticTcpProxyConfig(5900, webDriverContainer.getHost(), webDriverContainer.getMappedPort(5900));
            config.setWorkerCount(1);
            proxy = new TcpProxy(config);
            proxy.start();
            log.info("Running VNC proxy on vnc://localhost:5900, password: 'secret'"); // pragma: allowlist secret
        }
    }

    @AfterEach
    public void teardownAfterTest() {
        if (proxy != null) {
            try {
                proxy.shutdown();
            } catch (Exception e) {
                log.error("Failed to stop VNC proxy", e);
            }
        }
    }
}
