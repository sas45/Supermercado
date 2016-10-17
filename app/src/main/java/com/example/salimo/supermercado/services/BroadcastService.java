package com.example.salimo.supermercado.services;


import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.example.salimo.supermercado.controller.Parametros;
import com.example.salimo.supermercado.controller.StatusSerial;
import com.example.salimo.supermercado.socket.IOAcknowledge;
import com.example.salimo.supermercado.socket.IOCallback;
import com.example.salimo.supermercado.socket.SocketIO;
import com.example.salimo.supermercado.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class BroadcastService extends Service {


    public static SocketIO socket;
    public static String PACKAGE_NAME;
    public static boolean isServiceRunning = false;
    public static boolean isSocketConnected = false;
    public String nome, lastMessage, matriculaDb;
    public int numMessages = 0, numNotificacoes = 0;
    public String mainActivityClass, chatHistoricoClass, activityPrincipalClass;
    public String mensagemControllerClass;
    public String tipo, android_id;
    public boolean isConnectedTo = false;
    private String permissaoStatus;

    @Override
    public void onCreate() {
        super.onCreate();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            public void run() {

                try {
                    socket = new SocketIO(Parametros.SERVER_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                socket.connect(new IOCallback() {
                    @Override
                    public void onDisconnect() {
                        isSocketConnected = false;


                    }

                    @Override
                    public void onConnect() {
                        isSocketConnected = true;

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("connected");
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    public void onMessage(String data, IOAcknowledge ack) {

                    }

                    @Override
                    public void onMessage(JSONObject json, IOAcknowledge ack) {

                    }

                    @Override
                    public void on(String event, IOAcknowledge ack, Object... args) {


                        if (event.equals("registo")) {
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("registo");
                            broadcastIntent.putExtra("estado", args[1].toString());
                            sendBroadcast(broadcastIntent);

                        }
                        if (event.equals("statusList")) {


                            String nomeProd;
                            String validade;
                            String preco;
                            String imagem;
                            String codigoBarras;

                            ArrayList<StatusSerial> status = new ArrayList<>();
                            JSONArray jsonArray = (JSONArray) args[1];


                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    nomeProd = jsonArray.getJSONObject(i).getString("nomeProd");
                                    validade = jsonArray.getJSONObject(i).getString("validade");
                                    preco = jsonArray.getJSONObject(i).getString("preco");
                                    imagem = jsonArray.getJSONObject(i).getString("imagem");
                                    codigoBarras = jsonArray.getJSONObject(i).getString("codigoBarras");

                                    status.add(new StatusSerial(nomeProd, validade, preco, imagem, codigoBarras));


                                } catch (Exception e) {
                                    System.out.println(e.toString());


                                }
                            }

                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("statusAction");

                            broadcastIntent.putExtra("statuslist", status);
                            sendBroadcast(broadcastIntent);

                        }
                    }

                    @Override
                    public void onError(SocketIOException socketIOException) {

                    }
                });


            }
        }).start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        isServiceRunning = true;
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        socket.disconnect();
    }


}