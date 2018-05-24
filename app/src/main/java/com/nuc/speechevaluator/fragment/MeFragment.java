package com.nuc.speechevaluator.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nuc.speechevaluator.R;
import com.nuc.speechevaluator.activity.MainActivity;
import com.nuc.speechevaluator.db.UserService;
import com.nuc.speechevaluator.db.bean.User;
import com.nuc.speechevaluator.old.sign.SignActivity;
import com.nuc.speechevaluator.util.Config;
import com.nuc.speechevaluator.util.IFragment;

/**
 * 个人中心
 */
public class MeFragment extends Fragment implements IFragment {

    private static final String TAG = "MeFragment";

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private View mRootView;
    private TextView mTvUsername;
    private TextView mTvAdmin;
    private Button mBtnSignOut;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_me, container, false);
        initView(mRootView);
        initData();
        return mRootView;
    }

    private void initView(View rootView) {
        mTvUsername = rootView.findViewById(R.id.tv_me_username);
        mBtnSignOut = rootView.findViewById(R.id.btn_me_sign_out);
        mToolbar = rootView.findViewById(R.id.tb_common);
        mToolbar.setTitle(getString(R.string.me));
        mTvAdmin = rootView.findViewById(R.id.tv_me_admin);
        mBtnSignOut.setOnClickListener(v -> {
            // 退出登录
            UserService.getInstance(getContext()).signOut();
            Intent intent = new Intent(getContext(), SignActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void initData() {
        UserService.getInstance(getContext())
                .getCurrentUser(user ->
                        mHandler.post(
                                () -> {
                                    if (user != null) {
                                        mTvUsername.setText(user.getUsername());
                                        mTvAdmin.setVisibility(user.getType() == User.USER_TYPE_ADMIN ? View.VISIBLE : View.GONE);
                                    }
                                }
                        ));
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
