package rus.ru.yandexapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import rus.ru.yandexapp.App;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.activity.auth.AuthActivity;
import rus.ru.yandexapp.activity.gallery.GalleryActivity;
import rus.ru.yandexapp.service.YandexDiskService;

public class InitActivity extends AppCompatActivity {

    @Inject
    YandexDiskService yandexDiskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_activity);

        ((App)getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String token = yandexDiskService.getToken();

        if(token.equals("")) {
            Intent authIntent = new Intent(this, AuthActivity.class);
            authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(authIntent);
        } else {
            Intent galleryIntent = new Intent(this, GalleryActivity.class);
            galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(galleryIntent);
        }

    }
}
