package com.encrypt.im.business.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encrypt.im.R;
import com.encrypt.im.base.BaseFragment;
import com.encrypt.im.common.log.LogUtil;

/**
 * Created by v_zhaoqingfa on 2018/12/18.
 */

public class FoundFragment extends BaseFragment {
    public static Fragment newInstance() {
        return new FoundFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_found, container, false);
        LogUtil.e("=========", "3");
        return inflate;
    }
}
