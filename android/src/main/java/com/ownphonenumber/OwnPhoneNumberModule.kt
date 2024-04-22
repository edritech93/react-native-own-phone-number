package com.ownphonenumber

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod


class OwnPhoneNumberModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  @SuppressLint("MissingPermission", "HardwareIds")
  @ReactMethod(isBlockingSynchronousMethod = true)
  fun getPhoneNumberSync(): String {
    Log.e("TEST_YUDI", "start method get phone number")
    if (reactApplicationContext != null &&
      (reactApplicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && reactApplicationContext.checkCallingOrSelfPermission(
        Manifest.permission.READ_SMS
      ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && reactApplicationContext.checkCallingOrSelfPermission(
        Manifest.permission.READ_PHONE_NUMBERS
      ) == PackageManager.PERMISSION_GRANTED)
    ) {
      val telMgr =
        reactApplicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
      if (telMgr != null) {
        try {
          Log.d("Test", "TelManager phone = ${telMgr.line1Number}")
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager = SubscriptionManager.from(reactApplicationContext)
            val subsInfoList = subscriptionManager.activeSubscriptionInfoList
            Log.d("Test", "Current list = $subsInfoList")
            for (subscriptionInfo in subsInfoList) {
              val number = subscriptionInfo.number
              Log.d("Test", " Number is  $number")
            }
            return "success"
          } else {
            return "under MR1"
          }
        } catch (e: SecurityException) {
          System.err.println("getLine1Number called with permission, but threw anyway: " + e.message)
        }
      } else {
        System.err.println("Unable to getPhoneNumber. TelephonyManager was null")
      }
    }
    return "unknown"
  }

  @ReactMethod
  fun getPhoneNumber(p: Promise) {
    p.resolve(getPhoneNumberSync())
  }


  companion object {
    const val NAME = "OwnPhoneNumber"
  }
}
