package com.example.rentalz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView furnituretypeCompleteTextView, numberofbedroomsCompleteTextView;
    Button create;
    private EditText propertytype, dateandtime, mothlyprice, nameofthereporter, note;
    String regexLetter = "^[a-zA-Z\\s]*$";
    String regexPrice = "^[1-9][0-9\\.]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textInputLayout = findViewById(R.id.bedRoom);
        numberofbedroomsCompleteTextView = findViewById(R.id.chooseBedRoom);
        String[] items = {"Luxury", "1", "2", "3"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown, items);
        numberofbedroomsCompleteTextView.setAdapter(itemAdapter);


        textInputLayout = findViewById(R.id.furType);
        furnituretypeCompleteTextView = findViewById(R.id.chooseFurType);
        String[] furItems = {"Full furniture", "No furniture ", "Part Furnished"};
        ArrayAdapter<String> itemAdapterFur = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown, furItems);
        furnituretypeCompleteTextView.setAdapter(itemAdapterFur);

        propertytype = (EditText) findViewById(R.id.property);
        dateandtime = (EditText) findViewById(R.id.dateTime);
        dateandtime.setInputType(InputType.TYPE_NULL);
        mothlyprice = (EditText) findViewById(R.id.price);
        nameofthereporter = (EditText) findViewById(R.id.name);
        note = (EditText) findViewById(R.id.note);

        create = (Button) findViewById(R.id.submit_button);
        dateandtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialogDateTime(dateandtime);
                } else {
                    dateandtime.setError(null);
                }
            }
        });
        dateandtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDateTime(dateandtime);
            }
        });
        numberofbedroomsCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    numberofbedroomsCompleteTextView.setError(null);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.submit_button) {
                    if (!validateDateTime() | !validatePrice() | !validateName() | !validatebedRoom() | !validateProper()) {
                        Toast.makeText(MainActivity.this, "Submit Fail!!!", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                        View customeLayout = getLayoutInflater().inflate(R.layout.layout_dialog, null);
                        builder.setView(customeLayout);
                        builder.setTitle("Entered Details");
                        String enteredProper = propertytype.getText().toString();
                        String enteredBedRoom = numberofbedroomsCompleteTextView.getText().toString();
                        String enteredName = nameofthereporter.getText().toString();
                        String enteredPrice = mothlyprice.getText().toString();
                        String enteredDateTime = dateandtime.getText().toString();
                        String enteredNote = note.getText().toString();
                        String noteText = (enteredNote.length() == 0) ? ("None") : enteredNote;
                        String enteredFurType = furnituretypeCompleteTextView.getText().toString();
                        String furTypeText = (enteredFurType.length() == 0) ? ("None") : enteredFurType;
                        TextView resultDetail = customeLayout.findViewById(R.id.rental_detail);
                        String result = "Property Type:\t" + enteredProper + "\nBed Room:\t"
                                + enteredBedRoom + "\nName:\t" + enteredName
                                + "\nMonthly Price:\t" + enteredPrice
                                + "\nDate and Time:\t" + enteredDateTime
                                + "\nFurniture Type:\t" + furTypeText
                                + "\nNote:\t" + noteText;
                        resultDetail.setText(result);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Submit Successfully!!!", Toast.LENGTH_LONG).show();
                                propertytype.getText().clear();
                                numberofbedroomsCompleteTextView.getText().clear();
                                dateandtime.getText().clear();
                                mothlyprice.getText().clear();
                                nameofthereporter.getText().clear();
                                note.getText().clear();
                                furnituretypeCompleteTextView.getText().clear();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Cancelled!!!", Toast.LENGTH_LONG).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
            }
        });
    }

    private void showDialogDateTime(EditText dateTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

  new DatePickerDialog(MainActivity.this,dateSetListener,calendar .get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

}
        private boolean validateProper(){
        String properType = propertytype.getEditableText().toString().trim();
        if(properType.isEmpty()){
            propertytype.setError("Please enter property type");
            return false;
        }
        else if(!properType.matches(regexLetter)){
            propertytype.setError("The property contains only letters");
            return false;
        }
        else if(properType.length() > 30){
            propertytype.setError("The property is not bigger than 30 characters");
            return false;
        }
        else{
            propertytype.setError(null);
            return true;
        }
    }
    private boolean validateName(){
        String nameText = nameofthereporter.getEditableText().toString().trim();
        if(nameText.isEmpty()){
            nameofthereporter.setError("Please enter the name");
            return false;
        }
        else if(!nameText.matches(regexLetter)){
            nameofthereporter.setError("The name contains only letters");
            return false;
        }
        else if(nameText.length()>30){
            nameofthereporter.setError("The name is not bigger than 30 characters");
            return false;
        }
        else{
            nameofthereporter.setError(null);
            return true;
        }
    }
    private boolean validatePrice(){
        String priceText = mothlyprice.getEditableText().toString().trim();
        if(priceText.isEmpty()){
            mothlyprice.setError("Please enter the price");
            return false;
        }
        else if(!priceText.matches(regexPrice)){
            mothlyprice.setError("Please enter a number than 0");
            return false;
        }
        else{
            mothlyprice.setError(null);
            return true;
        }
    }
    private boolean validatebedRoom(){
        String bedRoomText = numberofbedroomsCompleteTextView.getText().toString().trim();
        if(bedRoomText.isEmpty()){
            numberofbedroomsCompleteTextView.setError("Please enter bedroom");
            return false;
        }else{
            numberofbedroomsCompleteTextView.setError(null);
            return true;
        }
    }
    private boolean validateDateTime(){
        String timeText = dateandtime.getEditableText().toString().trim();
        if(timeText.isEmpty()){
            dateandtime.setError("Please enter date and time");
            return false;
        }else{
            dateandtime.setError(null);
            return true;
        }
    }
}