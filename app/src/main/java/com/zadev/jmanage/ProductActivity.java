package com.zadev.jmanage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ProductListAdapter;
import Client.Client;
import Model.Product;
import SharedPref.UserSession;

public class ProductActivity extends AppCompatActivity {

    //Class
    UserSession userSession;
    Client client;
    //

    //Widget
    Toolbar product_toolbar;
    RecyclerView prod_recyclerview;

    //end of Widget
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Initializing class
        userSession = new UserSession(ProductActivity.this);
        client = new Client();
        //

        init();
        logic();
    }

    void init() {
        product_toolbar = findViewById(R.id.product_toolbar);
        prod_recyclerview = findViewById(R.id.prod_recyclerview);

        setSupportActionBar(product_toolbar);
    }

    void logic() {
        Product product = new Product();
        product.setProdID("10");
        product.setProdName("Buah belimbing");
        product.setProdWeight("10 Ton");
        product.setProdQuantity("100");
        product.setProdComm("50");
        product.setProdLove("50");
        product.setProdDesc("Ini adalah produk terbaik");
        product.setProdPrice("50000");
        product.setProdPict(String.valueOf(R.drawable.bg1));
        product.setPUsername("yolo");
        products.add(product);

        showData();
    }

    ArrayList<Product> products = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    public void loadProduct() {
        new AsyncTask<Void, Void, String>() {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProductActivity.this, "Sedang mengambil produk", "Mengambil produk dari server", false, false);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                try {
                    if (result != null) {
                        if (!result.isEmpty()) {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("fetchProduct");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                Product product = new Product();
                                product.setProdID(obj.getString("ProdID"));
                                product.setProdName(obj.getString("ProdName"));
                                product.setProdDesc(obj.getString("ProdDesc"));
                                product.setProdQuantity(obj.getString("ProdQuantity"));
                                product.setProdPrice(obj.getString("ProdPrice"));
                                product.setProdWeight(obj.getString("ProdWeight"));
                                product.setProdLove(obj.getString("ProdLove"));
                                product.setProdComm(obj.getString("ProdComm"));
                                product.setPUsername(obj.getString("PUsername"));
                                products.add(product);
                            }
                            if (products != null) {
                                if (products.size() != 0) {
                                    showData();
                                } else {
                                    Log.d("products_size", "0");
                                }
                            } else {
                                Log.d("products_define", "Null");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res;
                res = client.GET(client.product + "?Request=fetchProduct");
                return res;
            }
        }.execute();
    }

    void showData() {
        ProductListAdapter adapter = new ProductListAdapter(ProductActivity.this, products);
        prod_recyclerview.setAdapter(adapter);
        prod_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }
}