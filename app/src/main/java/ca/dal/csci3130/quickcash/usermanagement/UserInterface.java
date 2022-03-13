package ca.dal.csci3130.quickcash.usermanagement;

/**
 * User Interface, contains all the methods the user must have, created in case
 * different users get added for multiple behaviours
 */
public interface UserInterface {

    /**
     * getFirstName method will return first name of the user.
     *
     * @return String firstName: First name of the user, or null
     */
    String getFirstName();

    /**
     * setFirstName method will set firstName variable to parameter
     *
     * @param firstName: String that is first name of the user
     */
    void setFirstName(String firstName);

    /**
     * getLastName method will return last name of the user.
     *
     * @return String lastName: Last name of the user, or null
     */
    String getLastName();

    /**
     * setLastName method will set lastName variable to parameter
     *
     * @param lastName: String that is the last name of user
     */
    void setLastName(String lastName);

    /**
     * getEmail method will return email of the user.
     *
     * @return String email: email of the user, or null
     */
    String getEmail();

    /**
     * setEmail method will set email variable to parameter
     *
     * @param email: String that is the email of user
     **/
    void setEmail(String email);

    /**
     * getPhone method will return phone number of the user.
     *
     * @return String phone: phone number of the user, or null
     */
    String getPhone();

    /**
     * setPhone method will set phone variable to parameter
     *
     * @param phone: String that is the phone number of user
     */
    void setPhone(String phone);

    /**
     * getPassword method will return password of the user.
     *
     * @return String password: password of the user, or null
     */
    String getPassword();

    /**
     * setPassword method will set password variable to parameter
     *
     * @param password: String that is the password of user
     */
    void setPassword(String password);

    /**
     * getConfirmPassword method will return confirmPassword of the user.
     *
     * @return String confirmPassword: confirmed password of the user, or null
     */
    String getConfirmPassword();

    /**
     * setConfirmPassword method will set confirmPassword variable to parameter
     *
     * @param confirmPassword: String that is the last name of user
     */
    void setConfirmPassword(String confirmPassword);

    /**
     * getIsEmployee method will return y if user is Employee, n if user is Employer.
     *
     * @return String firstName: First name of the user, or null
     */
    String getIsEmployee();

    /**
     * setIsEmployee method will set isEmployee variable to parameter
     *
     * @param isEmployee: String that is the determine whether user is employee, y if yes, n if no
     */
    void setIsEmployee(String isEmployee);
}

