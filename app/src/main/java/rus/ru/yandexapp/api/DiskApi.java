package rus.ru.yandexapp.api;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rus.ru.yandexapp.model.FileURL;

public interface DiskApi {

    @GET("v1/disk/resources/download")
    Call<FileURL> getFileUrl(@Header("Authorization") String token, @Query("path") String path);
}
