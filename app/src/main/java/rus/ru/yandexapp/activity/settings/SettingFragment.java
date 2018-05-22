package rus.ru.yandexapp.activity.settings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.activity.auth.AuthActivity;
import rus.ru.yandexapp.base.AbstractFragment;

public class SettingFragment extends AbstractFragment<SettingInterface.Presenter> implements SettingInterface.View {

    public SettingFragment() {}

    public static SettingFragment newInstance() { return new SettingFragment(); }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.setting_fragment, container, false);

        uiBind(root);

        return root;
    }

    @OnClick(R.id.auth_button)
    public void onClickAuthButton(Button button) {
        Intent authIntent = new Intent(getActivity(), AuthActivity.class);
        authIntent.putExtra(AuthActivity.NEW_TOKEN, true);
        startActivity(authIntent);
    }

    @OnClick(R.id.clear_cache)
    public void onClickClearCache(Button button) {
        ClearCacheTask task = new ClearCacheTask();
        task.execute();
    }

    class ClearCacheTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(getActivity()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            showMessage(getString(R.string.clear_cache_complete));
        }
    }
}
