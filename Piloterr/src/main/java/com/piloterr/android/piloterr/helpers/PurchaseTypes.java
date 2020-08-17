package com.piloterr.android.piloterr.helpers;

import java.util.Arrays;
import java.util.List;

public class PurchaseTypes {

    public static String Purchase4Gems = "com.piloterr.android.piloterr.iap.4gems";
    public static String Purchase21Gems = "com.piloterr.android.piloterr.iap.21gems";
    public static String Purchase42Gems = "com.piloterr.android.piloterr.iap.42gems";
    public static String Purchase84Gems = "com.piloterr.android.piloterr.iap.84gems";

    public static List<String> allGemTypes = Arrays.asList(Purchase4Gems, Purchase21Gems, Purchase42Gems, Purchase84Gems);

    public static String Subscription1Month = "com.piloterr.android.piloterr.subscription.1month";
    public static String Subscription3Month = "com.piloterr.android.piloterr.subscription.3month";
    public static String Subscription6Month = "com.piloterr.android.piloterr.subscription.6month";
    public static String Subscription12Month = "com.piloterr.android.piloterr.subscription.12month";

    public static List<String> allSubscriptionTypes = Arrays.asList(Subscription1Month, Subscription3Month, Subscription6Month, Subscription12Month);

    public static String Subscription1MonthNoRenew = "com.piloterr.android.piloterr.norenew_subscription.1month";
    public static String Subscription3MonthNoRenew = "com.piloterr.android.piloterr.norenew_subscription.3month";
    public static String Subscription6MonthNoRenew = "com.piloterr.android.piloterr.norenew_subscription.6month";
    public static String Subscription12MonthNoRenew = "com.piloterr.android.piloterr.norenew_subscription.12month";

    public static List<String> allSubscriptionNoRenewTypes = Arrays.asList(Subscription1MonthNoRenew, Subscription3MonthNoRenew, Subscription6MonthNoRenew, Subscription12MonthNoRenew);
}
