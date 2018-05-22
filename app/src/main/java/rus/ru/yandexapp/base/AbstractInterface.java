package rus.ru.yandexapp.base;

import android.os.Bundle;

public interface AbstractInterface {

    interface View<T extends Presenter> extends BaseView<T> {
        Bundle saveInstance();

        void restoreInstance(Bundle b);

        void showNetworkError();

        void showServerError();

    }

    interface Presenter {
        void start();
    }

}

