package com.multivonex.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.keehoo.thetotallynewinsurancereminder.R;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by keehoo on 19.04.2016.
 */
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> dane;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public CarAdapter(Context context, List<Car> dane) {
        this.dane = dane; // podlaczenie danych z argumentów konstruktora do danych (listy samochodów)
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // podłączenie naszego layoutinflatera do serwisu systemowego
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;


    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.car_list_item, parent, false);
        return new CarViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = dane.get(position);  // przypisujemy obiektowi car referencje z listy dane o pozycji position


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView carModel;
        public TextView carRegistrationNumber;
        public TextView dataOfCarInsurance;
        public TextView dateOfCarTechnicalCheck;
        public CountdownView timeTillTheEndOfInsurance;
        public CountdownView timeTillNextTechnicalCheck;

        public int currentPosition;
        public Car currentObject;
        public CarAdapter adapter;
    }

    public CarViewHolder(View itemView, CarAdapter adapter) {

        super(itemView);
        this.adapter = adapter;


    }


    // Intefejs ktory definiuje jak mozna reagowac na klikniecia elementow listy
    public interface OnItemClickListener {
        void onClick(int position, Car object);
    }
}
