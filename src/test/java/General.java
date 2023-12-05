import Utils.FileUtils;
import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.microsoft.playwright.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Properties;


public class General {

    // If you want to see jsn in structured format
    // use keywords: command + option + L
    private FileUtils fileUtils = new FileUtils();
    private Properties properties;


    @Before
    public void init() {
        this.fileUtils = new FileUtils();
        this.properties = fileUtils.loadProperties("config.properties");
    }

    @Test
    public void shouldNotHaveAutomaticallyDetectableAccessibilityIssues() throws Exception {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        String baseUrl = properties.getProperty("base.url");
        page.navigate(baseUrl); // 3

        AxeResults accessibilityScanResults = new AxeBuilder(page).analyze(); // 4


        // Create file for violations
        FileUtils.writeToFile("src/test/resources/accessibility_violations.json",accessibilityScanResults.getViolations());

        // Create file for Incomplete
        FileUtils.writeToFile("src/test/resources/accessibility_errors.json",accessibilityScanResults.getIncomplete());

        // Create file for Passes
        FileUtils.writeToFile("src/test/resources/accessibility_passes.json",accessibilityScanResults.getPasses());

    }


    @Test
    public void screenshot() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Page page = browser.newPage();
            String baseUrl = properties.getProperty("base.url");
            page.navigate(baseUrl);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("src/test/resources/screenshots/example.png")));
        }
    }

    @Test
    public void evaluateBrowserContext() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            String baseUrl = properties.getProperty("base.url");
            page.navigate(baseUrl);
            Object dimensions = page.evaluate("() => {\n" +
                    "  return {\n" +
                    "      width: document.documentElement.clientWidth,\n" +
                    "      height: document.documentElement.clientHeight,\n" +
                    "      deviceScaleFactor: window.devicePixelRatio\n" +
                    "  }\n" +
                    "}");
            System.out.println(dimensions);
        }
    }

    @Test
    public void takeScreenshotOfSpecificPage() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Page page = browser.newPage();
            String baseUrl = properties.getProperty("base.url");
            page.navigate(baseUrl);

            // Use evaluate to wait for user interaction (pressing Enter)
            page.evaluate("document.addEventListener('keypress', (e) => { if (e.key === 'Enter') resolve(); });");

            // Take a screenshot after the user presses Enter
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("src/test/resources/screenshots/example.png")));
        } catch (PlaywrightException e) {
            e.printStackTrace();
        }
    }



}
