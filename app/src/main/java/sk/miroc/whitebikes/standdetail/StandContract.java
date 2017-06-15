package sk.miroc.whitebikes.standdetail;


import java.util.List;

import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;
import sk.miroc.whitebikes.data.models.Bike;
import sk.miroc.whitebikes.data.models.UserStatus;

public interface StandContract {

    interface View extends BaseView<Presenter> {
        void setStandName(String text);
        void setStandDescription(String text);

        void loadStandPhoto(String standPhotoUrl);

        void addBikeButtons(List<Bike> bikes);
    }

    interface Presenter extends BasePresenter {
    }
}
