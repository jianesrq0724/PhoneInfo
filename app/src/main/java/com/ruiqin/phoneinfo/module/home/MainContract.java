package com.ruiqin.phoneinfo.module.home;

import com.ruiqin.phoneinfo.base.BaseModel;
import com.ruiqin.phoneinfo.base.BasePresenter;
import com.ruiqin.phoneinfo.base.BaseView;
import com.ruiqin.phoneinfo.module.home.adapter.MainRecyclerAdapter;
import com.ruiqin.phoneinfo.module.home.bean.MainRecyclerData;

import java.util.List;

/**
 * Created by ruiqin.shen
 * 类说明：
 */

public interface MainContract {
    interface Model extends BaseModel {
        List<MainRecyclerData> initData();
    }

    interface View extends BaseView {
        void setRecyclerAdapterSuccess(MainRecyclerAdapter mainRecyclerAdapter);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void setAdapter();
    }
}
