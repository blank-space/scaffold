package com.bsnl.scaffold;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bsnl.base.BaseApp;

/**
 * @author : LeeZhaoXing
 * @date : 2020/10/14
 * @desc :
 */
public abstract class BaseActivity extends AppCompatActivity {


    private ViewModelProvider mActivityProvider;
    private ViewModelProvider.Factory mFactory;


    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this);
        }
        return mActivityProvider.get(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider() {
        return new ViewModelProvider((BaseApp) this.getApplicationContext(),
                getAppFactory(this));
    }

    private ViewModelProvider.Factory getAppFactory(Activity activity) {
        Application application = checkApplication(activity);
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return mFactory;
    }

    private Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }

}
