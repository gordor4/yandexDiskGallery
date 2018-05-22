package rus.ru.yandexapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    //Is show this activity on screen
    private boolean visible;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onPause() {
        visible = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        visible = true;
    }


    protected boolean isVisibleOnScreen() {
        return visible;
    }

}
