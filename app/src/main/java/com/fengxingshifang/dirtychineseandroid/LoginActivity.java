package com.fengxingshifang.dirtychineseandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.domain.RegisterFlag;
import com.fengxingshifang.dirtychineseandroid.domain.Token;
import com.fengxingshifang.dirtychineseandroid.domain.Token4LoginRtn;
import com.fengxingshifang.dirtychineseandroid.domain.UserRorL;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by git on 2017/12/7.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String APP_ID = "1106574339";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private Button btnQQLogin;
    private String isregister;
    private String mUrl;
    private String jsonStringResponse;
    private UserRorL userRorL;
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_login);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, LoginActivity.this.getApplicationContext());
        userRorL = new UserRorL();
        btnQQLogin = (Button) findViewById(R.id.btn_login_qq);
        btnQQLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonLoginQQ(v);
            }
        });
    }

    public void buttonLoginQQ(View v) {
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(LoginActivity.this, "all", mIUiListener);
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            ctx = getApplicationContext();
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                //判断openid在user表中是否存在，若不存在，则是第一次登录，先注册，再登录；若存在，则已注册，直接登录
                //判断openid在user表中是否存在

                userRorL.setRegisterstyle("qq");
                userRorL.setThirdpartyid(openID);
                isRegister(userRorL);

                //若不存在，注册
//                if("0".equals(isregister)){
//                    userRegister(userRorL);
//                } else {
//                    //若存在，登录
//                    userLogin(userRorL);
//                }








                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        //是一个json串response.tostring，直接使用gson解析就好
                        Log.e(TAG, "登录成功" + response.toString());
                        //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                        // Nickname  昵称
                        //Figureurl_qq_1 //头像
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void isRegister(final UserRorL userRorL) {
        Toast.makeText(LoginActivity.this, "开始判断是否已注册", Toast.LENGTH_SHORT).show();
        mUrl = GlobalConstants.USER_IS_REGISTER;
        Gson gson=new Gson();
        jsonStringResponse = gson.toJson(userRorL);
        Log.e("TAG1111----------------", jsonStringResponse);
        RequestParams params = new RequestParams(mUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(jsonStringResponse);
        params.addQueryStringParameter("wd","xUtils");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(LoginActivity.this, "判断是否已注册服务器返回", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                isregister = gson.fromJson(result, RegisterFlag.class).getIsRegisterFlag();
                //若不存在，注册
                if("0".equals(isregister)){
                    Toast.makeText(LoginActivity.this, "判断出未注册", Toast.LENGTH_SHORT).show();
                    userRegister(userRorL);
                } else {
                    //若存在，登录
                    Toast.makeText(LoginActivity.this, "判断出已注册", Toast.LENGTH_SHORT).show();
                    userLogin(userRorL);
                }
                Log.e("TAG", "xUtis3联网请求success==");

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


    private void userRegister(final UserRorL userRorL) {
        Toast.makeText(LoginActivity.this, "开始注册", Toast.LENGTH_SHORT).show();
        mUrl = GlobalConstants.USER_REGISTER;
        Gson gson=new Gson();
        jsonStringResponse = gson.toJson(userRorL);
        Log.e("TAG1111----------------", jsonStringResponse);
        RequestParams params = new RequestParams(mUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(jsonStringResponse);
        params.addQueryStringParameter("wd","xUtils");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //若注册成功，登录
                userLogin(userRorL);
                Log.e("TAG", "xUtis3联网请求success==");

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

    private void userLogin(UserRorL userRorL) {
        Toast.makeText(LoginActivity.this, "开始登录", Toast.LENGTH_SHORT).show();
        mUrl = GlobalConstants.USER_LOGIN;
        Gson gson=new Gson();
        jsonStringResponse = gson.toJson(userRorL);
        Log.e("TAG1111----------------", jsonStringResponse);
        RequestParams params = new RequestParams(mUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(jsonStringResponse);
        params.addQueryStringParameter("wd","xUtils");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //登录成功后的token放入PrefUtils中
                Gson gson = new Gson();
                String token4LoginRtn = gson.fromJson(result, Token4LoginRtn.class).getToken();
                PrefUtils.setString(LoginActivity.this, "token", token4LoginRtn);

                Log.e("TAG", "xUtis3联网请求success==");

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



}
