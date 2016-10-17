package com.jschdev.miLampara;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jschdev.miLampara.utils.HoloCircleSeekBar;

import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {
    final static String TAG = "myLamp";
    private CountDownTimer countDownTimer;
    private Button btnON, btnOFF, btnRestart;
    private long startTime = 1000;
    private final long interval = 1 * 1000;

    HoloCircleSeekBar numHoras;
    HoloCircleSeekBar numMinutos;
    HoloCircleSeekBar numSegundos;

    String manuName = android.os.Build.MANUFACTURER.toLowerCase();
    final static String motorola = "motorola";
    Camera mCamera;
    int count = 0;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        mContext = getApplicationContext();

        numHoras = (HoloCircleSeekBar) findViewById(R.id.horas);
        numMinutos = (HoloCircleSeekBar) findViewById(R.id.minutos);
        numSegundos = (HoloCircleSeekBar) findViewById(R.id.segundos);

        numHoras.setMinimumWidth(200);
        numMinutos.setMinimumWidth(200);
        numSegundos.setMinimumWidth(200);

        btnRestart = (Button) this.findViewById(R.id.btnRestart);
        btnOFF = (Button) this.findViewById(R.id.btnStop);
        btnON = (Button) this.findViewById(R.id.btnStart);

        btnRestart.setText(getResources().getString(R.string.txt_boton_reiniciar));
        btnON.setText(getResources().getString(R.string.txt_boton_prender));
        btnOFF.setText(getResources().getString(R.string.txt_boton_apagar));

        numHoras.setSubText(getResources().getString(R.string.etiqueta_hora));
        numMinutos.setSubText(getResources().getString(R.string.etiqueta_minutos));
        numSegundos.setSubText(getResources().getString(R.string.etiqueta_segundos));

        btnON.setEnabled(true);
        numHoras.setMax(23);
        numMinutos.setMax(59);
        numSegundos.setMax(59);


        btnON.setOnClickListener(this);
        btnOFF.setVisibility(View.GONE);

        btnRestart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMotorola(false) && mCamera != null) {
                    offCamera();
                    countDownTimer.cancel();
                }
                btnOFF.setVisibility(View.GONE);
                btnON.setVisibility(View.VISIBLE);
                numHoras.setValue(0);
                numMinutos.setValue(0);
                numSegundos.setValue(0);
            }
        });

        btnOFF.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOFF.setVisibility(View.GONE);
                btnON.setVisibility(View.VISIBLE);
                if (!isMotorola(false) && mCamera != null) {
                    offCamera();
                    countDownTimer.cancel();
                }
            }
        });

    }


    public Integer obtenerMinutos(int horas, int min, int seg) {
        return ((min + (horas * 60)) * 60) + seg;
    }

    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        startTime = 1000 * obtenerMinutos(numHoras.getValue(),
                numMinutos.getValue(), numSegundos.getValue());
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        prender();
        btnOFF.setVisibility(View.VISIBLE);
        btnON.setVisibility(View.INVISIBLE);

    }

    public void prender() {

        if (!isMotorola(true)) {
            if (mCamera == null) {
                mCamera = Camera.open();
            }
            if (mCamera != null) {

                final Parameters params = mCamera.getParameters();

                List<String> flashModes = params.getSupportedFlashModes();

                if (flashModes != null) {
                    if (count == 0) {
                        params.setFlashMode(Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(params);
                        mCamera.startPreview();
                    }

                    String flashMode = params.getFlashMode();

                    if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {

                        if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
                            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                            mCamera.setParameters(params);
                        } else {
                            params.setFlashMode(Parameters.FLASH_MODE_ON);
                            mCamera.setParameters(params);
                            try {

                                mCamera.autoFocus(new AutoFocusCallback() {
                                    public void onAutoFocus(boolean success,
                                                            Camera camera) {
                                        count = 1;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }

    public boolean isMotorola(boolean apagar) {
        boolean isMoto = false;
        if (manuName.contains(motorola)) {
            DroidLED led;
            try {
                led = new DroidLED();
                led.enable(apagar);
                isMoto = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isMoto;

    }

    public void offCamera() {
        final Parameters params = mCamera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        mCamera.release();
        mCamera = null;
        count = 0;

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            if (!isMotorola(false)) {
                if (mCamera != null) {
                    offCamera();
                }
            }
            numSegundos.setValue(0);
            btnOFF.setVisibility(View.INVISIBLE);
            btnON.setVisibility(View.VISIBLE);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            long misSeg = millisUntilFinished / 1000;
            Integer hh = (int) ((misSeg) / 3600);
            Integer mm = (int) ((misSeg - (3600 * hh)) / 60);
            Integer ss = (int) (misSeg - ((hh * 3600) + (mm * 60)));
            numHoras.setValue(hh);
            numMinutos.setValue(mm);
            numSegundos.setValue(ss);

        }

        public String agregarCero(Integer numero) {
            return (numero < 10) ? "0" + numero : String.valueOf(numero);
        }
    }

}
