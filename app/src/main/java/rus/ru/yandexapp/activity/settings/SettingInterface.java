package rus.ru.yandexapp.activity.settings;

import rus.ru.yandexapp.base.AbstractInterface;

public interface SettingInterface extends AbstractInterface {

    interface View extends AbstractInterface.View<Presenter> {
    }

    interface Presenter extends AbstractInterface.Presenter {

    }
}