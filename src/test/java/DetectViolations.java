import Utils.FileUtils;
import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.microsoft.playwright.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Properties;

/**
 *
 *  In this class we navigate through pages manually
 *  and when we press enter on that specific page
 *  we use playwright functionality to give us
 *  accessibility violations and we store that in json file
 *
 *  keyNote: when pressing enter it takes couple of milliseconds to close the browser
 *

 *
 */

public class DetectViolations {

    private FileUtils fileUtils;
    private Properties properties;


    @Before
    public void init() {
        this.fileUtils = new FileUtils();
        this.properties = fileUtils.loadProperties("config.properties");
    }

    @Test
    public void detectViolationsOfSpecificPage() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            String baseUrl = properties.getProperty("base.url");
            page.navigate(baseUrl);

            // Set up the initial page
            setUpPage(page);
            // Print the URL of the page at the end
            System.out.println("Current Page URL: " + page.url());
        } catch (PlaywrightException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setUpPage(Page page) throws InterruptedException {

        // Use evaluate to wait for user interaction (pressing Enter) and set the flag
        page.evaluate("document.addEventListener('keypress', (e) => { if (e.key === 'Enter') { resolve(); } });");

        // Perform accessibility check using Axe on the initial page
        AxeResults accessibilityScanResults = new AxeBuilder(page).analyze();

        // Output results for the initial page
        outputAccessibilityResults(accessibilityScanResults);
    }

    private static void outputAccessibilityResults(AxeResults accessibilityScanResults) {
        if (accessibilityScanResults.getViolations().isEmpty()) {
            System.out.println("No accessibility violations found.");
        } else {
            System.out.println("Accessibility violations found:");
            FileUtils.writeToFile("src/test/resources/single_violations/accessibility_violations.json",accessibilityScanResults.getViolations());
        }
    }
}
