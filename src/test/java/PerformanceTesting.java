import Utils.FileUtils;
import Utils.Jackson;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.Before;
import org.junit.Test;

public class PerformanceTesting {

    private Jackson jackson;

    @Before
    public void init() {
        this.jackson = new Jackson();
    }

    @Test
    public void detectPerformance() {
        try {
            // Launch a browser instance (Chromium, Firefox, or WebKit)
            Browser browser = Playwright.create().chromium().launch();

            // Create a browser context and open a new page
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Navigate to the target URL
            page.navigate("https://www.youtube.com");

            // Measure performance metrics using JavaScript in the browser context
            Object performanceMetrics = page.evaluate("() => JSON.stringify(window.performance.timing)");


            String json = jackson.convertObjectToJson(performanceMetrics);

            FileUtils.writeToFile("src/test/resources/perfo/performance.json",json);
            // Print the performance metrics



            // Close the browser
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
