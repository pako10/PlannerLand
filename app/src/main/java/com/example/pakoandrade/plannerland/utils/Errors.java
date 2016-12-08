package com.example.pakoandrade.plannerland.utils;

/**
 * Created by pakoAndrade on 24/11/16.
 */

public final class Errors {

    /** ERRORS FOR LOGIN CLASS**/
    public static final String errorUser = "2500"; //For user dont exist in the data base
    public static final String errorActivation = "2501"; //For user unactive via email
    //for incorrect password the server return a number from 1 to 5 until reach 5 to block the account
    public static final String accountBlocked = "2000";
    public static final String passRecoverSucess = "2252";

    /** ERRORS FOR REGISTRO CLASS**/
    public static final String errorEmail = "1500"; //For user dont exist in the data base
    public static final String accountCreated = "1250"; //For user dont exist in the data base


}
