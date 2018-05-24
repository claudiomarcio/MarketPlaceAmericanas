package com.marketplace.claudiomarcio.marketplaceamericanas.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.marketplace.claudiomarcio.marketplaceamericanas.R;

/**
 * Created by Claudio Marcio on 24/05/2018.
 */

public class Aplicacao extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static boolean CheckInternet(Context c) {

        try {
            ConnectivityManager cm =
                    (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting()) {

                return true;

            } else {
                return false;

            }
        }catch (Exception ex)
        {
            return  false;
        }
    }

    public static void AutoChangeImageLogo(Activity c, NavigationView navigationView)
    {
        final int[] imageArray = { R.drawable.logo, R.drawable.logo_loucura,
                R.drawable.logo_namorados };

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_menu);
        final ImageView img = headerView.findViewById(R.id.logoImage);


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                img.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
