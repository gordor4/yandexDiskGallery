package rus.ru.yandexapp.activity.auth;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import rus.ru.yandexapp.R;
import rus.ru.yandexapp.base.AbstractActivity;

public class AuthActivity extends AbstractActivity {

    public static final String NEW_TOKEN = "NEW_TOKEN";

    private boolean forceNewToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forceNewToken = getIntent().getBooleanExtra(NEW_TOKEN, false);

        AuthFragment fragment = (AuthFragment) getContentFragment();
        if (fragment == null) {
            fragment = addContentFragment(AuthFragment.newInstance());
        }
        AuthPresenter presenter = new AuthPresenter(fragment, forceNewToken);

        injecter().inject(presenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.auth_activity);
    }

}