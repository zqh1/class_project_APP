package ca.dal.csci3130.quickcash.usermanagement;

/**
 * Session Manager Interface, contains all the methods the managers must have, created in case
 * different manager get added for multiple behaviours
 */
public interface SessionManagerInterface {

    /**
     * Method will save user id into the shared preferences of the application to keep
     * user logged in. Also, it will read the user information from the database.
     *
     * @param userID: User ID given on sign up
     */
    void createLoginSession(String userID);

    /**
     * Method will check if the user is logged in and sent to user home screen,
     * otherwise, sent to login screen
     */
    void checkLogin();

    /**
     * Logout method, user ID and information is deleted from application
     */
    void logoutUser();

    /**
     * Method will check if the User is logged in in the app (User ID KEY exists)
     *
     * @return boolean: true if user logged in, otherwise, false
     */
    boolean isLoggedIn();
}
