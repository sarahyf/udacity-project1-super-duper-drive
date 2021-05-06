package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseURL + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseURL + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void homePageIsProtected() {
		driver.get(baseURL + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void creatingAnAccount(String firstName, String lastName, String username, String password) {
		driver.get(baseURL + "/signup");
		SignupPage signinPage = new SignupPage(driver);
		signinPage.signup(firstName, lastName, username, password);
	}

	@Test
	public void loginIntoAccount(String username, String password) {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@Test
	public void accessingHomePage() {
		driver.get(baseURL + "/home");
	}

	@Test
	public void loggingout() {
		HomePage homePage = new HomePage(driver);
		homePage.logout();
	}

	@Test
	public void testSigninLoginAccessHomePageLogoutProtectHomePage() {
		String username = "user";
		String password = "user";

		// signing up a new user
		creatingAnAccount("user", "user", "user", "user");

		//logging in the user
		loginIntoAccount(username, password);

		//able to access to home page
		accessingHomePage();

		//logging out the user
		loggingout();
		
		//not able to access the home page
		homePageIsProtected();

	}

	@Test
	public void testingCreatingNote() {
		driver.manage().window().maximize();

		creatingAnAccount("user", "user", "user", "user");
		loginIntoAccount("user", "user");

		HomePage homePage = new HomePage(driver);
		homePage.noteTab();
		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("nav-notes-tab"))));

		NoteTab noteTabObj = new NoteTab(driver);

		String noteTitle = "do shopping";
		String noteDescription = "going to the market";

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("add-new-note"))));
		noteTabObj.addNote();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("noteModal"))));
		driver.switchTo().activeElement();
		noteTabObj.fillingFields(noteTitle, noteDescription);
		
		homePage.noteTab();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-table"))));

		Assertions.assertEquals(noteTitle, driver.findElement(By.id("added-note-title")).getText());
		Assertions.assertEquals(noteDescription, driver.findElement(By.id("added-note-description")).getText());

	}

	@Test
	public void testEditingNote() {
		testingCreatingNote();

		HomePage homePage = new HomePage(driver);

		NoteTab noteTab = new NoteTab(driver);

		WebDriverWait wait = new WebDriverWait(driver, 2);

		String noteTitle = "do housework";
		String noteDescription = "clean the kitchen";

		noteTab.editNote();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("noteModal"))));
		driver.switchTo().activeElement();
		noteTab.clearTextFields();
		noteTab.fillingFields(noteTitle, noteDescription);

		homePage.noteTab();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-table"))));

		Assertions.assertEquals(noteTitle, driver.findElement(By.id("added-note-title")).getText());
		Assertions.assertEquals(noteDescription, driver.findElement(By.id("added-note-description")).getText());

	}

	@Test
	public void testDeletingNote() {
		testingCreatingNote();

		HomePage homePage = new HomePage(driver);

		NoteTab noteTab = new NoteTab(driver);

		WebDriverWait wait = new WebDriverWait(driver, 2);

		noteTab.deleteNote();

		homePage.noteTab();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-table"))));

		Note noteDate = noteTab.getNoteData();

		assertEquals("", noteDate.getNoteTitle());
		assertEquals("", noteDate.getNoteDescription());

	}

}
