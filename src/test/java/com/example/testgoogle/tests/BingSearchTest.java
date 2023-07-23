package com.example.testgoogle.tests;

import com.example.testgoogle.pages.MainPage;
import com.example.testgoogle.pages.ResultsPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BingSearchTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.bing.com/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void searchResultsTest() {
        String input = "Selenium";
        MainPage mp = new MainPage(driver);
        mp.sendText(input);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        wait.until(ExpectedConditions.and(
                ExpectedConditions.attributeContains(By.cssSelector("h2 > a[href]"), "href", "selenium"),
                ExpectedConditions.elementToBeClickable(By.cssSelector("h2 > a[href]"))
        ));
        ResultsPage rp = new ResultsPage(driver);
        rp.clickElement(0);
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        driver.getCurrentUrl();
        assertEquals("https://www.selenium.dev/", driver.getCurrentUrl(), "открыта не верная ссылка");
    }

    @Test
    public void searchFieldTest() {
        String input = "Selenium";

        MainPage mp = new MainPage(driver);
        mp.sendText(input);

        ResultsPage rp = new ResultsPage(driver);

        assertEquals(input, rp.getTestFromSearchField(), "Текст не совпал");
    }


}
