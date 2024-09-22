package x.mvmn.testcon.vp.e2etests.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractPageObject {
    public LoginPage(PageObjectContext context) {
        super(context);
    }

    public boolean login(String username, String password) {
        WebDriver webDriver = getWebDriver();
        webDriver.findElement(By.id("phoneNumber")).sendKeys(username);
        webDriver.findElement(By.id("password")).sendKeys(password);
        webDriver.findElement(By.xpath("//button[contains(text(), 'Відправити')]")).click();
        return webDriver.findElements(By.className("MuiAvatar-root")).size() > 0;
    }
}
