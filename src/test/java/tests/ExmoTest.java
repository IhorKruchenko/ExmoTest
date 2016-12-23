package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by IgorKruchenko on 23.12.2016.
 */
public class ExmoTest {
    private static String baseUrl = "https://exmo.com/uk/newyear2017";
    private static String baseEmail = "23122016@test.com";
    private static String basePassword = "1234567";

    private static SelenideElement SUBMIT = $(By.xpath(".//button[contains(@class,'reg_start_btn ')]"));
    private static SelenideElement LOGO = $(By.xpath("*//img[@alt='EXMO LOGO']"));
    private static SelenideElement LOGOUT = $(By.xpath("*//div[@ng-click='logout()']"));

    private static SelenideElement AUTORIZATION = $(By.xpath("*//.[@class='btn blue']"));

    @Test
    public void runTest() {
        open(baseUrl);
        String windowHandle = String.valueOf(getWebDriver().getWindowHandle());
        AUTORIZATION.waitUntil(Condition.visible, 10000).click();
        switchToAnotherWindowFrom(windowHandle);
        setFor("email", baseEmail);
        setFor("password", basePassword);
        SUBMIT.click();
        LOGO.waitUntil(Condition.visible,10000).click();
        LOGOUT.waitUntil(Condition.visible, 10000);
        openAllPage();

    }

    private void switchToAnotherWindowFrom(String windowHandle) {
        for (String handle : getWebDriver().getWindowHandles()) {
            if (!handle.equals(windowHandle)) {
                getWebDriver().switchTo().window(handle);
                break;
            }
        }
    }

    private void setFor(String label, String value) {
        $("#" + label).setValue(value);
    }

    private void openAllPage() {
        int tabsCounter = $$(By.xpath(".//nav[@class='navigation']/div/ul/li")).size();

        for (int i = 1; i<tabsCounter; i++){
            getTabByIndex(i).hover();
            int size = getSubTabsCount(i);
            for (int j = 1; j<=size; j++){
                getSubTabByIndex(i,j).click();
                if(title().startsWith("Error")){
                    System.out.println("NOT OPENED: "+getWebDriver().getCurrentUrl());
                }
                getTabByIndex(i).hover();
            }
            System.out.println("------");
        }
    }

    private SelenideElement getTabByIndex(int index){
        return $(By.xpath(".//nav[@class='navigation']/div/ul/li["+index+"]"));
    }

    private int getSubTabsCount(int index){
        return $$(By.xpath(".//nav[@class='navigation']/div/ul/li["+index+"]/div/ul/li")).size();
    }

    protected SelenideElement getSubTabByIndex(int tabIndex, int subTabIndex){
        return $(By.xpath(".//nav[@class='navigation']/div/ul/li["+tabIndex+"]/div/ul/li["+subTabIndex+"]"));
    }
}
