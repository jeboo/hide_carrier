package com.none.hidecarrier_zte;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.view.View;
import android.widget.TextView;

public class hide_carrier implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (!loadPackageParam.packageName.equals("com.android.systemui")) {
            return;
        }

        findAndHookMethod("com.android.systemui.statusbar.phone.PhoneStatusBar", loadPackageParam.classLoader, "updateCarrierLabelVisibility", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("after carriervisibility!");

                View mCarrierLabel = (View) XposedHelpers.getObjectField(param.thisObject, "mCarrierLabel");

                if (mCarrierLabel == null)
                    return;

                //XposedBridge.log("kill carrier(visibility).");
                mCarrierLabel.setVisibility(View.GONE);
            }
        });

        findAndHookMethod("com.android.keyguard.CarrierText", loadPackageParam.classLoader, "showWhich", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("after showwhich!");

                Integer mNumPhones = (Integer) XposedHelpers.getObjectField(param.thisObject, "mNumPhones");
                TextView[] mOperatorName = new TextView[mNumPhones];
                mOperatorName = (TextView[]) XposedHelpers.getObjectField(param.thisObject, "mOperatorName");

                if (mOperatorName == null)
                    return;

                //XposedBridge.log("kill carrier(showwhich).");
                mOperatorName[0].setVisibility(TextView.GONE);
                if (mNumPhones == 2)
                {
                    TextView sepTextView = (TextView) XposedHelpers.getObjectField(param.thisObject, "mOperatorSeparator");
                    sepTextView.setVisibility(TextView.GONE);
                    mOperatorName[1].setVisibility(TextView.GONE);
                }
            }
        });
    }
}