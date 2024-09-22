package x.mvmn.testcon.vp.e2etests.web.pages;

import lombok.experimental.SuperBuilder;
import org.openqa.selenium.By;

@SuperBuilder
public class WelcomePage extends AbstractPageObject {

    public WelcomePage(PageObjectContext context) {
        super(context);
    }

    public WelcomePage open() {
        getWebDriver().get(getBaseUrl());
        waitFor(wd -> wd.findElement(By.xpath("//a[text()='Ввійти']")), 30);
        return this;
    }

    public LoginPage gotoLogin() {
        getWebDriver().findElement(By.xpath("//a[text()='Ввійти']")).click();
        return new LoginPage(context);
    }

    public void gotoRegistration() {
        getWebDriver().findElement(By.xpath("//a[text()='Реєстрація']")).click();
    }
}
