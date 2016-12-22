package com.gh.tools.net;

import com.gh.utils.net.BaseModel;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * author: gh
 * time: 2016/10/5.
 * description: retrofit具体请求接口
 */

public interface ApiService {

    @GET("api/data/福利/{pageCount}/{pageIndex}")
    Observable<BaseModel<ArrayList<Benefit>>> rxBenefits(
            @Path("pageCount") int pageCount,
            @Path("pageIndex") int pageIndex
    );
}
