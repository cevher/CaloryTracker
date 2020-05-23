package com.example.calcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.Food;
import util.Utils;

public class DisplayFoodsActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalCals, totalFood;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);

        listView = findViewById(R.id.list);
        totalCals = findViewById(R.id.totalAmountTextView);
        totalFood = findViewById(R.id.totalItemsTextView);

        refreshData();




    }

    private void refreshData() {
        dbFoods.clear();

        dba = new DatabaseHandler(DisplayFoodsActivity.this);
        ArrayList<Food> foodsFromDB = dba.getFoods();
        int calsValue = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedCalValue = Utils.formatNumber(calsValue);
        String formattedItems = Utils.formatNumber(totalItems);


        totalCals.setText("Total Calories: " + formattedCalValue);
        totalFood.setText("Total Foods: " + formattedItems);

        for(int i =0; i< foodsFromDB.size(); i++){
            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("FOOD_IDS: ", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setCalories(cals);
            myFood.setRecordDate(dateText);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);
        }

        dba.close();

        ////// SET UP ADAPTER /////////
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row,dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();


    }


}
