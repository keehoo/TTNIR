package com.multivonex.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.keehoo.thetotallynewinsurancereminder.R;

import org.w3c.dom.Text;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> dane;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onClickListener;

    public CarAdapter(Context context, List<Car> dane) {
        this.dane = dane; // podlaczenie danych z argumentów konstruktora do danych (listy samochodów)
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // podłączenie naszego layoutinflatera do serwisu systemowego
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;


    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.car_list_item, parent, false);
        return new CarViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = dane.get(position);  // przypisujemy obiektowi car referencje z listy dane o pozycji position

        // 2. Uzupełnij widok (holder) pobranymi danymi
        holder.carModel.setText(car.getMarkaSamochodu());
        // W metode setText gdy chcemy wcisnac wartosc INT, musimy to robic przez Integer.toString()

        holder.carRegistrationNumber.setText(car.getNumer_Rejestracyjny());
        holder.dataOfCarInsurance.setText("Tutaj nalezy wstawic longa przepisanego na date i na stringa z data ubezpieczenia");
        holder.dateOfCarTechnicalCheck.setText("Tutaj data przegladu");
        holder.timeTillNextTechnicalCheck.start(1000);   // TODO: tutaj wstawic longa z casem do konca obowiazywania ubezpieczenia.
        holder.timeTillTheEndOfInsurance.start(1000);    // TODO: tutaj wstawic longa z czasem do konca przegladu w milisekundach.
        holder.currentPosition = position;
        holder.currentObject = car;

    }

    @Override
    public int getItemCount() {

        return dane.size();

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






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


        public CarViewHolder(View itemView, CarAdapter adapter) {

            super(itemView);
            this.adapter = adapter;
            carModel = (TextView) itemView.findViewById(R.id.carName_id);
            carRegistrationNumber = (TextView) itemView.findViewById(R.id.numerRejestracyjny_id);
            dataOfCarInsurance = (TextView) itemView.findViewById(R.id.insuranceDateInTheItemView);
            dateOfCarTechnicalCheck = (TextView) itemView.findViewById(R.id.technicalDateInTheItemView);
            timeTillTheEndOfInsurance = (CountdownView) itemView.findViewById(R.id.insuranceCountDownNumber);
            timeTillNextTechnicalCheck = (CountdownView) itemView.findViewById(R.id.technicalCountDownNumber);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            // 1. Najpierw sprawdzam czy w adapterze istnieje onClickListener

            if (adapter.onClickListener != null) {
                adapter.onClickListener.onClick(currentPosition, currentObject);
            }
        }
    }


    // Intefejs ktory definiuje jak mozna reagowac na klikniecia elementow listy
    public interface OnItemClickListener {
        void onClick(int position, Car object);
    }
}
