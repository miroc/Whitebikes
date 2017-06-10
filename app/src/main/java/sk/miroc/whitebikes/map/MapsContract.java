package sk.miroc.whitebikes.map;

import sk.miroc.whitebikes.base.BasePresenter;
import sk.miroc.whitebikes.base.BaseView;

public interface MapsContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
