package com.dim.ke.framework.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dim.ke.framework.R;
import com.dim.ke.framework.core.ActivityManager;
import com.dim.ke.framework.core.DimApp;
import com.dim.ke.framework.core.eventbus.BaseEvent;
import com.dim.ke.framework.core.eventbus.EventBusUtils;
import com.dim.ke.framework.core.pageState.PageState;
import com.dim.ke.framework.core.pageState.PageStateLayout;
import com.dim.ke.framework.core.util.LogUtils;
import com.dim.ke.framework.core.util.ToastUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class BaseActivity extends AutoLayoutActivity implements BaseView{

    protected static final String TAG = BaseActivity.class.getSimpleName();

    private Context mContext;

    protected ViewGroup mRootView;
    protected ViewStub mViewStubTitle;
    protected FrameLayout mViewStubContent;

    protected TextView mTxtTitle;
    protected Toolbar mToolbar;
    protected TextView mLeftTxt1;
    protected TextView mLeftTxt2;
    protected TextView mRightTxt1;
    protected TextView mRightTxt2;

    protected PageState mPageState;

    @Override
    public Context getContext() {
        mContext = this;
        return this;
    }

    protected boolean isWithTitle(){
        return true;
    }

    protected int getTitleLayout(){
        return R.layout.common_title_layout;
    }

    protected int getOrientation(){
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            LogUtils.i("onCreate fixOrientation when Oreo, result = " + result);
        }
        beforeOnCreate();
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.content_layout);

        mRootView = findViewById(android.R.id.content);
        mViewStubTitle = findViewById(R.id.view_title_sub);
        mViewStubContent = findViewById(R.id.view_content_sub);
        mPageState = PageStateLayout.wrap(this, R.id.view_content_sub);

        setRequestedOrientation(getOrientation());
        ARouter.getInstance().inject(this);
        ActivityManager.newInstance().addActivity(this);
        initTitleView();
        initContentView();
        initView();
        initListener();
        initData();
        EventBusUtils.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
        ActivityManager.newInstance().removeActivity(this);
        destroyView();
    }

    private void initTitleView(){
        if(isWithTitle()){
            mViewStubTitle.setLayoutResource(getTitleLayout());
            initToolBar(mViewStubTitle.inflate());
        }
    }

    private void initToolBar(View view){
        mToolbar = view.findViewById(R.id.toolbar_root);
        mTxtTitle = view.findViewById(R.id.toolbar_title);
        mTxtTitle.setText(initTitleName());

        mLeftTxt1 = view.findViewById(R.id.toolbar_left_1);
        mLeftTxt2 = view.findViewById(R.id.toolbar_left_2);
        mRightTxt1 = view.findViewById(R.id.toolbar_right_1);
        mRightTxt2 = view.findViewById(R.id.toolbar_right_2);

        mLeftTxt1.setOnClickListener(titleClickListener);
        mLeftTxt2.setOnClickListener(titleClickListener);
        mRightTxt1.setOnClickListener(titleClickListener);
        mRightTxt2.setOnClickListener(titleClickListener);

    }

    private View.OnClickListener titleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.toolbar_left_1){
                leftClick1();
            }else if(id == R.id.toolbar_left_2){
                leftClick2();
            }else if(id == R.id.toolbar_right_1){
                rightClick1();
            }else if(id == R.id.toolbar_right_2){
                rightClick2();
            }
        }
    };

    protected void leftClick1(){
        onBackPressed();
    }
    protected void leftClick2(){

    }
    protected void rightClick1(){

    }
    protected void rightClick2(){

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mViewStubContent != null) {
            initContentView(layoutResID);
        }
    }

    protected void initContentView(){

        initContentView(getLayoutId());
    }

    private void initContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mViewStubContent, false);
        mViewStubContent.setId(android.R.id.content);
        mRootView.setId(View.NO_ID);
        mViewStubContent.removeAllViews();
        mViewStubContent.addView(view);
    }

    protected void debug(String msg){
        LogUtils.d(TAG, msg);
    }

    protected void toShowToast(String msg){
        ToastUtils.showToast(mContext, msg);
    }

    public <APP extends DimApp> APP getApp(){
        return (APP) getApplication();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public <T> void onEvent(BaseEvent<T> event) {
    }

    @Override
    public void showLoadingView() {
        mPageState.showLoadingView();
    }

    @Override
    public void showEmptyView() {
        mPageState.showEmptyView();
    }

    @Override
    public void showLoadErrView() {
        mPageState.showErrorView();
    }

    @Override
    public void showContentView() {
        mPageState.showContentView();
    }

    /**
     * 修复google 8.0 Only fullscreen activities can request orientation 异常
     *
     * @return
     */
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            LogUtils.i("avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    /**
     * 防止用户设置系统字体大小对app布局产生影响
     * @return
     */
    @Override
    public Resources getResources() {

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;

    }

    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.fontScale != 1) {//非默认值
            getResources();
        }

        super.onConfigurationChanged(newConfig);

    }
}
