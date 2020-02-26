package com.hellmates.fastercheckout;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hellmates.fastercheckout.PappuPager.PappuPagerAdapter;
import com.hellmates.fastercheckout.PappuPager.SliderUtils;
import com.hellmates.fastercheckout.Product.Info;
import com.hellmates.fastercheckout.ProductRecyclerView.MyCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String userName;
    String phoneNumber;
    ViewPager pappuPager;
    TextView totalView;
    RecyclerView recyclerView;
    MyCustomAdapter adapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    PappuPagerAdapter pappuPagerAdapter;
    RequestQueue requestQueue;
    List<SliderUtils> sliderImgs;
    String img_request_url = "http://inspiroboutique.in/imgs.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle credentials = getIntent().getExtras();
        userName = credentials.getString("UserName");
        phoneNumber = credentials.getString("PhoneNumber");

        requestQueue = Volley.newRequestQueue(this);
        sliderImgs = new ArrayList<>();
        pappuPager = (ViewPager) findViewById(R.id.pappuPager);
        sendRequest();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyCustomAdapter(this);
        totalView = (TextView) findViewById(R.id.total);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void addItemToCart(View view){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan the QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Scan cancelled",Toast.LENGTH_LONG).show();
            }else {
                String resultString = result.getContents();
                if(resultString.contains("ItemName")){
                    try {
                        JSONObject jsonObject = new JSONObject(resultString);
                        String itemName = jsonObject.getString("ItemName");
                        Double itemMRP = Double.parseDouble(jsonObject.getString("ItemMRP"));
                        Double itemDisc = Double.parseDouble(jsonObject.getString("ItemDisc"));

                        adapter.addItem(new Info(itemName,itemMRP,itemDisc));
                        String total = adapter.getTotal();
                        updateTotalView(total);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Some error ocurred please try again error code 1",Toast.LENGTH_LONG).show();
                    }
                }else
                    Toast.makeText(this,"Invalid item or QR code",Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this,"Some error ocurred please try again error code 2",Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateTotalView(String total){
        totalView.setText(total);
    }

    public void sendRequest(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,img_request_url,null,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                for(int i = 0; i < response.length(); i++){
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String imageUrl = jsonObject.getString("img_url");
                        Log.d("FasterCheckout",imageUrl);
                        sliderUtils.setSliderImageUrl(imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderImgs.add(sliderUtils);
                }

                pappuPagerAdapter = new PappuPagerAdapter(sliderImgs,MainActivity.this);
                pappuPager.setAdapter(pappuPagerAdapter);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("FasterCheckout",error.toString());
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void checkout(View view){
        Intent intent = new Intent(MainActivity.this,CheckoutActivity.class);
        ArrayList<Info> itemList = adapter.getItemList();
        intent.putParcelableArrayListExtra("ItemList",itemList);
        intent.putExtra("Total",adapter.getTotal());
        intent.putExtra("UserName",userName);
        intent.putExtra("PhoneNumber",phoneNumber);
        startActivity(intent);
    }
}
