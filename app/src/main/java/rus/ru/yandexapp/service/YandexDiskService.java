package rus.ru.yandexapp.service;

import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.ResourceList;

import java.io.IOException;

import retrofit2.Callback;
import rus.ru.yandexapp.model.FileURL;

public interface YandexDiskService {
    ResourceList getFile(String path) throws ServerException, IOException;

    DiskInfo getDiskInfo();

    void getFileUrl(String path, Callback<FileURL> callback);

    void setToken(String token);

    String getToken();
}
