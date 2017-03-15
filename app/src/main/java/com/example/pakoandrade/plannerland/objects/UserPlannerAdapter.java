package com.example.pakoandrade.plannerland.objects;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakoandrade.plannerland.R;
import com.example.pakoandrade.plannerland.main.UserProfileActivity;

import java.util.List;


public class UserPlannerAdapter extends RecyclerView.Adapter<UserPlannerAdapter.AnimeViewHolder> {
    private List<UserPlanner> items;
    private Context context;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView habilidades;
        public CardView cvUser;

        public AnimeViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            habilidades = (TextView) v.findViewById(R.id.habilidades);
            cvUser = (CardView) v.findViewById(R.id.cvUser);
        }
    }

    public UserPlannerAdapter(Context context,List<UserPlanner> items) {
        notifyDataSetChanged();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder,final int i) {
//        notifyDataSetChanged();
        viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.habilidades.setText(items.get(i).getHabilidades());



        viewHolder.cvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ap = items.get(i).getNombre();
                String h = items.get(i).getHabilidades();
                String lat = items.get(i).getLat();
                String lon = items.get(i).getLon();
                String userM = items.get(i).getUserMember();

                Intent i = new Intent(context,UserProfileActivity.class);
                i.putExtra("member",userM);
                i.putExtra("habilidades",h);
                i.putExtra("nombre",ap);
                i.putExtra("latitud",lat);
                i.putExtra("longitud",lon);
                context.startActivity(i);
            }
        });

    }

    /*public void notifyData(List<UserPlanner> myList) {
        this.mAdapter.setDatas(myList);
        notifyDataSetChanged();
        this.mAdapter.notifyDataSetChanged();
    }*/
}
