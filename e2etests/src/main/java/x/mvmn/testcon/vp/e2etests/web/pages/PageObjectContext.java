package x.mvmn.testcon.vp.e2etests.web.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageObjectContext {
    protected WebDriver webDriver;
    protected String baseUrl;

    public static PageObjectContext of(WebDriver webDriver, String baseUrl) {
        return PageObjectContext.builder().webDriver(webDriver).baseUrl(baseUrl).build();
    }
}
