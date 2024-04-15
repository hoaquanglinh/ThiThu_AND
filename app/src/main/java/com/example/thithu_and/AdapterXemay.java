package com.example.thithu_and;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AdapterXemay extends BaseAdapter {
    private ArrayList<XeMay> list;
    private Context context;

    private MainActivity activity;

    public AdapterXemay(ArrayList<XeMay> list, Context context, MainActivity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_xemay, null);

        XeMay xeMay = list.get(i);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        TextView tvTen = rowView.findViewById(R.id.tvten);
        TextView tvMau = rowView.findViewById(R.id.tvmau);
        TextView tvGia = rowView.findViewById(R.id.tvgia);
        TextView tvmota = rowView.findViewById(R.id.tvmota);
        ImageView tvanh = rowView.findViewById(R.id.tvanh);

        tvTen.setText(xeMay.getTen_ph43159());
        tvMau.setText(xeMay.getMau_ph43159());
        tvGia.setText(numberFormat.format(xeMay.getGia_ph43159()));
        tvmota.setText(xeMay.getMota_ph43159());

        Glide.with(context)
                .load(xeMay.getAnh_ph43159())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .into(tvanh);

        rowView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.xoa(xeMay.get_id());
            }
        });

        rowView.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.them(context, 1, xeMay);
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.ttct(xeMay.get_id());
            }
        });

        return rowView;
    }
}
