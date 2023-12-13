package TestScripts;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class LoginJava {
	WebDriver driver;
	Properties prop;
	@BeforeMethod
	public void setup() throws IOException
	{
		String path = System.getProperty("user.dir") 
				+ "//src/test/resources/Configuration/config.properties";
		FileInputStream fin = new FileInputStream(path);
		prop = new Properties();
		prop.load(fin);
		String strBrowser = prop.getProperty("browser");
		if(strBrowser.equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
		}
		else if (strBrowser.equalsIgnoreCase("edge"));
		{
			driver = new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.manage().window().maximize();
	}
		@Test(dataProvider = "LoginData")
		public void validLogin(String strUser, String strPwd) {
			driver.get(prop.getProperty("url"));
			driver.findElement(By.xpath("//input[@id='username']")).sendKeys(strUser);
		    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(strPwd);
		    driver.findElement(By.cssSelector("button.radius")).click();
		    boolean isDisp = driver.findElement(By.cssSelector("div.flash.success")).isDisplayed();		    
		    Assert.assertTrue(isDisp);
		}
		@DataProvider(name="LoginData")
        public Object[][] getData() throws CsvValidationException, IOException
        {
			String path = System.getProperty("user.dir")+
					"//src//test//resources//TestData/LoginData.csv";
			CSVReader reader = new CSVReader (new FileReader(path));
			String cols[];
			
			ArrayList<Object> dataList = new ArrayList<Object>();
			while((cols = reader.readNext())!= null)
			{
				Object record[] = {cols[0], cols[1]};
				dataList.add(record);	
				}
			reader.close();
			return dataList.toArray(new Object[dataList.size()][]);
			}
//		@DataProvider(name="LoginData")
//		public String[][] getData() throws IOException, ParseException, org.json.simple.parser.ParseException
//        {
//			String path = System.getProperty("user.dir")+
//					"//src//test//resources//TestData/LoginData.json";
//			FileReader reader = new FileReader(path);
//			JSONParser parser = new JSONParser();
//			Object obj = parser.parse(reader);
//			JSONObject jsonObj = (JSONObject) obj;
//			JSONArray userArray = (JSONArray)jsonObj.get("userLogins");
//			String arr[][] = new String[userArray.size()][];
//			for(int i=0; i< userArray.size();i++)
//			{
//				JSONObject user = (JSONObject)userArray.get(i);
//				String strUser = (String)user.get("username");
//				String strPwd = (String)user. get("password");
//				String record[] = {strUser, strPwd};
//				arr[i] = record;
//			}
//			return arr;
//			}
	        
		@AfterMethod
		public void teardwon() {
			driver.close();
        }
		
		
	}
