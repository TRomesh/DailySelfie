package com.sliit.dailyselfie.Camera;

import android.app.Application;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.adobe.creativesdk.foundation.internal.auth.AdobeAuthIMSEnvironment;

/**
 * Created by Tharaka on 25/04/2016.
 */
public class Creative extends Application implements IAviaryClientCredentials{

    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID ="0d21f466e2e04eab864f791cc686c3dc";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "e72825b3-b0be-4c2c-9aef-8d1f41454aa1";

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String getBillingKey() {
        return "";
    }



}
