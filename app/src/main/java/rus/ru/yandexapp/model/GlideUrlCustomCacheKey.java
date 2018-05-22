package rus.ru.yandexapp.model;

import android.net.Uri;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.net.URL;

public class GlideUrlCustomCacheKey extends GlideUrl {
    private String path;
    private String url;

    public GlideUrlCustomCacheKey(String url) {
        super(url);
    }

    public GlideUrlCustomCacheKey(String url, String path, Headers headers) {
        super(url, headers);

        this.path = path;
        this.url = url;
    }

    public GlideUrlCustomCacheKey(URL url) {
        super(url);
    }

    public GlideUrlCustomCacheKey(URL url, Headers headers) {
        super(url, headers);
    }

    @Override
    public String getCacheKey() {
        //ссылка на картинку выдается динамически, чтобы работало кеширование нужен другой ключ.
        // Hash приходит пустой, думаю, кэшировать можно по идентификатору пользователя +
        // пути к картинке на диске

        Uri uri = Uri.parse(url);
        String uid = uri.getQueryParameter("uid");

        return uid + path;
    }
}
