package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.MyUser;
import com.example.samuel.expensemanager.utils.SPUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private static int CAMER_REQUEST_CODE = 20;
    private static int TUKU_REQUEST_CODE = 30;
    private static int CAIJIAN_REQUEST_CODE = 40;
    private TextInputLayout inputLayoutUserName;
    private TextInputLayout inputLayoutNickName;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private TextInputLayout inputLayoutRePassword;
    private EditText regist_et_account;
    private EditText regist_et_pwd;
    private EditText regist_et_repwd;
    private EditText regist_et_nickname;
    private EditText regist_et_email;
    private Button regist_btn_reg;
    private Button regist_btn_cancel;
    private ImageView regist_iv_touxiang;
    private Button regist_bn_touxiang;
    private ProgressBar regist_progressBar_touxiang;
    private Uri mImageUri;

    private String mLoadfilePath;
    private String mSuccessLoadFilePath;
    private String mSuccessLoadFileName;
    private boolean isLoadFileSuccess = false;
    private boolean isLoadButtonKicked = false;


    //1.点击之后检验照片是否存在,文字变化 I/+++++上传图片的路径: mLoadfilePath:/storage/emulated/0/com.expansemanager.pic/user.png
    //2.I/bmob: 图片上传成功：ffa6be203f734c349caed6ed46c203a8.png,文件路径:http://file.bmob.cn/M02/DC/7C/oYYBAFZkE6CAV6aoAAAmE6xLBdo489.png
    //3 +++++Regist: id:2c7285df26

    //从URI上去取数据
    /*public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("注册");
        }


        initView();
        initEvent();
    }

    private void initEvent() {

        regist_btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = regist_et_account.getText().toString().trim();
                String pwd = regist_et_pwd.getText().toString().trim();
                String repwd = regist_et_repwd.getText().toString().trim();
                String nickname = regist_et_nickname.getText().toString().trim();
//                String email = regist_et_email.getText().toString().trim();

                //panduangeshi
                if (!submitForm()) {
                    return;
                }

                MyUser user = new MyUser();
                user.setUsername(username);
                user.setPassword(pwd);
                user.setNickname(nickname);
//                user.setEmail(email);


//                if (isLoadFileSuccess) {//如果上传成功
//                    user.setUserimageurl(mSuccessLoadFilePath);
//
//                }
                user.signUp(RegisterActivity.this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        SPUtils.saveBoolean(RegisterActivity.this, "haveLogin", true);
                        toast("注册成功");
                        MyUser user = BmobUser.getCurrentUser(RegisterActivity.this, MyUser.class);
                        if (user != null) {
                            Log.i("+++++Regist", "id:" + user.getObjectId());
                            //保存当前用户的昵称和头像地址
                            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                            sp.edit().putString(user.getObjectId() + "_nickName", user.getNickname()).commit();

                        }
                        //跳转回登陆界面
                        Intent intent = new Intent(RegisterActivity.this, UserActivity.class);

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("注册失败,已经有该用户");
                    }
                });
            }
        });

        //重置
        regist_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*regist_et_account.setText("");
                regist_et_pwd.setText("");
                regist_et_nickname.setText("");
                regist_et_email.setText("");*/
                finish();
            }
        });


        //上传头像
        /*regist_bn_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadButtonKicked = true;

                if (mLoadfilePath != null) {
                    toast("开始上传文件");
                    regist_bn_touxiang.setText("正在进行上传处理,请稍后");
                    regist_bn_touxiang.setClickable(false);
                    regist_progressBar_touxiang.setVisibility(View.VISIBLE);
                    BmobProFile.getInstance(RegisterActivity.this).upload(mLoadfilePath, new UploadListener() {
                        @Override
                        public void onSuccess(String fileName, String url, BmobFile file) {
                            Log.i("bmob", "图片上传成功：" + fileName + ",文件路径:" + file.getUrl());
                            //记录上传成功后的文件路径
                            mSuccessLoadFilePath = file.getUrl();
                            mSuccessLoadFileName = fileName;
                            isLoadFileSuccess = true;
                            regist_progressBar_touxiang.setVisibility(View.GONE);
                            toast("上传成功");
                            regist_bn_touxiang.setText("上传成功");
                        }

                        @Override
                        public void onProgress(int i) {
                            regist_progressBar_touxiang.setProgress(i);
                        }

                        @Override
                        public void onError(int i, String s) {
                            toast("上传失败");
                            isLoadFileSuccess = false;
                            regist_progressBar_touxiang.setVisibility(View.GONE);
                            regist_bn_touxiang.setText("上传失败，点击再次上传");
                            regist_bn_touxiang.setClickable(true);
                        }
                    });
                } else {
                    regist_bn_touxiang.setText("上传图片路径为空，点击上方空白选择图片");
                    regist_progressBar_touxiang.setVisibility(View.GONE);
                    regist_bn_touxiang.setClickable(true);
                }
            }
        });*/

        //加载图片
        /*regist_iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出菜单
                String[] items = {"从图库中选", "从相机中选"};
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                toast("按键0");
                                go2Galley();
                                break;

                            case 1:
                                toast("按键1");

                                go2Camer();
                                break;
                        }
                    }
                }).show();
            }
        });*/
    }

    //跳转到相机
    /*public void go2Camer() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMER_REQUEST_CODE);
    }

    //跳转到相册
    public void go2Galley() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");
        startActivityForResult(intent, TUKU_REQUEST_CODE);
    }
*/
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMER_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                    startImageZoom(uri);//file类型
                }
            }

        } else if (requestCode == TUKU_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Uri uri;
                uri = data.getData();//content类型
                Uri fileUri = convertUri(uri);
                startImageZoom(fileUri);
            }
        } else if (requestCode == CAIJIAN_REQUEST_CODE) {
            if (mImageUri == null) {
                Log.i("!!!!!!!!!!!", "裁剪返回空");
                return;
            } else {
                Bitmap bitmap = getBitmapFromUri(mImageUri, this);

                if (bitmap == null) {
                    Log.i("!!!!!!!!!!!", "图片为返回空");
                }

                regist_iv_touxiang.setImageBitmap(bitmap);
            }
        }
    }*/

    //转化uri
  /*  private Uri convertUri(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            //将图像写入到SD卡，返回URI
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    //启动图像裁剪界面
    /*private void startImageZoom(Uri uri) {

        mImageUri = uri; //保存uri

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CAIJIAN_REQUEST_CODE);
    }*/


    /*private Uri saveBitmap(Bitmap bitmap) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.expansemanager.pic");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File img = new File(tmpDir.getAbsolutePath(), "user.png");
        try {
            //bitmap 写入文件
            FileOutputStream fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            mLoadfilePath = img.getAbsolutePath();
            if (mLoadfilePath != null) {
                Log.i("+++++上传图片的路径", "mLoadfilePath:" + mLoadfilePath);
            }
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    private void initView() {

        inputLayoutUserName = (TextInputLayout) findViewById(R.id.regist_fl_username);
        inputLayoutNickName = (TextInputLayout) findViewById(R.id.regist_fl_nickname);
//        inputLayoutEmail = (TextInputLayout) findViewById(R.id.regist_fl_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.regist_fl_repassword);
        inputLayoutRePassword = (TextInputLayout) findViewById(R.id.regist_fl_repassword);

        regist_et_account = (EditText) findViewById(R.id.regist_et_account);
        regist_et_pwd = (EditText) findViewById(R.id.regist_et_pwd);
        regist_et_repwd = (EditText) findViewById(R.id.regist_et_repwd);
        regist_et_nickname = (EditText) findViewById(R.id.regist_et_nickname);
//        regist_et_email = (EditText) findViewById(R.id.regist_et_email);

        regist_et_account.addTextChangedListener(new MyTextWatcher(regist_et_account));
        regist_et_pwd.addTextChangedListener(new MyTextWatcher(regist_et_pwd));
        regist_et_repwd.addTextChangedListener(new MyTextWatcher(regist_et_repwd));
        regist_et_nickname.addTextChangedListener(new MyTextWatcher(regist_et_nickname));


        regist_btn_reg = (Button) findViewById(R.id.regist_btn_reg);
        regist_btn_cancel = (Button) findViewById(R.id.regist_btn_cancel);


    }

    private void toast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    //重新定位焦点
    private void requestFoucs(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE));
        }
    }

    private boolean submitForm() {
        if (!validateAcount() || !validateNickName() || !validatePassword() || !validateRePassword()) {
            toast("请把资料填写完全");
            return false;
        }
        return true;
    }

    //校验账户
    private boolean validateAcount() {
        if (regist_et_account.getText().toString().trim().isEmpty()) {
            inputLayoutUserName.setError("用户名不能为空");
            requestFoucs(regist_et_account);
            return false;
        } else {
            inputLayoutUserName.setErrorEnabled(false);
        }
        return true;
    }

    //校验昵称
    private boolean validateNickName() {
        if (regist_et_nickname.getText().toString().trim().isEmpty()) {
            inputLayoutNickName.setError("昵称不能为空");
            requestFoucs(regist_et_nickname);
            return false;
        } else {
            inputLayoutNickName.setErrorEnabled(false);
        }
        return true;
    }

    //检验密码
    private boolean validatePassword() {
        if (regist_et_pwd.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("密码不能为空");
            requestFoucs(regist_et_pwd);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    //检验重复密码
    private boolean validateRePassword() {
        if (regist_et_repwd.getText().toString().trim().isEmpty()) {
            inputLayoutRePassword.setError("重复密码不能为空");
            requestFoucs(regist_et_repwd);
            return false;
        } else if (!regist_et_repwd.getText().toString().trim().equals(regist_et_pwd.getText().toString().trim())) {
            inputLayoutRePassword.setError("两次密码不一致");
            requestFoucs(regist_et_repwd);
            return false;
        } else {
            inputLayoutRePassword.setErrorEnabled(false);
        }
        return true;
    }

    //校验邮箱
   /* private boolean validateEmail() {
        String email = regist_et_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("邮箱为空或者不符合格式");
            requestFoucs(regist_et_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }*/

    private class MyTextWatcher implements TextWatcher {
        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.regist_et_account:
                    // toast("校验用户名");
                    validateAcount();

                    break;
                case R.id.regist_et_pwd:
                    //toast("校验密码");
                    validatePassword();
                    break;
                case R.id.regist_et_repwd:
                    // toast("校验检验密码");
                    validateRePassword();
                    break;
                case R.id.regist_et_nickname:
                    // toast("校验昵称");
                    validateNickName();
                    break;

                default:
                    break;
            }
        }
    }
}