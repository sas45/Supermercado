package com.example.salimo.supermercado.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salimo.supermercado.model.Product;
import com.example.salimo.supermercado.R;

import java.util.ArrayList;
import java.util.List;


public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Holder> {


    List<Product> lista = new ArrayList<>();

    public StatusAdapter(List<Product> taxiList) {
        lista = taxiList;
    }

    public int getCount() {
        return lista.size();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        Product p = lista.get(i);
        holder.nomeProd.setText(p.getNomeProd());
        holder.preco.setText("" + p.getPreco());
        holder.validade.setText(p.getValidade());
        holder.qty.setText(null);
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.product_item, viewGroup, false);

        return new Holder(itemView);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView nomeProd;
        TextView validade;
        TextView preco, qty;
        ImageView imagem;

        public Holder(View view) {
            super(view);

            nomeProd = (TextView) view.findViewById(R.id.item_title);
            validade = (TextView) view.findViewById(R.id.item_expiry);
            preco = (TextView) view.findViewById(R.id.item_price);
            qty = (TextView) view.findViewById(R.id.item_quant);
            imagem = (ImageView) view.findViewById(R.id.item_imageView);


        }
    }
}
