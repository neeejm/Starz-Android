package me.neeejm.starz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.neeejm.starz.adapters.StarAdapter;
import me.neeejm.starz.beans.Star;
import me.neeejm.starz.services.StarService;

public class MainActivity extends AppCompatActivity {
    private StarAdapter starsAdapter;
    private StarService starService;
    private ConstraintLayout layout;
    private RecyclerView rvStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starService = StarService.getInstance(this);

        this.rvStars = findViewById(R.id.star_rv);
        rvStars.setLayoutManager(new LinearLayoutManager(this));
        this.layout = findViewById(R.id.main_activity_layout);

        this.starsAdapter = StarAdapter.getInstance(this, starService.findAll());
        this.starsAdapter.setContext(this);
        rvStars.setAdapter(starsAdapter);
        this.setSwipeRight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_add)
            addStar();

        return true;
    }

    public void addStar() {
        startActivity(new Intent(MainActivity.this, StarActivity.class));
        finish();
    }

    public void setSwipeRight() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                starsAdapter.notifyItemRemoved(position);
                starsAdapter.onBindViewHolder((StarAdapter.CarViewHolder) viewHolder, position);
                Star star = starService.findById(starsAdapter.getId());
                starService.delete(star);
                // quick fix (data is stored in the filter too) --> to be refactored (cause: bad approach)
                starsAdapter.deleteStar(star);
                Toast.makeText(MainActivity.this,
                        star.getNom().toUpperCase() + " " +  star.getPrenom() + " " + "Supprimé",
                        Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(this.rvStars);
    }
}
