package com.evaluacion.josue.evaluacion12;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    //Atributos para Ad
    private AdView adView;
    //Atributos para login facebook
    private CallbackManager cM;
    private LoginButton lB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Iniciar componentes para login con Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        cM=CallbackManager.Factory.create();
        getFbKeyHash("ijLE4sjeIu6Vw5jOHOSreKHjToQ=");
        setContentView(R.layout.activity_main);
        lB=(LoginButton)findViewById(R.id.login_facebook);
        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this,"Inicio de Sesion Exitoso",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"Inicio de Sesion Cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"Inicio de Sesion No Exitoso",Toast.LENGTH_SHORT).show();
            }
        });
        //Enlazar componentes para la publicidad
        adView=(AdView)findViewById(R.id.ad_view);
        AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);


    }

    private void getFbKeyHash(String packageName) {
        try{
            PackageInfo info=getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES );
            for (Signature signature:info.signatures){
                MessageDigest md= MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Una vez que inicie sesion recupera la informacion
        cM.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        if (adView!=null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView!=null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView!=null){
            adView.resume();
        }
        super.onResume();
    }
}
