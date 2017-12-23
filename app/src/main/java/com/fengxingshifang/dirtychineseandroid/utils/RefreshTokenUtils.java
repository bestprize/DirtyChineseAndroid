package com.fengxingshifang.dirtychineseandroid.utils;

import android.content.Context;
import android.util.Log;

import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.domain.Token;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import android.content.SharedPreferences;

/**
 * Created by git on 2017/12/21.
 */

public class RefreshTokenUtils {

    private String mUrl;
    private String oldToken;
    private String newTokenString;
    private Token newToken;
    SharedPreferences sharedPreferences;

    public String refreshToken(Context ctx) {
        //cache中获取现有token
        oldToken = PrefUtils.getString(ctx, "token", null);
        getDataFromServer(ctx);
        newTokenString = PrefUtils.getString(ctx, "token", null);
        Log.e("TAG2222", "===============++++++++==================" + newTokenString);
        return newTokenString;

    }

    private void getDataFromServer(final Context ctx) {
        mUrl = GlobalConstants.REFRESH_TOKEN;
        mUrl = mUrl + "?token=" + oldToken;
        RequestParams params = new RequestParams(mUrl);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG11111", "===============++++++++==================" + result);
                processData(ctx, result, false);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Log.e("TAG", "xUtis3联网请求失败==" + ex.getMessage());


            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");
            }

        });
    }



    private void processData(Context ctx, String result, boolean isMore) {
        Gson gson = new Gson();
        newToken = gson.fromJson(result, Token.class);
        newTokenString = newToken.getToken();
        //写入新token
        PrefUtils.setString(ctx, "token", newTokenString);

    }



}
