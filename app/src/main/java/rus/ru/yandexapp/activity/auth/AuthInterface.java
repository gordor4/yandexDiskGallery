package rus.ru.yandexapp.activity.auth;

import rus.ru.yandexapp.base.AbstractInterface;

public interface AuthInterface extends AbstractInterface {

    interface View extends AbstractInterface.View<AuthInterface.Presenter> {
    }

    interface Presenter extends AbstractInterface.Presenter {
        void setToken(String string);

        boolean isForceNewToken();

    }
}