package com.prueba.rappi.activities;

import android.app.Activity;
import android.app.Application;

import di.component.ApplicationComponent;
import di.component.DaggerApplicationComponent;
import di.module.ContextModule;

public class MovieApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder().contextModule(new ContextModule(this)).build();
        applicationComponent.injectApplication(this);

    }

    public static MovieApplication get(Activity activity){
        return (MovieApplication) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
