package rus.ru.yandexapp.base;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rus.ru.yandexapp.App;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.app.AppComponent;
import rus.ru.yandexapp.utils.ActivityUtils;

import static android.view.View.GONE;

public abstract class AbstractActivity extends BaseActivity {

    private Toolbar toolbar;

    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.abstract_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.toolbar = toolbar;

        ButterKnife.bind(this);


        if(!isOnline()) {
            String errorString = getString(R.string.error_no_internet_connection);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setActionBarTitle();
    }

    protected void setActionBarTitle() {
        setActionBarTitle(getActivityTitle());
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbarTitle.setText(title);
        }
    }

    protected Fragment getContentFragment() {
        return getFragment(R.id.content);
    }

    private Fragment getFragment(int fragmentId) {
        return getSupportFragmentManager().findFragmentById(fragmentId);
    }

    protected <T extends Fragment> T addContentFragment(T fragment) {
        addFragment(fragment, R.id.content);

        return fragment;
    }

    protected <T extends Fragment> T addFragment(T fragment, int fragmentId) {
        ActivityUtils.addFragment(getSupportFragmentManager(), fragment, fragmentId);

        return fragment;
    }

    protected AppComponent injecter() {
        return ((App)getApplication()).getAppComponent();
    }

    protected String getActivityTitle() {
        return getString(R.string.app_name);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleToolbarVisibility() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(toolbar.getVisibility() == View.GONE) {
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setVisibility(View.GONE);
                }

            }
        });
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }
}

