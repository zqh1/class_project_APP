package ca.dal.csci3130.quickcash.usermanagement;

/**
 * This class will create basic get and set method for user activity
 */
public class User implements UserInterface {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;
    private String isEmployee; // Values y = yes, n= no.

    /**
     * User constructor will be called If new user with all information can be created with
     * all require information
     *
     * @param firstName:       firstname of user
     * @param lastName:        last name of user
     * @param email:           email that user use for register
     * @param phone:           phone number of user
     * @param password:        password that user input in sign up
     * @param confirmPassword: confirmed password that user put in text field in sign up
     * @param isEmployee:      "y" if user set the account to employee. "n" if user set to employer
     */
    public User(String firstName, String lastName, String email, String phone, String password,
                String confirmPassword, String isEmployee) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.isEmployee = isEmployee;
    }

    /**
     * This constructor is an empty constructor
     */
    public User() {
    }

    /**
     * getFirstName method will return first name of the user.
     *
     * @return String firstName: First name of the user, or null
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * setFirstName method will set firstName variable to parameter
     *
     * @param firstName: String that is first name of the user
     */
    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getLastName method will return last name of the user.
     *
     * @return String lastName: Last name of the user, or null
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * setLastName method will set lastName variable to parameter
     *
     * @param lastName: String that is the last name of user
     */
    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getEmail method will return email of the user.
     *
     * @return String email: email of the user, or null
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * setEmail method will set email variable to parameter
     *
     * @param email: String that is the email of user
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getPhone method will return phone number of the user.
     *
     * @return String phone: phone number of the user, or null
     */
    @Override
    public String getPhone() {
        return phone;
    }

    /**
     * setPhone method will set phone variable to parameter
     *
     * @param phone: String that is the phone number of user
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * getPassword method will return password of the user.
     *
     * @return String password: password of the user, or null
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * setPassword method will set password variable to parameter
     *
     * @param password: String that is the password of user
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getConfirmPassword method will return confirmPassword of the user.
     *
     * @return String confirmPassword: confirmed password of the user, or null
     */
    @Override
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * setConfirmPassword method will set confirmPassword variable to parameter
     *
     * @param confirmPassword: String that is the last name of user
     */
    @Override
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * getIsEmployee method will return y if user is Employee, n if user is Employer.
     *
     * @return String firstName: First name of the user, or null
     */
    @Override
    public String getIsEmployee() {
        return isEmployee;
    }

    /**
     * setIsEmployee method will set isEmployee variable to parameter
     *
     * @param isEmployee: String that is the determine whether user is employee, y if yes, n if no
     */
    @Override
    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

}
