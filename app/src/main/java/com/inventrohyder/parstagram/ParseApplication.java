package com.inventrohyder.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("OE3UHMvECK9URrx6mXIe7KtLdQFAOQtxkSHaxLvc")
                .clientKey("TOWY2pGmQxlGHCxSBhg96c31F731NiIvce8qCssb")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
