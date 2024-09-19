package x.mvmn.testcon.vp.e2etests.web.pages;

import org.openqa.selenium.By;

public class LoginPage extends AbstractPageObject {
    public LoginPage(PageObjectContext context) {
        super(context);
    }

    public void login(String username, String password) {
        getWebDriver().findElement(By.id("phoneNumber")).sendKeys(username);
        getWebDriver().findElement(By.id("password")).sendKeys(password);
        getWebDriver().findElement(By.xpath("//button[contains(text(), 'Відправити')]")).click();
    }
}
