package com.jschdev.miLampara.acra;

/**
 * Created by tcsadmin on 03/12/2015.
 */

import android.app.Application;

import com.jschdev.miLampara.utils.AppConstant;
import com.jschdev.miLampara.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;


@ReportsCrashes(
//      formKey = "", // This is required for backward compatibility but not used
//      formUri = "http://www.backendofyourchoice.com/reportpath"
        mailTo = AppConstant.CORREO,
        mode = ReportingInteractionMode.SILENT,
        resToastText = R.string.crash_toast_text
)
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.mipmap.ic_lamp);
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}
