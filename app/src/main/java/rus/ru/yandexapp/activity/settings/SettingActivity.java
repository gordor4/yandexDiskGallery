package rus.ru.yandexapp.activity.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import rus.ru.yandexapp.R;
import rus.ru.yandexapp.base.AbstractActivity;

public class SettingActivity extends AbstractActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingFragment fragment = (SettingFragment) getContentFragment();
        if (fragment == null) {
            fragment = addContentFragment(SettingFragment.newInstance());
        }
        SettingPresenter presenter = new SettingPresenter(fragment);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected String getActivityTitle() {
        return getString(R.string.setting_activity);
    }

}
