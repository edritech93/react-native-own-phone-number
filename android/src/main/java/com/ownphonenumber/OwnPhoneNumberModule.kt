package com.ownphonenumber

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.SubscriptionManager.DEFAULT_SUBSCRIPTION_ID
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
    Log.e(TAG, "start method get phone number")
    if (reactApplicationContext != null &&
      (reactApplicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && reactApplicationContext.checkCallingOrSelfPermission(
        Manifest.permission.READ_SMS
      ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && reactApplicationContext.checkCallingOrSelfPermission(
        Manifest.permission.READ_PHONE_NUMBERS
      ) == PackageManager.PERMISSION_GRANTED)
    ) {
      val telMgr =
        reactApplicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
      try {
        Log.e(TAG, "TelManager phone = ${telMgr.line1Number}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
          val subscriptionManager = reactApplicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
          val subsInfoList = subscriptionManager.activeSubscriptionInfoList
          Log.e("Test", "Current list = $subsInfoList")
          for (subscriptionInfo in subsInfoList) {
            val number = subscriptionInfo.number
            Log.e("Test", " Number is  $number")
          }
          Log.e(TAG, Build.VERSION.SDK_INT.toString())
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val phoneNumber = subscriptionManager.getPhoneNumber(DEFAULT_SUBSCRIPTION_ID)
            Log.e(TAG, phoneNumber)
          }
          return "success"
        } else {
          return "under MR1"
        }
      } catch (e: SecurityException) {
        System.err.println("getLine1Number called with permission, but threw anyway: " + e.message)
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
    const val TAG = "Test"
  }
}
