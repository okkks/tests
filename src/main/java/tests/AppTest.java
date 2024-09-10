package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext context = browser.newContext();

            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));


            Page page = context.newPage();
            page.navigate("http://playwright.dev");
            String title = page.title();

            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            Locator btnGetStarted = page.getByText("Get Started");
            List<Locator> s = page.locator("div").all();
            assertThat(btnGetStarted).hasAttribute("href", "/docs/intro");
            btnGetStarted.click();

            assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation"))).isVisible();

            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));

        }

    }
}