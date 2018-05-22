package rus.ru.yandexapp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rus.ru.yandexapp.R;

public abstract class AbstractFragment<T extends AbstractInterface.Presenter> extends Fragment implements AbstractInterface.View<T> {

    protected Unbinder unbinder;

    protected T presenter;

    private boolean visible;

    private ActionBar toolbar;

    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        AppCompatActivity activity = getContext();
        toolbar = activity.getSupportActionBar();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.start();

        setHasOptionsMenu(true);
    }


    @Override
    public BaseActivity getContext() {
        return (BaseActivity) getActivity();
    }


    @CallSuper
    @Override
    public Bundle saveInstance() {
        return new Bundle();
    }

    @CallSuper
    @Override
    public void restoreInstance(Bundle b) {
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


    protected void uiBind(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    protected void uiUnbind() {
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AbstractActivity activity = (AbstractActivity) getActivity();
            activity.onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNetworkError() {
        final Activity activity = getActivity();

        activity.runOnUiThread(new Runnable() {

            public void run() {

                String errorString =  activity.getString(R.string.error_no_internet_connection);
                Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showServerError() {
        final Activity activity = getActivity();

        activity.runOnUiThread(new Runnable() {

            public void run() {

                String errorString =  activity.getString(R.string.server_exception);
                Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showMessage(final String message) {
        final Activity activity = getActivity();

        activity.runOnUiThread(new Runnable() {

            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
