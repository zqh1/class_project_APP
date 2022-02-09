package ca.dal.csci3130.quickcash.usermanagement;

public class SessionManager implements SessionManagerInterface {

    private String email = new String();
    private String password = new String();
    private String name = new String();
    private boolean isLoggedIn = false;


    @Override
    public void createLoginSession(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        isLoggedIn = true;
    }

    @Override
    public void checkLogin() {
        if(isLoggedIn = true){
        }
    }

    @Override
    public void logoutUser() {
        this.email = new String();
        this.password = new String();
        this.name = new String();
        this.isLoggedIn = false;
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public String getKeyName() {
        return name;
    }

    @Override
    public String getKeyEmail() {
        return email;
    }
}
