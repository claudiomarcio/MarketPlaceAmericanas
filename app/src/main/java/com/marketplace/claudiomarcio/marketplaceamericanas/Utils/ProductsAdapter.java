package com.marketplace.claudiomarcio.marketplaceamericanas.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.marketplace.claudiomarcio.marketplaceamericanas.Models.Products;
import com.marketplace.claudiomarcio.marketplaceamericanas.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import br.com.datumti.library.utils.ImageLoader;

public class ProductsAdapter extends BaseAdapter implements Filterable {

    ArrayList<Products> list;
    ArrayList<Products> listFilter;
    ValueFilter valueFilter;
    private final Activity c;

    public ProductsAdapter(Activity activity, ArrayList<Products> products) {

        if(products.size() > 0) {

            this.listFilter = products;
            this.list = products;

        }else
        {
            list = new ArrayList<>();
            Products _model = new Products();
            _model.setProdId(-1);
            this.list.add(_model);
        }
        this.c = activity;
    }

    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ?list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = (LayoutInflater)c .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.product_adapter, null);
        }

        if(list.size() > 0 && list.get(0).getProdId() != -1) {
            TextView name = convertView.findViewById(R.id.name);
            TextView price = convertView.findViewById(R.id.tvPrice);
            TextView link = convertView.findViewById(R.id.price);
            RatingBar rating = convertView.findViewById(R.id.ratingBar);
            ImageView picture = convertView.findViewById(R.id.picture);

            ImageLoader imgLoader = new ImageLoader(c);
            imgLoader.DisplayImage(list.get(position).getImage(), c, picture);
            name.setText(list.get(position).name);
            price.setText(list.get(position).price);
            rating.setRating((float) list.get(position).rating);
            link.setText(list.get(position).getInstallment());
        }else
        {
            convertView = inflator.inflate(R.layout.not_found, null);
            if(Aplicacao.CheckInternet(c))
            {
                ImageView img = convertView.findViewById(R.id.notConnection);
                img.setVisibility(View.GONE);

                TextView notFound = convertView.findViewById(R.id.notFound);
                notFound.setText("Não foi possível localizar produtos desta categoria em nosso estoque !");

                Animation animation = null;
                animation = AnimationUtils.loadAnimation(c, R.anim.top_to_down);
                animation.setDuration(1000);
                notFound.startAnimation(animation);
            }else
            {
                ImageView img = convertView.findViewById(R.id.notConnection);
                img.setVisibility(View.VISIBLE);

                TextView notFound = convertView.findViewById(R.id.notFound);
                notFound.setText("Habilite a internet e tente novamente !");

            }

        }

        return convertView;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Products> filterList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if ((list.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        filterList.add(list.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listFilter.size();
                results.values = listFilter;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list = (ArrayList<Products>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
