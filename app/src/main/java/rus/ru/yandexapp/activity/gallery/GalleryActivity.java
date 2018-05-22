package rus.ru.yandexapp.activity.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import rus.ru.yandexapp.R;
import rus.ru.yandexapp.activity.settings.SettingActivity;
import rus.ru.yandexapp.base.AbstractActivity;

public class GalleryActivity extends AbstractActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GalleryFragment fragment = (GalleryFragment) getContentFragment();

        if (fragment == null) {
            fragment = addContentFragment(GalleryFragment.newInstance());
        }
        GalleryPresenter presenter = new GalleryPresenter(fragment);

        injecter().inject(presenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bar_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
