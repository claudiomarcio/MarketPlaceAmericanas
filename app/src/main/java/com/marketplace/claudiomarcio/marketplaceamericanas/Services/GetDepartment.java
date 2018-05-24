package com.marketplace.claudiomarcio.marketplaceamericanas.Services;

import android.app.Activity;
import android.os.AsyncTask;

import com.marketplace.claudiomarcio.marketplaceamericanas.DepartmentActivity;
import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Departments;
import com.marketplace.claudiomarcio.marketplaceamericanas.ProductsActivity;
import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Products;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.DepartmentsAdapter;
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

/**
 * Created by Claudio Marcio on 23/05/2018.
 */

public class GetDepartment extends AsyncTask<String,String, ArrayList<Departments>> {

    private Activity act;
    private ArrayList<Departments> arrayListDepartments;


    public GetDepartment(Activity activity)
    {
        this.act =  activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<Departments> doInBackground(String... urls) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try
        {

            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject menu = new JSONObject(finalJson);
            JSONObject itemMenu = new JSONObject(menu.getString("component"));
            JSONArray children = new JSONArray(itemMenu.getString("children"));
            JSONObject departments = new JSONObject(children.get(0).toString());
            JSONArray departmentsChildren = new JSONArray(departments.getString("children"));
            arrayListDepartments = new ArrayList<>();

            for (int a = 0; a < departmentsChildren.length();a++ )
            {
                JSONObject department = new JSONObject(departmentsChildren.get(a).toString());

                Departments model = new Departments();
                model.listCategory = new ArrayList<>();
                model.title = department.getString("title").substring(0,1).toUpperCase() + department.getString("title").substring(1);

                JSONObject category = new JSONObject(departmentsChildren.get(a).toString());
                JSONArray categoryChildren = new JSONArray(category.getString("children"));

                for (int b = 0; b < categoryChildren.length(); b++ ) {

                    JSONObject item = new JSONObject(categoryChildren.get(b).toString());
                    JSONArray itemChildren = new JSONArray(item.getString("children"));

                    for (int d = 0; d < itemChildren.length(); d++ ) {

                        JSONObject subItem = new JSONObject(itemChildren.get(d).toString());

                        Departments modelSubItem =  new Departments();

                        String title = subItem.getString("title").substring(0,1).toUpperCase() + subItem.getString("title").substring(1);
                        String link = subItem.getString("link");
                        String categoria = "0";

                        if(link.contains("categoria"))
                           categoria = link.replace("https://","").split("/")[2];

                        modelSubItem.title = title;
                        modelSubItem.link = link;
                        modelSubItem.category = categoria;
                        model.listCategory.add(modelSubItem);
                    }
                }

                arrayListDepartments.add(model);

            }

            return  arrayListDepartments;

        } catch (MalformedURLException e) {

        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<Departments> result){
        super.onPostExecute(result);
        final DepartmentActivity instance = DepartmentActivity.getInstance();

        if(result != null && result.size() > 0)
        {
            instance.listDepartment = result;
            instance.adapter = new DepartmentsAdapter(act, result);
            instance.lvDepartments.setAdapter(instance.adapter);
        }else {
            instance.adapter = new DepartmentsAdapter(act, new ArrayList<Departments>());
            instance.lvDepartments.setAdapter(instance.adapter);
            instance.listDepartment = new ArrayList<>();
        }

        instance.swipeLayout.setRefreshing(false);
    }

}