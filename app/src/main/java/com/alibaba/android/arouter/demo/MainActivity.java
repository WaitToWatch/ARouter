package com.alibaba.android.arouter.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.demo.testservice.HelloService;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
    }

    public static Activity getThis() {
        return activity;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openLog:
                ARouter.openLog();
                break;
            case R.id.openDebug:
                ARouter.openDebug();
                break;
            case R.id.init:
                ARouter.init(AppContext.getInstance());
                break;
            case R.id.normalNavigation:
                ARouter.getInstance()
                        .build("/test/activity2")
                        .navigation();
                break;
            case R.id.normalNavigation2:
                ARouter.getInstance()
                        .build("/test/activity2")
                        .navigation(this, 666);//for result
                break;
            case R.id.normalNavigationWithParams:
                ARouter.getInstance()
                        .build("/test/activity2")
                        .withString("key1", "value1")//with params
                        .navigation();
                break;
            case R.id.interceptor:
                ARouter.getInstance()
                        .build("/test/activity4")
                        .navigation();
                break;
            case R.id.navByUrl:
                ARouter.getInstance()
                        .build("/test/webview")
                        .withString("url", /*"file:///android_asset/schame-test.html"*/"http://www.sogou.com")
                        .navigation();
                break;
            case R.id.autoInject://Activity中的属性，将会由ARouter自动注入，无需 getIntent().getStringExtra("xxx")等等
                ARouter.enableAutoInject();
                break;
            case R.id.navByName://根据router跳转
                ((HelloService) ARouter.getInstance().build("/service/hello").navigation()).sayHello("mike");
                break;
            case R.id.navByType://无需强转
                ARouter.getInstance().navigation(HelloService.class).sayHello("mike");
                break;
            case R.id.navToMoudle1:
                ARouter.getInstance().build("/module/1").navigation();
                break;
            case R.id.navToMoudle2:
                // 这个页面主动指定了Group名
                ARouter.getInstance().build("/module/2", "m2").navigation();
                break;
            case R.id.destroy:
                ARouter.getInstance().destroy();
                break;
            case R.id.failNav:
                ARouter.getInstance().build("/xxx/xxx").navigation(this, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {

                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.e("ARouter", "找不到了");
                    }
                });
                break;
            case R.id.failNav2:
                ARouter.getInstance().build("/xxx/xxx").navigation();
                break;
            case R.id.failNav3:
                ARouter.getInstance().navigation(MainActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 666:
                Log.e("activityResult", String.valueOf(resultCode));
                break;
            default:
                break;
        }
    }
}
