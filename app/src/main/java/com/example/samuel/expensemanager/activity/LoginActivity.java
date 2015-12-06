package com.example.samuel.expensemanager.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.MyUser;
import com.example.samuel.expensemanager.utils.Constants;
import com.example.samuel.expensemanager.utils.NetUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {


    private static final String TAG = "LoginActivity";
    private static final int LOGIN_SUCCESS = 100;
    private static final int LOGIN_FAIL = 101;
    public static Tencent mTencent;
    private MyUser mBmobUser = null;
    //    private ImageView login_imageView;
    //已登录
    private FloatingActionButton mFabLogin;
    private RelativeLayout login_success_relativelayout;
    private TextView login_success_tv_nickname;
    //未登录
    private RelativeLayout unlogin_relativelayout;
    private EditText et_account;
    private EditText et_pwd;
    private Button btn_qq;
    private Button btn_weibo;
    private Button btn_login;
    private Button btn_register;
    private Button btn_weixin;
    private MyTecentListener mTecentListener;
    private SharedPreferences mSharedPerfarece;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    SPUtils.saveBoolean(LoginActivity.this, "haveLogin", true);
                    initData();
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPerfarece = getSharedPreferences("user", MODE_PRIVATE);
        //初始化BmobSDK
        Bmob.initialize(this, Constants.BMOB_APPID);

        //初始化sharedsdk
        ShareSDK.initSDK(this);

        initView();
        initData();

    }

    private void initData() {
        mBmobUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (mBmobUser != null) {
            //已经登录
            unlogin_relativelayout.setVisibility(View.GONE);
            login_success_relativelayout.setVisibility(View.VISIBLE);

            //设置头像
            ImageLoader imageLoader = ImageLoader.getInstance();

            mSharedPerfarece = getSharedPreferences("user", MODE_PRIVATE);
            String userImageUrl = mSharedPerfarece.getString(mBmobUser.getObjectId() + "_imageUrl", "");
            String userimageUrl = (String) BmobUser.getObjectByKey(this, "userimageurl");
//            if (!"".equals(userimageUrl) || ("").equals(userimageUrl)) {
//                if ("" != userImageUrl) {
//                    imageLoader.displayImage(userImageUrl, login_imageView);
//                } else {
//                    imageLoader.displayImage(userimageUrl, login_imageView);
//                }
//            }

            String nickName = mSharedPerfarece.getString(mBmobUser.getObjectId() + "_nickName", "");
            String nickname = (String) BmobUser.getObjectByKey(this, "nickname");
            login_success_tv_nickname.setText(!"".equals(nickName) ? nickName : nickname);
        } else {
            unlogin_relativelayout.setVisibility(View.VISIBLE);
            login_success_relativelayout.setVisibility(View.GONE);
//            login_imageView.setImageResource(R.drawable.ic_yingyong);
        }

    }

    private void initView() {
        et_account = (EditText) findViewById(R.id.login_et_account);
        et_pwd = (EditText) findViewById(R.id.login_et_pwd);

        btn_login = (Button) findViewById(R.id.btn_login);//登陆按钮
        btn_login.setOnClickListener(this);

        btn_register = (Button) findViewById(R.id.btn_register);//注册按钮
        btn_register.setOnClickListener(this);

        btn_qq = (Button) findViewById(R.id.login_btn_qq);
        btn_weibo = (Button) findViewById(R.id.login_btn_weibo);
        //btn_weixin = (Button) findViewById(R.id.btn_weixin);
        btn_qq.setOnClickListener(this);
        btn_weibo.setOnClickListener(this);
        //btn_weixin.setOnClickListener(this);

        login_success_relativelayout = (RelativeLayout) findViewById(R.id.login_relativelayout);
        unlogin_relativelayout = (RelativeLayout) findViewById(R.id.unlogin_relativelayout);

//        login_imageView = (ImageView) findViewById(R.id.login_imageView);

        mFabLogin = (FloatingActionButton) findViewById(R.id.fab_login);
        mFabLogin.setOnClickListener(this);

        login_success_tv_nickname = (TextView) findViewById(R.id.login_success_tv_nickname);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn_qq: //QQ
                qqAuthorize();
                break;

            case R.id.login_btn_weibo: //weibo
                Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                if (weibo.isValid()) {
                    weibo.removeAccount();
                    Log.i(TAG, "取消授权");
                }
                Log.i(TAG, "++++++++++++++++++++++++dianjiweibo!!!!!!!!!");
                weibo.setPlatformActionListener(this);
                weibo.authorize();
                break;
//
//            case R.id.btn_weixin://weixin
//                Platform weixin = ShareSDK.getPlatform(this, Wechat.NAME);
//                weixin.setPlatformActionListener(this);
//
//                weixin.authorize();
//                break;

            case R.id.btn_register://zhuce
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_login://denglu
                String account = et_account.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (account.equals("")) {
                    toast("填写你的用户名");
                    return;
                }

                if (pwd.equals("")) {
                    toast("填写你的密码");
                    return;
                }

                BmobUser user = new BmobUser();
                user.setUsername(account);
                user.setPassword(pwd);
                user.login(this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        toast("登陆成功");

                        Message msg = handler.obtainMessage();
                        msg.what = LOGIN_SUCCESS;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        // TODO Auto-generated method stub
                        toast("登陆失败: 账号或者密码不对");
                    }
                });
                break;
            default:
                break;
        }
    }

    private void qqAuthorize() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
        }
        mTecentListener = new MyTecentListener();
        mTencent.logout(this);
        mTencent.login(this, "all", mTecentListener);
    }

    //接受回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API) {
            mTencent.handleLoginData(data, mTecentListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.i("TAG", platform.getName() + " " + i);

        BmobThirdUserAuth authInfo = null;
        if (i == 1) { //weibo

            Platform weibo = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
            String accessToken = weibo.getDb().getToken();
            String openId = weibo.getDb().getUserId();
            String expiretime = String.valueOf(weibo.getDb().getExpiresTime());
            String expiresin = String.valueOf(weibo.getDb().getExpiresIn());
            Log.i(TAG + "+++++ ", "微博访问成功,调用第三方+accessToken" + accessToken + openId + expiresin);
            authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_WEIBO, accessToken, expiresin, openId);

        } else if (i == 4) {//weixin

            Platform weixin = ShareSDK.getPlatform(LoginActivity.this, Wechat.NAME);
            String accessToken = weixin.getDb().getToken();
            String openId = weixin.getDb().getUserId();
            String expiretime = String.valueOf(weixin.getDb().getExpiresTime());
            String expiresin = String.valueOf(weixin.getDb().getExpiresIn());
            Log.i(TAG + "+++++ ", "微博访问成功,调用第三方+accessToken" + accessToken + openId + expiresin);
            authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_WEIXIN, accessToken, expiresin, openId);

        }

        //调用第三方
        loginWithAuth(authInfo);

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    //第三方登陆
    public void loginWithAuth(final BmobThirdUserAuth authInfo) {
        if (authInfo != null) {
            Log.e(TAG, "--------------" + authInfo.toString());
        }
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.i("smile", authInfo.getSnsType() + "登陆成功返回:" + userAuth);

                //已经登陆成功
                //1.获得相关信息
                //如果是QQ登陆
                if ("qq" == authInfo.getSnsType()) {
                    // 传入用户信息,并保存在本地
                    getQQInfo(userAuth);

                } else if ("weibo" == authInfo.getSnsType()) {
                    //调用微博API，并保存在本地
                    getWeiboInfo();
                } else if ("weixin" == authInfo.getSnsType()) {
                    getWeixinInfo();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "第三方登陆失败：" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeixinInfo() {
        Platform weixin = ShareSDK.getPlatform(this, Wechat.NAME);
        String imageUrl = weixin.getDb().getUserIcon();
        String nickName = weixin.getDb().getUserName();

        MyUser currentUser = BmobUser.getCurrentUser(LoginActivity.this, MyUser.class);
        currentUser.setNickname(nickName);
        currentUser.setUserimageurl(imageUrl);

        String userObjectId = currentUser.getObjectId();//获得ID

        mSharedPerfarece.edit().putString(userObjectId + "_nickName", nickName).commit();
//        mSharedPerfarece.edit().putString(userObjectId + "_imageUrl", imageUrl).commit();

        Message msg = handler.obtainMessage();
        msg.what = LOGIN_SUCCESS;
        handler.sendMessage(msg);
    }

    //获得微博相关信息
    private void getWeiboInfo() {
        //初始化sharedsdk
        ShareSDK.initSDK(this);
        Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
        Log.i(TAG, "获得微博相关信息" + weibo.toString());

        String imageUrl = weibo.getDb().getUserIcon();
        String nickName = weibo.getDb().getUserName();
        Log.i(TAG, "获得微博相关信息" + imageUrl + nickName);
        MyUser currentUser = BmobUser.getCurrentUser(LoginActivity.this, MyUser.class);

        currentUser.setNickname(nickName);
        currentUser.setUserimageurl(imageUrl);

        String userObjectId = currentUser.getObjectId();//获得ID

        mSharedPerfarece.edit().putString(userObjectId + "_nickName", nickName).commit();
        mSharedPerfarece.edit().putString(userObjectId + "_imageUrl", imageUrl).commit();

        Message msg = handler.obtainMessage();
        msg.what = LOGIN_SUCCESS;
        handler.sendMessage(msg);

    }


    //获得QQ相关的信息
    public void getQQInfo(final JSONObject userAuth) {
        new Thread() {
            @Override
            public void run() {
                String result = null;
                try {
                    String oauth_consumer_key = "222222"; //应用的KEY
                    String info = userAuth.getString("qq");
                    String openid = userAuth.getJSONObject("qq").getString("openid");
                    String access_token = userAuth.getJSONObject("qq").getString("access_token");
                    String expires_in = userAuth.getJSONObject("qq").getString("expires_in");

                    //拼接路径
                    String path = "https://graph.qq.com/user/get_simple_userinfo?oauth_consumer_key=222222&access_token=" + access_token + "&openid=" + openid + "&format=json";
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        result = NetUtils.getStringFromInputstream(is);

                        JSONObject qqInfo = new JSONObject(result);
                        String nickName = qqInfo.getString("nickname");
                        String imageUrl = qqInfo.getString("figureurl_qq_2");

                        //获得当前用户的id
                        MyUser user = BmobUser.getCurrentUser(LoginActivity.this, MyUser.class);

                        if (user != null) {
                            //保存到本地
                            user.setNickname(nickName);
                            user.setUserimageurl(imageUrl);

                            String userObjectId = user.getObjectId();//获得ID
                            mSharedPerfarece.edit().putString(userObjectId + "_nickName", nickName).commit();
                            mSharedPerfarece.edit().putString(userObjectId + "_imageUrl", imageUrl).commit();

                            Message msg = handler.obtainMessage();
                            msg.what = LOGIN_SUCCESS;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.i(TAG, "connection is failed");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void toast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 和第三方账号进行关联
     *
     * @param snsType
     * @param accessToken
     * @param expiresIn
     * @param userId
     * @return void
     * @throws
     * @method associateThirdParty
     */
    private void associateThirdParty(String snsType, String accessToken, String expiresIn, String userId) {
        BmobThirdUserAuth authInfo = new BmobThirdUserAuth(snsType, accessToken, expiresIn, userId);
        BmobUser.associateWithAuthData(this, authInfo, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("关联成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("关联失败：code =" + code + ",msg = " + msg);
            }
        });
    }

    /**
     * 取消关联
     *
     * @param type：只能是三种值：qq、weibo、weixin
     * @return void
     * @throws
     * @method dissociateAuth
     */
    public void dissociateAuth(final String type) {
        BmobUser.dissociateAuthData(this, type, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("取消" + type + "关联成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                if (code == 208) {// 208错误指的是没有绑定相应账户的授权信息
                    toast("你没有关联该账号");
                } else {
                    toast("取消" + type + "关联失败：code =" + code + ",msg = " + msg);
                }
            }
        });
    }

    private class MyTecentListener implements IUiListener {
        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            if (arg0 != null) {
                Log.e(TAG, " " + arg0);
                JSONObject jsonObject = (JSONObject) arg0;
                try {
                    String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                    String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                    String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                    BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_QQ, token, expires, openId);
                    loginWithAuth(authInfo);
                } catch (JSONException e) {
                }
            }
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(LoginActivity.this, "QQ授权出错：" + arg0.errorCode + "--" + arg0.errorDetail, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

            Toast.makeText(LoginActivity.this, "QQ取消授权：", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
//    class AuthListener implements WeiboAuthListener {
//        @Override
//        public void onComplete(Bundle values) {
//            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//            if (mAccessToken != null && mAccessToken.isSessionValid()) {
//                //调用Bmob提供的授权登录方法进行微博登陆，登录成功后，你就可以在后台管理界面的User表中看到微博登陆后的用户啦
//                String token = mAccessToken.getToken();
//                String expires = String.valueOf(mAccessToken.getExpiresTime());
//                String uid = mAccessToken.getUid();
//                Log.i("smile", "微博授权成功后返回的信息:token = " + token + ",expires =" + expires + ",uid = " + uid);
//                BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_WEIBO, token, expires, uid);
//                loginWithAuth(authInfo);
//            }
//        }
//        @Override
//        public void onCancel() {
//            Toast.makeText(LoginActivity.this,
//                    "取消", Toast.LENGTH_LONG).show();
//        }
//        @Override
//        public void onWeiboException(WeiboException e) {
//            Toast.makeText(LoginActivity.this,
//                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }


}

