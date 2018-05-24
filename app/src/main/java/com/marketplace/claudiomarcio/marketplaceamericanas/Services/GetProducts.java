package com.marketplace.claudiomarcio.marketplaceamericanas.Services;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.marketplace.claudiomarcio.marketplaceamericanas.ProductsActivity;
import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Products;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.ProductsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetProducts  extends AsyncTask<String,String, ArrayList<Products>> {

    private Activity act;
    private ArrayList<Products> arrayListCategory;


    public GetProducts(Activity activity)
    {
        this.act =  activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<Products> doInBackground(String... urls) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try
        {


            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
//            String userCredentials = "amerinacas:hm290118";
//            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(),0));
//            connection.setRequestProperty ("Authorization", basicAuth.trim());
            connection.setRequestMethod("GET");

//            List<Pair<String, String>> params = new ArrayList<>();
//            params.add(new Pair<>("productIds", "1"));
//
//            OutputStream os = connection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getQuery(params));
//            writer.flush();
//            writer.close();
//            os.close();

            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONArray arrayProducts = new JSONArray(finalJson);

            arrayListCategory = new ArrayList<>();

            for (int i = 0; i < arrayProducts.length();i++ )
            {
                JSONObject positionItems = new JSONObject(arrayProducts.get(i).toString());
                Products model = new Products();
                model.prodId = positionItems.getInt("prodId");
                model.stock = positionItems.getBoolean("stock");
                model.productSku = positionItems.getInt("productSku");
                model.price = positionItems.getString("price");
                model.image = positionItems.getString("image");
                try {model.installment = positionItems.getString("installment");}catch (Exception ex){}
                try {model.rating = positionItems.getDouble("rating");}catch (Exception ex){}
                model.name = positionItems.getString("name");

                arrayListCategory.add(model);
            }

            return  arrayListCategory;

        } catch (MalformedURLException e) {

        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<Products> result){
        super.onPostExecute(result);
        final ProductsActivity instance = ProductsActivity.getInstance();

        instance.loadingBar.setVisibility(View.GONE);

        if(result != null && result.size() > 0)
        {
            instance.adapter = new ProductsAdapter(act, result);
            instance.lvProducts.setAdapter(instance.adapter);
        }else {
            instance.adapter = new ProductsAdapter(act, new ArrayList<Products>());
            instance.lvProducts.setAdapter(instance.adapter);
        }

        instance.swipeLayout.setRefreshing(false);
    }

//    private String getQuery(List<Pair<String,String>> params) throws UnsupportedEncodingException
//    {
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        for (Pair<String,String> pair : params)
//        {
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(pair.first , "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(pair.second, "UTF-8"));
//        }
//
//        return result.toString();
//    }
}
