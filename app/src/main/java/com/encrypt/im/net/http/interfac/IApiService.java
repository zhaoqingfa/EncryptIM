package com.encrypt.im.net.http.interfac;

import com.encrypt.im.business.login.LoginBean;
import com.encrypt.im.net.http.RootBean;
import com.encrypt.im.util.Config;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zqf on 2018/11/23.
 */

public interface IApiService {
    public static final String BASE_URL = Config.ROOT_URL + "/";

    @FormUrlEncoded
    @POST("toutiaoguanggao/interface/appInterFace.jsp")
    Observable<RootBean<LoginBean>> login(@FieldMap Map<String, String> formMap);
}
