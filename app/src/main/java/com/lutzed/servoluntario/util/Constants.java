package com.lutzed.servoluntario.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by luizfreitas on 17/07/2016.
 */
public class Constants {

    public static final String API_URL = "http://192.168.25.2:3000/";
    public static final Collection<String> FACEBOOK_PERMISSIONS = Arrays.asList("public_profile", "user_friends", "email");
    public static final int MIN_OPPORTUNITY_SKILLS_REQUIRED = 1;
    public static final int MIN_OPPORTUNITY_CAUSES_REQUIRED = 2;
}
