package com.vinuthana.vinvidyaadmin.fcm;

/**
 * Created by anirudh on 08/06/18.
 */

public class FCMUtils {

    public static final class sharedInstance{
        public static String token = "";

    }
    public static final class messages{
        public static final String REG_SUCCESS = "RegistrationSuccess";
        public static final String REG_ERROR = "RegistrationError";

    }

    public static final class notification{
        public static int icon ;
        public static String notificationTitle = "";
    }
}
