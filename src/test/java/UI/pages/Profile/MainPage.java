package UI.pages.Profile;

import UI.pages.BasePage;
import org.openqa.selenium.By;

public class MainPage extends BasePage {

    public MainPage() {
        super();
    }

    private final static By SIDE_BAR = By.xpath("//div[@id='side_bar']");

    public void choosePageInSideBar(String text) {
        findElement(SIDE_BAR).findElement(By.xpath(".//span[text()='" + text + "']")).click();
    }
}
