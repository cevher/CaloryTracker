package com.example.calcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class FoodItemDetailsActivity extends AppCompatActivity {
    private TextView foodName, calories, dateTaken;
    private Button shareButton, deleteButton;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);

        foodName = findViewById(R.id.detsFoodName);
        calories = findViewById(R.id.detsCalories);
        dateTaken = findViewById(R.id.detsDateText);
        shareButton = findViewById(R.id.detsShareButton);
        deleteButton = findViewById(R.id.deleteButton);
        Food food = (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());
        foodId = food.getFoodId();
        calories.setTextSize(34.5f);
        calories.setTextColor(Color.RED);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelected(foodId);
            }
        });

    }

    private void deleteSelected(final int foodId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
        alert.setTitle("Delete?");
        alert.setMessage("Are you sure you want to delete the item?");
        alert.setNegativeButton("No", null);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler dba = new DatabaseHandler(FoodItemDetailsActivity.this);
                dba.deleteFood(foodId);
                Toast.makeText(getApplicationContext(), "Food Item Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));


                /////////////////////////// REMOVE THIS ACTIVITY FROM THE ACTIVITY STACT //////////////////////
                FoodItemDetailsActivity.this.finish();



            }
        });
        alert.show();

    }

    private void shareCals() {
        StringBuilder dataString = new StringBuilder();
        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(" Eaten on: " + date);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "My Caloric Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"pazocak@gmail.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());
        
        try{
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "Please Install Email Client before Sending", Toast.LENGTH_SHORT).show();
        }

    }


}
