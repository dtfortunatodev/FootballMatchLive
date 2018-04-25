package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.firebase.ui.auth.IdpResponse;
import com.footballmatch.live.R;
import com.footballmatch.live.managers.AuthManagerSingleton;
import com.footballmatch.live.ui.views.BasePopupDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David Fortunato on 24/04/2018
 * All rights reserved GoodBarber
 */

public class AuthActivity extends Activity
{
    private static final String TAG = AuthActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        AuthManagerSingleton.getInstance().startAuthUI(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AuthManagerSingleton.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthManagerSingleton.getInstance().signinSuccess(this, user);
            } else {
                // Sign in failed, check response for error code
                this.displayFailedScreen();
            }
        }
    }

    private void displayFailedScreen() {
        BasePopupDialog basePopupDialog = new BasePopupDialog(this);
        basePopupDialog.setupPoup("Sorry but was not possible to Signin your account. Please try again", "Retry", "Exit", new BasePopupDialog.OnPopupListener()
        {
            @Override
            public void onLeftBtnClick(BasePopupDialog basePopupDialog)
            {
                AuthManagerSingleton.getInstance().startAuthUI(AuthActivity.this);
            }

            @Override
            public void onRightBtnClick(BasePopupDialog basePopupDialog)
            {
                finish();
            }

            @Override
            public void onCancelled()
            {

            }
        });
        basePopupDialog.show();
    }
}
