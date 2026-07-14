package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData = findViewById(R.id.get_data);

        getData.setOnClickListener(view -> fetchProducts());
    }

    private void fetchProducts() {

        Methods methods = RetrofitClient.getRetrofit().create(Methods.class);

        Call<Model> call = methods.getAllData();

        call.enqueue(new Callback<Model>() {

            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Log.d(TAG, "Response code: " + response.code());

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Request failed: " + response.code());
                    return;
                }

                Model responseBody = response.body();

                if (responseBody == null) {
                    Log.e(TAG, "Response body is null");
                    return;
                }

                ArrayList<Model.Product> products =
                        responseBody.getProducts();

                if (products == null || products.isEmpty()) {
                    Log.e(TAG, "Products list is empty");
                    return;
                }

                for (Model.Product product : products) {
                    Log.d(TAG, "Product ID: " + product.getId());
                    Log.d(TAG, "Title: " + product.getTitle());
                    Log.d(TAG, "Price: " + product.getPrice());
                    Log.d(TAG, "Category: " + product.getCategory());
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable throwable) {
                Log.e(TAG, "API call failed", throwable);
            }
        });
    }
}