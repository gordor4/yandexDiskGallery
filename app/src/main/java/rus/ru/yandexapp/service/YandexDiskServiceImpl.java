package rus.ru.yandexapp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.ResourceList;

import java.io.IOException;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rus.ru.yandexapp.api.DiskApi;
import rus.ru.yandexapp.model.FileURL;

public class YandexDiskServiceImpl implements YandexDiskService  {

    public static final String DEBUG_TOKEN = "OAuth AQAAAAAP-xXnAADLW-TjP2gxREJYlpHsXK6n7ek";
    public static final String PREFERENCE_NAME = "YANDEX_SERVICE_PREFERENCE";
    public static final String TOKEN_KEY = "TOKEN_KEY";

    private String token;

    private Context context;
    private RestClient restClient;
    private DiskInfo diskInfo;
    private DiskApi service;
    private boolean restApiInitialized = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferencesEditor;

    public YandexDiskServiceImpl(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferencesEditor = sharedPreferences.edit();

        token = sharedPreferences.getString(TOKEN_KEY, "");


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .baseUrl("https://cloud-api.yandex.net/")
                .build();

        service = retrofit.create(DiskApi.class);

        if(!token.equals("")) {
            initRestApi();
        }
    }

    private void initRestApi() {
        Credentials credentials = new Credentials("rus.min96", token);
        restClient = new RestClient(credentials);

        //получаем информацию о диске пользователя
        DiskInfoTask diskInfoTask = new DiskInfoTask();
        diskInfoTask.execute();

    }


    @Override
    public ResourceList getFile(String path) throws ServerException, IOException {
        ResourcesArgs args = new ResourcesArgs.Builder()
                .setMediaType("image")
                //.setPath(path)
                .setPreviewSize("L")
                .build();

        return restClient.getFlatResourceList(args);
    }

    @Override
    public DiskInfo getDiskInfo() {
        return diskInfo;
    }

    @Override
    public void getFileUrl(String path, Callback<FileURL> callback) {
        service.getFileUrl(token, path).enqueue(callback);
    }

    class DiskInfoTask extends AsyncTask<Void, Void, DiskInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected DiskInfo doInBackground(Void... params) {
            DiskInfo info = null;

            try {
                info = restClient.getDiskInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }

            diskInfo = info;

            return info;
        }

        @Override
        protected void onPostExecute(DiskInfo result) { }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        preferencesEditor.putString(TOKEN_KEY, token);

        this.token = token;

        preferencesEditor.commit();

        if(!restApiInitialized) {
            initRestApi();
        }
    }
}
