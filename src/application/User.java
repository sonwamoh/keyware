package application;

/**
 * @author team-02-04-2
 */

public class User 
{
	/*
	 * Instance variables
	 */
	private String username;
	private String masterPassword;
	private String securityQuestion;
	private String securityAnswer;
	private String email;
	private String accountSite;
	private String creationDate;
	private String expirationDate;
	public boolean showExpiredPasswordsAtLogin;
	
	
	/**
	 * The constructor for user new account info
	 * @param accountSiteInput the name of user's application
	 * @param userNameInput the username of user's account
	 * @param passwordInput the password of user's account 
	 * @param emailInput the email adress of the user's account
	 * @param creationDateInput the creation date user's account
	 * @param expirationDateInput the expiration date of user's account
	 */
	public User(String accountSiteInput, String userNameInput, String passwordInput, String emailInput, String creationDateInput, String expirationDateInput ) {
		
		username = userNameInput;
		masterPassword = passwordInput;
		email = emailInput;
		accountSite = accountSiteInput;
		creationDate = creationDateInput;
		expirationDate = expirationDateInput;
	}
	
	/**
	 * The constructor for user new signUp info
	 * @param accountSiteInput the name of user's application
	 * @param userNameInput the username of user's account
	 * @param passwordInput the password of user's account 
	 * @param emailInput the email adress of the user's account
	 * @securityQuestion the user's security question for verification
	 * @securityAnswer the user's security answer for verification
	 */
	public User(String usernameInput, String passwordInput, String emailInput, String securityQuestionInput, String securityAnswerInput)
	{
		username = usernameInput;
		masterPassword = passwordInput;
		email = emailInput;
		securityQuestion = securityQuestionInput;
		securityAnswer = securityAnswerInput;
		
	}
	
	/**
	 * Constructor the validate user's credential 
	 * @param userInput the username of user's account
	 * @param passwordInput the password of user's account
	 */
	public User(String userInput, String passwordInput) {
		// TODO Auto-generated constructor stub
		username = userInput;
		masterPassword = passwordInput;
	}


	/**
	 * The setters of all instance variables
	 * All setters have String parameters
	 */
	public void setUsername(String input)
	{
		username = input;
	}
	
	public void setMasterPassword(String input)
	{
		masterPassword = input;
	}
	
	public void setSecurityQuestion (String input)
	{
		securityQuestion = input;
	}
	
	public void setSecurityQuestionAnswer (String input)
	{
		securityAnswer = input;
	}
	
	public void setEmail (String input)
	{
		email = input;
	}
	
	public void setAccountSite (String input)
	{
		accountSite = input;
	}
	
	public void setCreationDate(String input)
	{
		creationDate = input;
	}
	public void setExpirationDate(String input)
	{
		expirationDate = input;
	}
	
	/**
	 * The getters of instance variables
	 * All getters return Strings
	 */
	public String getUsername()
	{
		return username;
	}
	
	public String getMasterPassword()
	{
		return masterPassword;
	}
	
	public String getSecurityQuestion()
	{
		return securityQuestion;
	}
	
	public String getSecurityQuestionAnswer ()
	{
		return securityAnswer;
	}
	
	public String getEmail ()
	{
		return email;
	}
	
	public String getAccountSite()
	{
		return accountSite;
	}
	
	public String getCreationDate()
	{
		return creationDate;
	}
	public String getExpirationDate()
	{
		return expirationDate;
	}
	
}
