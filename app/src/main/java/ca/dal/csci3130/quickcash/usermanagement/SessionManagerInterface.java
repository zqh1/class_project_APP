package ca.dal.csci3130.quickcash.usermanagement;

public interface SessionManagerInterface {

    void createLoginSession(String userID);

    void checkLogin();

    void logoutUser();

    boolean isLoggedIn();
}
