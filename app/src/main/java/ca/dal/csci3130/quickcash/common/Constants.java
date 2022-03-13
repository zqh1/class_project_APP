package ca.dal.csci3130.quickcash.common;

/**
 * Constants class, contains all variables that never change in app (constants)
 */
public class Constants {

    /**
     * Constants private constructor, avoid initialization of class
     */
    private Constants() {
    }

    //Firebase URL
    public static final String FIREBASE_URL = "https://csci3130-group-17-default-rtdb.firebaseio.com/";

    //Local shared preferences key
    public static final String USER_KEY = "user_key";
}
