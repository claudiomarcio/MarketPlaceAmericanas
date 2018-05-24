package com.marketplace.claudiomarcio.marketplaceamericanas;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Departments;
import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Products;
import com.marketplace.claudiomarcio.marketplaceamericanas.Services.GetDepartment;
import com.marketplace.claudiomarcio.marketplaceamericanas.Services.GetProducts;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.Aplicacao;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.DepartmentsAdapter;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.ProductsAdapter;
import com.marketplace.claudiomarcio.marketplaceamericanas.Utils.TextViewOnClick;

import java.util.ArrayList;

public class DepartmentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static DepartmentActivity instance;
    public DepartmentsAdapter adapter;
    public ListView lvDepartments;
    public SwipeRefreshLayout swipeLayout;
    public ArrayList<Departments> listDepartment;
    public boolean isFiltered = false;
    public int positionFiltered = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        instance = this;

        setTitle( "Departamentos" );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        lvDepartments = findViewById(R.id.lvDepartment);
        lvDepartments.setOnItemClickListener(this);

        adapter = new DepartmentsAdapter(this, new ArrayList<Departments>());

        hideSoftKeyboard();

        Aplicacao.AutoChangeImageLogo(this, navigationView);

        new GetDepartment(this).execute(getResources().getString(R.string.URL_GET_DEPARTAMENTS));
    }

    public static DepartmentActivity getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {

        instance = null;
        super.onDestroy();

    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onRefresh() {

        isFiltered = false;
        positionFiltered = -1;
        new GetDepartment(this).execute(getResources().getString(R.string.URL_GET_DEPARTAMENTS));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

          if(listDepartment != null && listDepartment.size() > 0 && !isFiltered )
          {
              positionFiltered = position;
              isFiltered = true;
              Departments model = listDepartment.get(position);
              adapter = new DepartmentsAdapter(instance, model.listCategory);
              lvDepartments.setAdapter(adapter);
          }else if(listDepartment != null && listDepartment.size() > 0  && isFiltered && positionFiltered > -1)
          {
              Departments model = listDepartment.get(positionFiltered);
              Intent it = new Intent(this,ProductsActivity.class);
              it.putExtra("category", model.getListCategory().get(position).category);
              startActivity(it);
          }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                isFiltered = false;
                positionFiltered = -1;
                adapter = new DepartmentsAdapter(instance, listDepartment);
                lvDepartments.setAdapter(adapter);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Departamento) {

            DepartmentActivity da = DepartmentActivity.getInstance();
            if(da == null) {
                Intent it = new Intent(DepartmentActivity.this, DepartmentActivity.class);
                startActivity(it);
                finish();
            }

        }else if (id == R.id.nav_Site) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.americanas.com.br/oferta-do-dia"));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
