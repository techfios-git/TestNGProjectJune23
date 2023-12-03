package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmTest {

	WebDriver driver;
	String environment;
	String browser;

	// Login Data
	String userName;
	String password;

	// Element List
	By USERNAME_FIELD = By.xpath("//*[@id=\"user_name\"]");
	By PASSWORD_FIELD = By.xpath("//*[@id=\"password\"]");
	By SIGNIN_BUTTON_FIELD = By.xpath("//*[@id=\"login_submit\"]");
	By DASHBOARD_HEADER_FIELD = By.xpath("//h3[contains(text(), 'Desposit Vs Expense')]");
	By CUSTOMER_MENU_FIELD = By.xpath("/html/body/div[1]/aside[1]/div/nav/ul[2]/li[2]/a/span");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//*[@id=\"customers\"]/li[2]/a/span");
	By NEW_CUSTOMER_HEADER_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By ADD_CUSTOMER_HEADER_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By FULL_NAMES_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By COMPANY_NAME_DROPDOWN_FIELD = By.xpath("//select[@name='company_name']");
	By EMAIL_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[3]/div/input");
	By PHONE_FIELD = By.xpath("//*[@id=\"phone\"]");
	By COUNTRY_NAME_DROPDOWN_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[8]/div[1]/select");
	By ZIP_CODE_FIELD = By.xpath("//*[@id=\"port\"]");
	By ZIP_CODE_ERROR_LESS_CHAR_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[7]/div/div/p");
	By ZIP_CODE_ERROR_MORE_CHAR_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[7]/div/div/p");
	By SAVE_BUTTON_FIELD = By.xpath("//*[@id=\"save_btn\"]");
	
	// Test Data / Mock Data
	String dashboardValidationText = "Desposit Vs Expense";
	String alertUserValidationText = "Please enter your user name";
	String alertPasswordValidationText = "Please enter your password";
	String addCustomerHeaderValidationText = "New Customer";
	String fullName = "Selenium";
	String companyName = "Techfios";
	String emailAddress = "test@gmail.com";
	String phoneNum = "1234567";
	String countryName = "Algeria";
	String zipLessCharErrMsg = "Error: Do not allow less than 5 digits";
	String zipMoreCharErrMsg = "Error: Do not allow more than 9 digits";

	@BeforeClass
	public void readConfig() {
		// bufferedReader //InputStream //FileReader //Scanner

		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			
			browser = prop.getProperty("browser");
			environment = prop.getProperty("url");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {
		
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if(browser.equalsIgnoreCase("edge")){
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
		}else {
			System.out.println("please use a valid browser");
		}
		driver.manage().deleteAllCookies();
		driver.get(environment);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

//	@Test(priority=2)
	public void loginTest() {
		driver.findElement(USERNAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), dashboardValidationText,
				"Dashboard page not found!!");
	}
	
//	@Test(priority=1)
	public void testAlertLoginPage() {
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		String actualAlertUserText = driver.switchTo().alert().getText();
		Assert.assertEquals(actualAlertUserText, alertUserValidationText, "Alert user msg is not available!!");
		
		driver.switchTo().alert().accept();
		driver.findElement(USERNAME_FIELD).sendKeys(userName);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), alertPasswordValidationText, "Alert password msg is not available!!");
		driver.switchTo().alert().accept();
	}
	
//	@Test(priority=3)
	public void addCustomer() {
		
		loginTest();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_HEADER_FIELD).getText(), addCustomerHeaderValidationText, "Add Customer Page is not available!");

		driver.findElement(FULL_NAMES_FIELD).sendKeys(fullName + generateRandomNum(999));
		selectFromDropDown(driver.findElement(COMPANY_NAME_DROPDOWN_FIELD), companyName);
		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNum(9999) + emailAddress);
		driver.findElement(PHONE_FIELD).sendKeys(phoneNum + generateRandomNum(999));
		selectFromDropDown(driver.findElement(COUNTRY_NAME_DROPDOWN_FIELD), countryName);
		
	}
	
	private int generateRandomNum(int boundNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(boundNum);
		return generatedNum;
	}

	private void selectFromDropDown(WebElement element, String visibleText) {
		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
	}
	
	@Test
	public void validateZipCodeField() {
		loginTest();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_HEADER_FIELD).getText(), addCustomerHeaderValidationText, "Add Customer Page is not available!");

		driver.findElement(FULL_NAMES_FIELD).sendKeys(fullName + generateRandomNum(999));
		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNum(9999) + emailAddress);
		driver.findElement(PHONE_FIELD).sendKeys(phoneNum + generateRandomNum(999));
		
		driver.findElement(ZIP_CODE_FIELD).sendKeys("1234");
		driver.findElement(SAVE_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(ZIP_CODE_ERROR_LESS_CHAR_FIELD).getText(), zipLessCharErrMsg, "Msg do not match!");
		
		driver.findElement(FULL_NAMES_FIELD).click();
		driver.findElement(ZIP_CODE_FIELD).clear();
		driver.findElement(ZIP_CODE_FIELD).sendKeys("1234567890");
		driver.findElement(SAVE_BUTTON_FIELD).click();
//		Assert.assertEquals(driver.findElement(ZIP_CODE_ERROR_MORE_CHAR_FIELD).getText(), zipMoreCharErrMsg, "Msg do not match!");
		
		
		
	}

	

//	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
