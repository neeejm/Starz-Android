package me.neeejm.starz.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.neeejm.starz.R;
import me.neeejm.starz.beans.Star;
import me.neeejm.starz.services.StarService;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.CarViewHolder> {
    private final List<Star> stars;
    private Context context;
    private final LayoutInflater inflater;
    private static StarAdapter instance;
    private int id;

    public StarAdapter(Context context, List<Star> stars) {
        this.stars = stars;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public synchronized static StarAdapter getInstance(Context context, List<Star> stars) {
        if(instance == null)
            instance = new StarAdapter(context, stars);
        return instance;
    }

    public void deleteStar(Star o) {
        this.stars.remove(o);
        this.notifyDataSetChanged();
    }

   public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View starsView = inflater.inflate(R.layout.item_star, parent, false);
        return new CarViewHolder(starsView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Star star = stars.get(position);
        this.id = star.getId();
        holder.id.setText(this.id + "");
        holder.fullname.setText(star.getPrenom() + " " + star.getNom().toUpperCase());
        holder.city.setText(star.getVille());
        holder.gender.setText(star.isGender() ? "Homme" : "Femme");
        holder.genderIcon.setImageResource(star.isGender() ? R.drawable.male : R.drawable.female);
        holder.cityIcon.setImageResource(R.drawable.location);
        if (star.getImageURL() != null) {
            Glide
                    .with(context)
                    .load(star.getImageURL())
                    .into(holder.starImg);
        } else {
            holder.starImg.setImageResource(R.drawable.star_face);
        }
    }

    @Override
    public int getItemCount() {
        return stars.size();
    }

    public int getId() {
        return this.id;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, city, gender, id;
        ImageView starImg, genderIcon, cityIcon;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.item_id);
            fullname = itemView.findViewById(R.id.label_fullname);
            city = itemView.findViewById(R.id.label_city);
            gender = itemView.findViewById(R.id.lable_gender);
            starImg = itemView.findViewById(R.id.img_star);
            genderIcon = itemView.findViewById(R.id.gender_icon);
            cityIcon = itemView.findViewById(R.id.img_city);
        }
    }
}
