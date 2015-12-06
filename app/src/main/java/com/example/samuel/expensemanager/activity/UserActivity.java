package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.v3.BmobUser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView user_iv_head;
    private TextView user_tv_nickname;
    private Button user_bt_update;
    private TextView user_tv_checkout;
    private MyUser mBmobUser;
    private ImageLoader mImageLoader;
    private SharedPreferences mSharedPerfarece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        mBmobUser = BmobUser.getCurrentUser(this, MyUser.class);

        initView();
        initLogin();
    }

    private void initLogin() {
        if (mBmobUser != null) {
            //设置头像
            mSharedPerfarece = getSharedPreferences("user", MODE_PRIVATE);
            String userImageUrl = mSharedPerfarece.getString(mBmobUser.getObjectId() + "_imageUrl", "");
            String userimageUrl = (String) BmobUser.getObjectByKey(this, "userimageurl");
            if (!"".equals(userimageUrl) || ("").equals(userimageUrl)) {
                if ("" != userImageUrl) {
                    mImageLoader.displayImage(userImageUrl, user_iv_head);
                } else {
                    mImageLoader.displayImage(userimageUrl, user_iv_head);
                }
            }

            String nickName = mSharedPerfarece.getString(mBmobUser.getObjectId() + "_nickName", "");
            String nickname = (String) BmobUser.getObjectByKey(this, "nickname");
            if (!"".equals(userimageUrl) || ("").equals(userimageUrl)) {
                user_tv_nickname.setText(nickname);
            } else {
                user_tv_nickname.setText(nickName);
            }
        }
    }

    private void initView() {
        mImageLoader = ImageLoader.getInstance();
        user_iv_head = (ImageView) findViewById(R.id.user_iv_head);
        user_tv_nickname = (TextView) findViewById(R.id.user_tv_nickname);
        user_bt_update = (Button) findViewById(R.id.user_bt_update);
        user_tv_checkout = (TextView) findViewById(R.id.user_tv_checkout);
        user_bt_update.setOnClickListener(this);
        user_tv_checkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.user_bt_update://修改密码
//                Intent intent = new Intent(UserActivity.this,ChangePWordActivity.class);
//

            case R.id.user_tv_checkout://退出登陆
                BmobUser.logOut(UserActivity.this);
                if (null == BmobUser.getCurrentUser(UserActivity.this)) {
                    mBmobUser = null;
                    Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
        }
    }
}
