package com.footballmatch.live.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.firebase.ui.auth.AuthUI;
import com.footballmatch.live.data.managers.SharedPreferencesManager;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.ui.activities.AuthActivity;
import com.footballmatch.live.ui.activities.LiveMatchesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

/**
 * Created by David Fortunato on 24/04/2018
 * All rights reserved GoodBarber
 */

public class AuthManagerSingleton
{
    public static final int RC_SIGN_IN = 123;

    private static final String KEY_EMAIL = "KEY_EMAIL";

    private static AuthManagerSingleton instance;

    private AuthManagerSingleton()
    {
    }

    public static AuthManagerSingleton getInstance() {
        if(instance == null) {
            instance = new AuthManagerSingleton();
        }
        return instance;
    }

    /**
     * Called by SplashScreen
     * @param activity
     */
    public void initApp(Activity activity) {
        if(isLoggedin(activity) || !StartupManager.getInstance(activity).getAppConfigs().isAuthenticationEnabled()) {
            // Continue normal app flow
            LiveMatchesActivity.startActivity(activity);
        } else {
            Intent intent = new Intent(activity, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }


    /**
     * Init on the AuthActivity
     * @param activity
     */
    public void startAuthUI(Activity activity) {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        // Create and launch sign-in intent
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void logout(final Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferencesManager.putStringMessage(context, KEY_EMAIL, "");
                    }
                });
    }

    public void signinSuccess(Activity activity, FirebaseUser user) {
        SharedPreferencesManager.putStringMessage(activity, KEY_EMAIL, user.getEmail());
        // Continue normal app flow
        LiveMatchesActivity.startActivity(activity);
        activity.finish();
    }

    public boolean isLoggedin(Context context) {
        String email = SharedPreferencesManager.getStringMessage(context, KEY_EMAIL, null);
        return email != null && !email.isEmpty();
    }
}
