package com.jschdev.miLampara;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jschdev.miLampara.utils.ColorPicker;
import com.jschdev.miLampara.R;

public class ColorsActivity extends Activity {

    Button btnFondo, btnCirculo, btn_okColor, btnTexto, btnLineCirculo, btnON,
            btnOFF, btnGuardar;
    Context mContext;
    ActionBar actionBar;
    TextView codHex;
    String colorHex;
    LinearLayout linearPrincipal;
    int defaultColorR, defaultColorG, defaultColorB;
    ColorPicker cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        initComponents();
        mContext = getApplicationContext();

    }

    public void guardar(View view) {
        Toast.makeText(mContext, "Guardar", Toast.LENGTH_SHORT).show();
        btnGuardar.setBackgroundColor(getResources().getColor(R.color.blue));

    }

    public void changeColor(View view) {
        cp = new ColorPicker(ColorsActivity.this, defaultColorR, defaultColorG,
                defaultColorB);
        cp.show();
        Button btn_okColor = (Button) cp.findViewById(R.id.okColorButton);

        switch (view.getId()) {

            case R.id.color_texto:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(), btnTexto);
                        cp.dismiss();
                    }
                });
                break;

            case R.id.color_fondo:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(), btnFondo);
                        cp.dismiss();
                    }
                });
                break;

            case R.id.color_on:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(), btnON);
                        cp.dismiss();
                    }
                });
                break;

            case R.id.color_off:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(), btnOFF);
                        cp.dismiss();
                    }
                });
                break;

            case R.id.color_circle:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(),
                                btnCirculo);
                        cp.dismiss();
                    }
                });
                break;

            case R.id.color_lineCircle:
                btn_okColor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColor(cp.getRed(), cp.getGreen(), cp.getBlue(),
                                btnLineCirculo);
                        cp.dismiss();
                    }
                });
                break;
        }
    }

    public void setColor(int red, int green, int blue, Button boton) {

        GradientDrawable drawable = (GradientDrawable) boton.getBackground();

        drawable.setColor(Color.rgb(red, green, blue));

    }

    private void initComponents() {
        btnGuardar = (Button) findViewById(R.id.btn_guardar);
        btnTexto = (Button) findViewById(R.id.color_texto);
        btnFondo = (Button) findViewById(R.id.color_fondo);
        btnON = (Button) findViewById(R.id.color_on);
        btnON = (Button) findViewById(R.id.color_off);
        btnCirculo = (Button) findViewById(R.id.color_circle);
        btnLineCirculo = (Button) findViewById(R.id.color_lineCircle);

        linearPrincipal = (LinearLayout) findViewById(R.id.principal);
        defaultColorR = 0;
        defaultColorG = 0;
        defaultColorB = 0;

    }
}

