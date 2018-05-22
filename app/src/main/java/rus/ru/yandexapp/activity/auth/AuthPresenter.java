package rus.ru.yandexapp.activity.auth;

import javax.inject.Inject;

import rus.ru.yandexapp.base.BasePresenter;
import rus.ru.yandexapp.service.YandexDiskService;

public class AuthPresenter extends BasePresenter<AuthInterface.View> implements AuthInterface.Presenter {

    private boolean forceNewToken = false;

    @Inject
    YandexDiskService yandexDiskService;

    public AuthPresenter(AuthInterface.View view, boolean forceNewToken) {
        super(view);

        this.forceNewToken = forceNewToken;
    }

    @Override
    protected void onStart() {

    }

    @Override
    public boolean isForceNewToken() {
        return forceNewToken;
    }

    @Override
    public void setToken(String string) {
        yandexDiskService.setToken("OAuth " + string);
    }
}