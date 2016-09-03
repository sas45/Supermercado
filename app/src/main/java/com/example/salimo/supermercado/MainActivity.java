package com.example.salimo.supermercado;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salimo.supermercado.adapter.CustomItemAnimator;
import com.example.salimo.supermercado.adapter.StatusAdapter;
import com.example.salimo.supermercado.controller.StatusSerial;
import com.example.salimo.supermercado.services.BroadcastService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public double total = 0;
    private ProgressBar pb;
    private List<Product> productsList;
    private RecyclerView mReciclerView;
    private PopupWindow popupWindow;
    private RelativeLayout popupWindowLayout;

    private DrawerLayout mDrawerLayout;
    private TextView mTxvMenuItem;
    private IntentFilter mIntentFilter;
    private ProductAdapter productAdapter;
    StatusAdapter statusAdapter;
    ProgressDialog progress;
    Button pagar;
    FloatingActionButton fab;
    TextView totaltxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.carrinho_title));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        productAdapter = new ProductAdapter();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("statusAction");
        mIntentFilter.addAction("connected");
        mIntentFilter.addAction("registo");

        configView();
        conectToServer();
        iniatilizeFloatingButton();

        pagar = (Button) findViewById(R.id.button);
        pagar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        totaltxt = (TextView) findViewById(R.id.total_tv);

    }

    public void conectToServer() {
        Intent service = new Intent(getApplicationContext(), BroadcastService.class);
        startService(service);

    }

    private void configView() {
        mReciclerView = (RecyclerView) findViewById(R.id.product_recycler_view);
//        mReciclerView.setHasFixedSize(true);
//        mReciclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mReciclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        productsList = new ArrayList<>();
        statusAdapter = new StatusAdapter(productsList);
        mReciclerView.setItemAnimator(new CustomItemAnimator());
        mReciclerView.setAdapter(statusAdapter);


    }

    void updateTotal(double value) {
        total = total + value;
        totaltxt.setText(total + "MT");
    }

    private void iniatilizeFloatingButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanProduct();
            }
        });
    }

    private void scanProduct() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getString(R.string.scanner_text));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, R.string.scan_cancelado, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    //startPopUpWindow(result);
                    BroadcastService.socket.emit("getStatus", result.getContents());
                    progress = ProgressDialog.show(this, "Carregando",
                            "A buscar", true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startPopUpWindow(IntentResult result) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_scanner_result, null);
        popupWindow = new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageButton closePopUp = (ImageButton) findViewById(R.id.close_popup);
        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindowLayout = (RelativeLayout) findViewById(R.id.popup_window);

        popupWindow.showAtLocation(popupWindowLayout, Gravity.CENTER, 0, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateDisply() {

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent();

        int id = item.getItemId();
        switch (id) {
            case R.id.conta:
                hideDrawer();
                break;
            case R.id.definicoes:
                hideDrawer();
                break;
            case R.id.feedback:
                hideDrawer();
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("connected")) {
                pagar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                fab.setEnabled(true);
            }
            if (intent.getAction().equals("registo")) {
                String mes = intent.getStringExtra("estado");

                if (mes.equals("sucesso")) {
                    Toast.makeText(MainActivity.this, "registado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "ocorreu um erro", Toast.LENGTH_SHORT).show();

                }
            }
            if (intent.getAction().equals("statusAction")) {
                ArrayList<StatusSerial> listT = (ArrayList<StatusSerial>) intent.getSerializableExtra("statuslist");
                String nomeProd;
                String validade;
                String preco;
                String imagem;
                String codigoBarras;


                for (int i = 0; i < listT.size(); i++) {


                    nomeProd = listT.get(i).getNomeProd();
                    validade = listT.get(i).getValidade();
                    preco = listT.get(i).getPreco();
                    imagem = listT.get(i).getImagem();
                    codigoBarras = listT.get(i).getCodigoBarras();
                    double pr = Double.parseDouble(preco);
                    productsList.add(new Product(nomeProd, validade, pr, imagem, codigoBarras));
                    statusAdapter.notifyItemInserted(productsList.size());
                    progress.dismiss();
                    updateTotal(pr);


                }


            }


        }
    };


}
