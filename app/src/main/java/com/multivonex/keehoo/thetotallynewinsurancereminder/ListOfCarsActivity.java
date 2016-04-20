package com.multivonex.keehoo.thetotallynewinsurancereminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.android.keehoo.thetotallynewinsurancereminder.R;

import java.util.ArrayList;
import java.util.List;

public class ListOfCarsActivity extends AppCompatActivity {

    private RecyclerView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_cars);


        lista = (RecyclerView)findViewById(R.id.lista);


        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Ford Focus","GD481HJ", 1000, 1000));
        cars.add(new Car("Kia Picanto", "GD...?", 1000, 1000));


        CarAdapter adapter = new CarAdapter(this, cars);  // this to context;

        adapter.setClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Car object) {
                Toast.makeText(ListOfCarsActivity.this, object.getMarkaSamochodu(), Toast.LENGTH_SHORT).show();
            }
        });


        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
