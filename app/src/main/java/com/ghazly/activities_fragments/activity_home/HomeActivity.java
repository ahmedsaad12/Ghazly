package com.ghazly.activities_fragments.activity_home;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ghazly.activities_fragments.activity_login.LoginActivity;
import com.ghazly.databinding.ActivityHomeBinding;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ghazly.R;
import com.ghazly.language.Language;
import com.ghazly.models.UserModel;
import com.ghazly.preferences.Preferences;
import com.ghazly.remote.Api;
import com.ghazly.share.Common;
import com.ghazly.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private UserModel userModel;
    private String lang;
    private ActionBarDrawerToggle toggle;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            binding.setUsermodel(userModel);
            //  Log.e("ldldldll",userModel.getData().getToken());
        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
        toggle.syncState();


        if (userModel != null) {
            EventBus.getDefault().register(this);
            updateTokenFireBase();

        }


    }

    private void Logout() {
        if (userModel != null) {

//
//            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//            dialog.show();
//
//
//            FirebaseInstanceId.getInstance()
//                    .getInstanceId().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    String token = task.getResult().getToken();
//
//                    Api.getService(Tags.base_url)
//                            .logout("Bearer " + userModel.getData().getToken(), token)
//                            .enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    dialog.dismiss();
//                                    if (response.isSuccessful()) {
//                                        preferences.clear(HomeActivity.this);
//                                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                        if (manager != null) {
//                                            manager.cancel(Tags.not_tag, Tags.not_id);
//                                        }
//                                        navigateToSignInActivity();
//
//
//                                    } else {
//                                        dialog.dismiss();
//                                        try {
//                                            Log.e("error", response.code() + "__" + response.errorBody().string());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        if (response.code() == 500) {
//                                            Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    try {
//                                        dialog.dismiss();
//                                        if (t.getMessage() != null) {
//                                            Log.e("error", t.getMessage() + "__");
//
//                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        Log.e("Error", e.getMessage() + "__");
//                                    }
//                                }
//                            });
//
//                }
//            });


        } else {
            navigateToSignInActivity();
        }

    }


    private void updateTokenFireBase() {


//        FirebaseInstanceId.getInstance()
//                .getInstanceId().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                String token = task.getResult().getToken();
//
//                try {
//
//                    try {
//
//                        Api.getService(Tags.base_url)
//                                .updatePhoneToken("Bearer "+userModel.getData().getToken(),token,userModel.getData().getId(),1)
//                                .enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        if (response.isSuccessful() && response.body() != null )
//                                        {
//                                            Log.e("token","updated successfully");
//                                        } else {
//                                            try {
//
//                                                Log.e("error", response.code() + "_" + response.errorBody().string());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                        try {
//
//                                            if (t.getMessage() != null) {
//                                                Log.e("error", t.getMessage());
//                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                        }
//                                    }
//                                });
//                    } catch (Exception e) {
//
//
//                    }
//                } catch (Exception e) {
//
//
//                }
//
//            }
//        });
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 1050);


    }


    @Override
    public void onBackPressed() {
        finish();
    }


    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}