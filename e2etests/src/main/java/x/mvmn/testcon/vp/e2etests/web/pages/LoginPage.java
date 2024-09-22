package x.mvmn.testcon.vp.e2etests.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends AbstractPageObject {
    public LoginPage(PageObjectContext context) {
        super(context);
    }

    public boolean login(String username, String password) {
        WebDriver webDriver = getWebDriver();
        webDriver.findElement(By.id("phoneNumber")).sendKeys(username);
        webDriver.findElement(By.id("password")).sendKeys(password);
        webDriver.findElement(By.xpath("//button[contains(text(), 'Відправити')]")).click();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
        wait.until(wd -> wd.findElements(By.className("MuiAvatar-root")).size() > 0);
        return webDriver.findElements(By.className("MuiAvatar-root")).size() > 0;
    }
}
