package com.marketplace.claudiomarcio.marketplaceamericanas;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Products;
import com.marketplace.claudiomarcio.marketplaceamericanas.Services.GetProducts;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.Aplicacao;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.ProductsAdapter;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class ProductsActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener{

    public static ProductsActivity instance;
    public ImageView picture;
    public TextView name;
    public TextView price;
    public RatingBar ratingBar;
    public ListView lvProducts;
    private SearchView search_view;
    public ProductsAdapter adapter;
    public GifImageView loadingBar;
    public SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        instance = this;

        setTitle( "Produtos" );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadingBar = findViewById(R.id.loading);
        loadingBar.setVisibility(View.VISIBLE);

        search_view = findViewById(R.id.search_view);
        search_view.setOnQueryTextListener(this);
        search_view.clearFocus();

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        lvProducts = findViewById(R.id.lvProducts);
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        ratingBar = findViewById(R.id.ratingBar);

        adapter = new ProductsAdapter(this, new ArrayList<Products>());

        hideSoftKeyboard();

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.getString("category").equals("0")){

            new GetProducts(this).execute(getResources().getString(R.string.URL_GET_PRODUCTS_BY_CATEGORY) + "categoryId=" + extras.getString("category"));
        }else
        {
            new GetProducts(this).execute(getResources().getString(R.string.URL_GET_PRODUCTS_BY_CATEGORY) + "categoryId=" + 0);
        }

        Aplicacao.AutoChangeImageLogo(this, navigationView);

    }

    public static ProductsActivity getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {

        instance = null;
        super.onDestroy();

    }

    @Override
    public void onRefresh() {

        loadingBar.setVisibility(View.VISIBLE);
        search_view.setQuery("", false);
        search_view.clearFocus();

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.getString("category").equals("0")){

            new GetProducts(this).execute(getResources().getString(R.string.URL_GET_PRODUCTS_BY_CATEGORY) + "categoryId=" + extras.getString("category"));
        }else
        {
            new GetProducts(this).execute(getResources().getString(R.string.URL_GET_PRODUCTS_BY_CATEGORY) + "categoryId=" + 0);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.getFilter().filter(newText);

        return false;
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Departamento) {
            finish();
        }else if (id == R.id.nav_Site) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.americanas.com.br/oferta-do-dia"));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
