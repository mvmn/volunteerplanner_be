package x.mvmn.testcon.vp.e2etests.web.pages;

import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

@RequiredArgsConstructor
@SuperBuilder
public abstract class AbstractPageObject {
    protected final PageObjectContext context;

    protected WebDriver getWebDriver() {
        return context.getWebDriver();
    }

    protected String getBaseUrl() {
        return context.getBaseUrl();
    }

    protected <T> T waitFor(Function<WebDriver, T> isTrue, int waitTimeSeconds) {
        return waitFor(isTrue, Duration.ofSeconds(waitTimeSeconds));
    }

    protected <T> T waitFor(Function<WebDriver, T> isTrue, Duration waitTime) {
        Wait<WebDriver> wait = new WebDriverWait(context.getWebDriver(), waitTime);
        return wait.until(isTrue);
    }
}
