package rus.ru.yandexapp.activity.gallery.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import rus.ru.yandexapp.R;
import rus.ru.yandexapp.base.AbstractActivity;
import rus.ru.yandexapp.model.PhotoList;

public class PhotoViewActivity extends AbstractActivity {

    public static final String LOG_TAG = "PhotoViewActivity";
    public static final String GALLERY_PHOTO_LIST = "GALLERY_PHOTO_LIST";
    public static final String PHOTO_INDEX = "PHOTO_INDEX";
    public static final String ALL_PHOTO_COUNT = "ALL_PHOTO_COUNT";
    public static final String CURRENT_INDEX_BUNDLE = "CURRENT_INDEX_BUNDLE";

    private int hideCountdown = 8000;

    private int photoIndex;
    private int allPhotoIndex;

    private Handler autoHideScheduler;
    private MenuItem downloadMenuItem;
    private PhotoViewPresenter presenter;
    private int currentIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX_BUNDLE, -1);
        }

        PhotoList photoList = (PhotoList)getIntent().getSerializableExtra(GALLERY_PHOTO_LIST);

        Intent intent = getIntent();

        photoIndex = intent.getIntExtra(PHOTO_INDEX, 0);
        allPhotoIndex = intent.getIntExtra(ALL_PHOTO_COUNT, 0);

        PhotoViewFragment fragment = (PhotoViewFragment) getContentFragment();
        if (fragment == null) {
            fragment = addContentFragment(PhotoViewFragment.newInstance());
        }

        if (currentIndex > -1) {
            presenter = new PhotoViewPresenter(fragment, photoList, this, currentIndex, allPhotoIndex);
            this.setActionBarTitle(getString(R.string.image_count_holder, currentIndex + 1, allPhotoIndex));
        } else {
            presenter = new PhotoViewPresenter(fragment, photoList, this, photoIndex, allPhotoIndex);
            this.setActionBarTitle(getString(R.string.image_count_holder, photoIndex + 1, allPhotoIndex));
        }

        //TODO: добавить анимацию
        autoHideScheduler = new Handler();
        autoHideScheduler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleToolbarVisibility();
            }

        }, 8000);

        injecter().inject(presenter);

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX_BUNDLE, presenter.getCurrentIndex());
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    public void restartHideScheduler() {
        if(autoHideScheduler != null) {
            //если пользователь не делает ничего определенное кол-во времени, скрываем toolbar,
            // обновляем таймер при нажатии или прокрутке pager'а
            autoHideScheduler.removeCallbacksAndMessages(null);
            autoHideScheduler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideToolbar();
                }
            }, hideCountdown);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_view, menu);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace);
        actionBar.setDisplayHomeAsUpEnabled(true);

        downloadMenuItem = menu.findItem(R.id.action_bar_download);

        return true;
    }

    public void setCheckDownloadMark(boolean downloaded) {
        if(downloaded) {
            downloadMenuItem.setIcon(R.drawable.ic_check);
            downloadMenuItem.setEnabled(false);
        } else {
            downloadMenuItem.setIcon(R.drawable.ic_file_download);
            downloadMenuItem.setEnabled(true);
        }

    }

    @Override
    protected String getActivityTitle() {
        if(currentIndex > -1) {
            return getString(R.string.image_count_holder, currentIndex + 1, allPhotoIndex);
        } else {
            return getString(R.string.image_count_holder, photoIndex, allPhotoIndex);
        }
    }
}
