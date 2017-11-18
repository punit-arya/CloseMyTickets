package net.thearya;

import java.awt.AWTException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main extends Application
{
	private static final int DELAY = 2000;
	private static final int LONG_DELAY = 4000;
	private static final int NETWORK_MONITORING_CLASSIFICATION = 7;
	private static final int NETWORK_MONITORING_CATEGORY = 22;

	public static void main(String[] args) throws MalformedURLException, AWTException, InterruptedException
	{
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("CloseMyTickets Log");
		// Label label = new Label("Starting...");
		Popup logWindow = new Popup();
		logWindow.setX(0);
		logWindow.setY(468);
		logWindow.show(primaryStage);
		TextArea textArea = new TextArea();
		VBox vBox = new VBox(textArea);
		logWindow.getContent().add(textArea);
		// Scene scene = new Scene(vBox, 400, 200);
		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.show();
		// TODO use Actions to avoid delays.
		// new Actions(driver).moveToElement(input).click().perform();

		// TODO check what this can do:
		// System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");

		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		// Point point = new Point(0, 0);
		// driver.manage().window().setPosition(point);
		// Dimension dimension = new Dimension(1366, 600);
		// driver.manage().window().setSize(dimension);
		driver.manage().window().maximize();

		// TODO check whether TOPS logged in.
		// driver.get("http://helpdesk.tkm.co.int:9090/SUMMIT/Summit Winlogin.aspx");

		driver.get("http://helpdesk.tkm.co.int:9090/SUMMIT/MDLIncidentMgmt/IM_WorkgroupTickets.aspx");
		new WebDriverWait(driver, 10).until((ExpectedCondition<Boolean>) d ->
		{
			assert d != null;
			return d.getTitle().startsWith("View Incident List");
		});
		try
		{
			// TODO Are these all needed ?
			// WebElement tableElement = driver.findElement(By.id("BodyContentPlaceHolder_gvMyTickets"));
			// ArrayList<HashMap<String, WebElement>> allTickets = new ArrayList<>();
			// ArrayList<WebElement> rowElements = (ArrayList<WebElement>) tableElement.findElements(By.xpath(".//tr"));
			// ArrayList<String> columnNames = new ArrayList<>();
			// ArrayList<WebElement> headerElements = (ArrayList<WebElement>) rowElements.get(0).findElements(By.xpath(".//th"));
			// for (WebElement headerElement : headerElements)
			// {
			// 	columnNames.add(headerElement.getText());
			// }
			// for (WebElement rowElement : rowElements)
			// {
			// 	HashMap<String, WebElement> row = new HashMap<>();
			// 	int columnIndex = 0;
			// 	ArrayList<WebElement> cellElements = (ArrayList<WebElement>) rowElement.findElements(By.xpath(".//td"));
			// 	for (WebElement cellElement : cellElements)
			// 	{
			// 		row.put(columnNames.get(columnIndex), cellElement);
			// 		columnIndex++;
			// 	}
			// 	allTickets.add(row);
			// }
			List<WebElement> rows = driver.findElements(By.xpath(".//*[@id=\"BodyContentPlaceHolder_gvMyTickets\"]/tbody/tr/td[1]"));
			Integer numberOfTickets = rows.size();
			WebElement element = driver.findElement(By.xpath("//*[@id=\"s2id_BodyContentPlaceHolder_ddlRecords\"]/a/span[2]/b"));
			Thread.sleep(DELAY);
			element.click();
			Thread.sleep(DELAY);
			element = driver.findElement(By.id("select2-result-label-20"));
			Thread.sleep(DELAY);
			element.click();
			Thread.sleep(DELAY);
			for (int j = numberOfTickets + 1; j > 1; --j)
			{
				WebElement ticket = driver.findElement(By.xpath("//*[@id=\"BodyContentPlaceHolder_gvMyTickets\"]/tbody/tr[" + j + "]/td[2]/div[2]/a[1]"));
				Thread.sleep(DELAY);
				System.out.println("Ticket #: " + ticket.getText());
				textArea.setText("Ticket #: " + ticket.getText());
				Thread.sleep(DELAY);
				element = driver.findElement(By.xpath("//*[@id=\"BodyContentPlaceHolder_gvMyTickets\"]/tbody/tr[" + j + "]/td[5]"));
				String caller = element.getText();
				System.out.println("Caller: " + caller);
				textArea.setText("Caller:" + caller);
				if (!Objects.equals(caller, "FootPrint"))
				{
					System.out.println("Non-FootPrint.... Skipping.\n");
					textArea.setText("Non-FootPrint.... Skipping.\n");
					continue;
				}
				// WebElement element = driver.findElement(By.id("filter"));
				// Thread.sleep(DELAY);
				// element.click();
				// element = driver.findElements(By.cssSelector(".multiselect.dropdown-toggle.btn.btn-default")).get(1);
				// Thread.sleep(DELAY);
				// element.sendKeys(Keys.RETURN, Keys.ARROW_UP, Keys.RETURN);
				// JavascriptExecutor jse = (JavascriptExecutor) driver;
				// jse.executeScript("document.getElementById('BodyContentPlaceHolder_btnFilter').click();");
				// Thread.sleep(DELAY);
				// element = driver.findElement(By.xpath("//*[@id=\"BodyContentPlaceHolder_gvMyTickets\"]/tbody/tr[" + j + "]/td[2]/div[2]/a[1]"));
				// Thread.sleep(DELAY);
				ticket.click();
				new WebDriverWait(driver, 10).until((ExpectedCondition<Boolean>) d ->
				{
					assert d != null;
					return d.getTitle().startsWith("INCIDENT ID");
				});
				// new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlStatus"))).selectByVisibleText("Resolved");
				element = driver.findElement(By.xpath("//*[@id=\"ticketdetail\"]/div[2]/div/div[2]/div/div[1]/div/div/ul/li[5]/a"));
				Thread.sleep(DELAY);
				element.click();
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlUrgency"))).selectByVisibleText("LOW");
				Thread.sleep(DELAY);
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlImpact"))).selectByVisibleText("LOW");
				Thread.sleep(DELAY);
				element = driver.findElement(By.id("BodyContentPlaceHolder_btnClearClassification"));
				Thread.sleep(DELAY);
				element.click();
				Thread.sleep(DELAY);
				element = driver.findElement(By.id("BodyContentPlaceHolder_btnShowClassificationPopup"));
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(LONG_DELAY);
				element = driver.findElements(By.cssSelector(".jstree-icon.jstree-ocl")).get(NETWORK_MONITORING_CLASSIFICATION);
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(LONG_DELAY);
				element = driver.findElement(By.linkText("MONITORING LAN"));
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(DELAY);
				element = driver.findElement(By.id("BodyContentPlaceHolder_btnClearCategoryPopup"));
				Thread.sleep(DELAY);
				element.click();
				Thread.sleep(DELAY);
				element = driver.findElement(By.id("BodyContentPlaceHolder_btnShowCategoryPopup"));
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(LONG_DELAY);
				element = driver.findElements(By.cssSelector(".jstree-icon.jstree-ocl")).get(NETWORK_MONITORING_CATEGORY);
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(LONG_DELAY);
				element = driver.findElement(By.linkText("GALC TERMINAL DOWN"));
				Thread.sleep(LONG_DELAY);
				element.click();
				Thread.sleep(LONG_DELAY);
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlPriority"))).selectByVisibleText("PRIORITY 6");
				Thread.sleep(DELAY);
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlWorkgroup"))).selectByVisibleText("GALC");
				Thread.sleep(DELAY);
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlAssignedExecutive"))).selectByVisibleText("fil_punit");
				Thread.sleep(DELAY);

				// TODO using Actions here will probably make this faster.
				// Actions builder = new Actions(driver);
				// Actions seriesOfActions = builder.moveToElement(solution).click().sendKeys(solution, "Terminal not in use temporarily.");
				// seriesOfActions.perform();

				element = driver.findElement(By.className("nicEdit-main"));
				Thread.sleep(DELAY);
				element.sendKeys("Terminal not in use temporarily.");
				Thread.sleep(DELAY);
				new Select(driver.findElement(By.id("BodyContentPlaceHolder_ddlClosureCode"))).selectByVisibleText("SUCCESSFUL");
				Thread.sleep(DELAY);
				element = driver.findElement(By.xpath("//*[@id=\"BodyContentPlaceHolder_btnSave\"]"));
				Thread.sleep(DELAY);
				element.click();
				System.out.println("");
				textArea.setText("");
				driver.get("http://helpdesk.tkm.co.int:9090/SUMMIT/MDLIncidentMgmt/IM_WorkgroupTickets.aspx");
				new WebDriverWait(driver, 10).until((ExpectedCondition<Boolean>) d ->
				{
					assert d != null;
					return d.getTitle().startsWith("View Incident List");
				});
			}
		}
		catch (ElementNotVisibleException | ElementNotSelectableException | NoSuchElementException exception)
		{
			System.out.println("exception.toString(): " + exception.toString());
			textArea.setText("exception.toString(): \" + exception.toString()");
			System.out.println("exception.getCause(): " + exception.getCause());
			textArea.setText("exception.getCause(): " + exception.getCause());
			System.out.println("exception.getMessage(): " + exception.getMessage());
			textArea.setText("exception.getMessage(): " + exception.getMessage());
			exception.printStackTrace();
			textArea.setText("exception.getStackTrace(): " + exception.getStackTrace());
			driver.quit();
		}
		finally
		{
			driver.quit();
		}
	}
}
