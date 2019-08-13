package com.example.androidtesttask.network;

import android.content.Context;

import com.example.androidtesttask.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {

    public static String BASE_URL="https://demo.bankplus.ru/";

    private static Retrofit retrofit;


    public static Retrofit getClient(Context context){
        OkHttpClient client = new OkHttpClient();
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inStream = context.getResources().openRawResource(R.raw.pem_certificate);
            Certificate ca;
            ca = cf.generateCertificate(inStream);
            KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());
            kStore.load(null, null);
            kStore.setCertificateEntry("ca", ca);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(kStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();

        } catch (CertificateException
                | KeyStoreException
                | NoSuchAlgorithmException
                | IOException
                | KeyManagementException e) {
            e.printStackTrace();
        }
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
