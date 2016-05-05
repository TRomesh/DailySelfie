package com.sliit.dailyselfie.Start.mFacebook;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

/**
 * Created by Tharaka on 04/05/2016.
 */
public class MyConfigurations {

    Permission[] permissions = new Permission[]{Permission.EMAIL,Permission.USER_ABOUT_ME};
    static final String APP_ID="1529806223987699";

    public SimpleFacebookConfiguration getMyConfigs(){

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(APP_ID)
                .setNamespace("facebookstart")
                .setPermissions(permissions)
                .build();
        return  configuration;
    }

}
