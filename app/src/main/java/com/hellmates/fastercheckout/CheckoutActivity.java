package com.hellmates.fastercheckout;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hellmates.fastercheckout.Product.Info;
import com.hellmates.fastercheckout.ProductRecyclerView.FinalListAdapter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    String userName;
    String phoneNumber;
    TextView userNameTV;
    TextView totalTV;
    ImageView barcodeIV;
    TextView orderIdTV;
    RecyclerView itemsRV;
    FinalListAdapter finalListAdapter;

    Bitmap barcodeImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Info> itemList = bundle.getParcelableArrayList("ItemList");

        String total = bundle.getString("Total");
        userName = bundle.getString("UserName");
        phoneNumber = bundle.getString("PhoneNumber");
        userNameTV = findViewById(R.id.textView1);
        userNameTV.setText("Thank You "+userName+" For Shopping With Us,");

        totalTV = findViewById(R.id.total_amount);
        totalTV.setText("$ "+total);

        itemsRV = findViewById(R.id.final_item_list);
        finalListAdapter = new FinalListAdapter(this);
        itemsRV.setLayoutManager(new LinearLayoutManager(this));;
        itemsRV.setAdapter(finalListAdapter);
        for(Info i : itemList){
            finalListAdapter.addItemToFinalList(i);
        }

        //setting orderID and barcode
        orderIdTV = findViewById(R.id.orderId);
        orderIdTV.setText("1234567890");
        try {
            barcodeImage = encodeAsBitmap("1234567890",BarcodeFormat.CODE_128,600,300);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        barcodeIV = findViewById(R.id.barcode_imageview);
        barcodeIV.setImageBitmap(barcodeImage);

    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
