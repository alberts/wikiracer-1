package com.segment.wikiracer.util;

/**
 * Util class to form a proper URL from the start argument and domain argument or Filter out un-necessary links that does not need to traverse
 */
public class LinkHelper {

    //TODO A lot more validation can be done here
    public static String wikify(String pageTitle, String domain) {
        return domain + pageTitle.replace(" ", "_");
    }

    public static String normalize(String pageTitle) {
        return pageTitle.replace("_", " ").toLowerCase();
    }
}
