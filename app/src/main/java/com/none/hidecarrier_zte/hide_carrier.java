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

        findAndHookMethod("com.android.keyguard.CarrierText", loadPackageParam.classLoader, "updateText", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("after updatetext!");

                Integer mNumPhones = (Integer) XposedHelpers.getObjectField(param.thisObject, "mNumPhones");
                if (mNumPhones == 0)
                    return;

                TextView[] mCarrierName = new TextView[mNumPhones];
                mCarrierName = (TextView[]) XposedHelpers.getObjectField(param.thisObject, "mCarrierName");

                if (mCarrierName == null)
                    return;

                //XposedBridge.log("kill carrier(updatetext).");

                mCarrierName[0].setVisibility(TextView.GONE);
                if (mNumPhones == 2)
                {
                    TextView sepTextView = (TextView) XposedHelpers.getObjectField(param.thisObject, "mOperatorSeparator");
                    sepTextView.setVisibility(TextView.GONE);
                    mCarrierName[1].setVisibility(TextView.GONE);
                }
            }
        });
    }
}