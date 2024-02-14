package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.BrowserstackConfigFull;
import config.LocalConfigFull;
import drivers.LocalAndroidDriver;
import drivers.MobileDriver;
import helpers.AttachHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;

public class TestBase {
    public static BrowserstackConfigFull browserstackConfigFull;
    public static LocalConfigFull localConfigFull;
    public static String runtimeEnvironment;

    @BeforeAll
    static void beforeAll() {
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

        runtimeEnvironment = System.getProperty("runtimeEnv", "local");

        initConfig();
        initDriver();
    }

    static void initConfig() {

        if (runtimeEnvironment.equals("local")) {
            localConfigFull = new LocalConfigFull();
        } else if (runtimeEnvironment.equals("browserstack")) {
            browserstackConfigFull = BrowserstackConfigFull.getInstance();
        } else {
            throw new RuntimeException("You need to specify runtimeEnv!");
        }
    }

    static void initDriver() {
        Configuration.browser = null;
        if (runtimeEnvironment.equals("local")) {
            Configuration.browser = LocalAndroidDriver.class.getName();
        } else if (runtimeEnvironment.equals("browserstack")) {
            Configuration.browser = MobileDriver.class.getName();
        }

        Configuration.browserSize = null;
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void beforeEach() {
        //SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        open();
    }

    @AfterEach
    void addAttachments() {
        String sessionId = Selenide.sessionId().toString();
        System.out.println(sessionId);

        AttachHelper.takeScreenshotAs("Last screenshot");
        AttachHelper.pageSource();
        closeWebDriver();

        if (runtimeEnvironment.equals("browserstack")){
            AttachHelper.addVideoBrowserstack(sessionId);
        }
    }
}
