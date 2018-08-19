package com.ticktalk.translateto.accountmanager;

/**
 * Created by Indogroup02 on 04/01/2018.
 */

public final class AccountConstants {

    private AccountConstants() {
    }

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.ticktalk.translateto";

    /**
     * Auth token types
     */
    public static final String TOKEN_TYPE_FREE_ACCESS = "ua.gov.dp.econtact.TYPE_FREE_ACCESS";
    //TODO: replace AndroidTemplate
    public static final String TOKEN_TYPE_FREE_ACCESS_LABEL = "Free AndroidTemplate account";

    //TODO: replace account_type, remove if you don't need another types of accounts
    public static final String TOKEN_TYPE_FULL_ACCESS = "ua.gov.dp.econtact.TYPE_FULL_ACCESS";
    //TODO: replace AndroidTemplate
    public static final String TOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to a AndroidTemplate account";


}
