package com.udacity.jwdnd.course1.cloudstorage;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.edgedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new EdgeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	@Order(2)
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	@Order(3)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertTrue(driver.getPageSource().contains("Error 404"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	@Order(4)
	public void testLargeUpload() {
		// Create a test account
//		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("UT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	@Test
	@Order(5)
	public void unauthorizedAccessRouting() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(6)
	public void authorizedAndUnauthorizedUserAccess() {
		doMockSignUp("User", "Test", "AccessCheck", "Checking");
		doLogIn("AccessCheck", "Checking");

		Assertions.assertEquals("Home", driver.getTitle());
		WebElement logoutButton = driver.findElement(By.id("logout-btn"));
		logoutButton.click();

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(7)
	public void createNote() {
		doLogIn("UT", "123");

		String testNoteTitle = "Unit Testing";
		String testNoteDescription = "Testing";

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-btn")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-btn"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys(testNoteTitle);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.sendKeys(testNoteDescription);


		WebElement saveChangesButton = driver.findElement(By.id("save-note-btn"));
		saveChangesButton.click();

		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
				.className("note-title-element")));
		List<WebElement> noteTitleList = driver.findElements(By.className("note-title-element"));
		boolean titleFound = noteTitleList.stream()
				.anyMatch(noteTitle -> noteTitle.getText().equals(testNoteTitle));
		Assertions.assertTrue(titleFound, "Expected Test Note title not found");

		List<WebElement> noteDescriptionList = driver.findElements(By.className("note-description-element"));
		boolean descriptionFound = noteDescriptionList.stream()
				.anyMatch(noteDescription -> noteDescription.getText().equals(testNoteDescription));
		Assertions.assertTrue(descriptionFound, "Expected Test Note description not found");
	}

	public void redirectToNotesTab() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
	}

	public void LoginAndCreateNoteForTest(String noteTitle, String noteDescription) {
		doLogIn("UT", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-btn")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-btn"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys(noteTitle);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.sendKeys(noteDescription);

		WebElement saveChangesButton = driver.findElement(By.id("save-note-btn"));
		saveChangesButton.click();
	}

	@Test
	@Order(9)
	public void editNote() throws InterruptedException {
		String testNoteTitle = "Test for Updating";
		String testNoteDescription = "No Change";
		LoginAndCreateNoteForTest(testNoteTitle, testNoteDescription);
		redirectToNotesTab();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));
		WebElement editNote = driver.findElement(By.id("edit-note-button"));
		editNote.click();

		String afterDescriptionChange = "This is changed";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.clear();
		inputDescription.sendKeys(afterDescriptionChange);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		WebElement submitNote = driver.findElement(By.id("save-note-btn"));
		submitNote.click();

		redirectToNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.className("note-description-element"))
				.getText().contains(afterDescriptionChange));
	}

	@Test
	@Order(8)
	public void deleteNote() {
		String testNoteTitle = "Test for Deletion";
		String testNoteDescription = "N/A";
		LoginAndCreateNoteForTest(testNoteTitle, testNoteDescription);
		redirectToNotesTab();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-btn")));
		WebElement deleteNote = driver.findElement(By.id("delete-note-btn"));
		deleteNote.click();
		redirectToNotesTab();

		// check note is deleted
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(1, notesList.size());
	}

	public void redirectToCredentialsTab() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		Thread.sleep(3000);
	}

	@Test
	@Order(10)
	public void createCredential() throws InterruptedException {

//		doMockSignUp("Imad","Sheikh","UT","123");

		doLogIn("UT", "123");

		WebElement credentialsTab= driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String inputCredentialPassword = "1234";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button")));
		WebElement addCredentialsButton= driver.findElement(By.id("add-credentials-button"));
		addCredentialsButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputURL = driver.findElement(By.id("credential-url"));
		inputURL.click();
		inputURL.sendKeys("https://www.google.com/");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys("Imad");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys(inputCredentialPassword);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-button")));
		WebElement submitNote = driver.findElement(By.id("submit-credential-button"));
		submitNote.click();

		redirectToCredentialsTab();

		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList = credentialsTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(1, credList.size());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), inputCredentialPassword);

		Thread.sleep(3000);
	}

	@Test
	@Order(12)
	public void editCredentials() throws InterruptedException {
		createCredential();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// press on edit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential-button")));
		WebElement editCredentialsButton= driver.findElement(By.id("edit-credential-button"));
		editCredentialsButton.click();

		// make changes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputURL = driver.findElement(By.id("credential-url"));
		inputURL.click();
		inputURL.clear();
		inputURL.sendKeys("https://github.com/isheikh8492");

		// get the unencrypted pwd
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		String inputPassword = driver.findElement(By.id("credential-password")).getAttribute("value");


		// Attempt ot submit the changes in the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-credential-button")));
		WebElement submitCredential = driver.findElement(By.id("submit-credential-button"));
		submitCredential.click();

		// Redirect to home page & press on credentials tab
		redirectToCredentialsTab();

		// check changes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-cred-url")).getText().contains("https://github.com/isheikh8492"));

		// Verify that the viewable password is unencrypted
		Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), inputPassword);

		Thread.sleep(3000);

	}

	@Test
	@Order(11)
	public void deleteCredentials() throws InterruptedException {
//		createCredential();
		doLogIn("UT", "123");
		redirectToCredentialsTab();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// press on edit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential-button")));
		WebElement deleteCredentialsButton= driver.findElement(By.id("delete-credential-button"));
		deleteCredentialsButton.click();

		// Redirect to home page & press on credentials tab
		redirectToCredentialsTab();

		// check the credential is deleted
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList = credentialTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(1, credList.size());

		Thread.sleep(3000);
	}

}
