package com.wessam.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.wessam.quizapp.utils.Constants.SHARED_PREF_NAME;
import static com.wessam.quizapp.utils.Constants.USER_UID;

public class PreferenceHelper {

    private SharedPreferences mSharedPreferences;

    public PreferenceHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getUserUid() {
        return mSharedPreferences.getString(USER_UID, null);
    }

    public void setUserUid(String uid) {
        mSharedPreferences.edit().putString(USER_UID, uid).apply();
    }

}
