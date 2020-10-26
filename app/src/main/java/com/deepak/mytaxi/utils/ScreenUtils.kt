package com.deepak.mytaxi.utils

import android.content.res.Resources


    object ScreenUtils {

        fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

        fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels
    }
