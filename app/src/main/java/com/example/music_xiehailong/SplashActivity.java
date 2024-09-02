package com.example.music_xiehailong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean hasAgreed = sharedPreferences.getBoolean("has_agreed_privacy_policy", false);

        // 设置通知栏颜色
        getWindow().setStatusBarColor(Color.WHITE);
        // 设置通知栏字体颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if (!hasAgreed) {
            showPrivacyPolicyDialog();
        } else {
            continueToApp();
        }
    }

    private void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.privacy_dialog, null);
        builder.setView(dialogView);

        TextView privacyMessage = dialogView.findViewById(R.id.privacy_message);
        String message = "欢迎使用音乐社区，我们将严格遵守相关法律和隐私政策保护您的个人隐私，请您阅读并同意" +
                "《用户协议》与《隐私协议》。";
        SpannableString spannableString = new SpannableString(message);

        // 设置“隐私政策”和“用户协议”为可点击链接
        ClickableSpan privacyPolicySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 处理点击事件，例如跳转到隐私政策页面
                Toast.makeText(SplashActivity.this, "查看隐私协议", Toast.LENGTH_SHORT).show();
            }
        };
        ClickableSpan userAgreementSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 处理点击事件，例如跳转到用户协议页面
                Toast.makeText(SplashActivity.this, "查看用户协议", Toast.LENGTH_SHORT).show();
            }
        };

        spannableString.setSpan(privacyPolicySpan, 41, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(userAgreementSpan, 48, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacyMessage.setText(spannableString);
        privacyMessage.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // 禁止用户点击外部关闭对话框

        // 处理按钮点击事件
        TextView btnDisagree = dialogView.findViewById(R.id.btn_disagree);
        btnDisagree.setOnClickListener(v -> {
            dialog.dismiss();
            finish(); // 退出应用
        });

        TextView btnAgree = dialogView.findViewById(R.id.btn_agree);
        btnAgree.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("app_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("has_agreed_privacy_policy", true);
            editor.apply();
            dialog.dismiss();
            // 继续进入应用
            continueToApp();
        });

        dialog.show();
    }

    private void continueToApp() {
        // 进入应用的主界面

        // 获取ImageView
        View logoAndText = findViewById(R.id.logoAndText);

        // 创建淡入淡出动画
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(logoAndText, "alpha", 1f, 0f);
        fadeOut.setDuration(2000); // 动画时长2秒

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画结束后启动主页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish(); // 结束SplashActivity
            }
        });

        // 启动动画
        fadeOut.start();
    }
}