package com.example.expensetrackauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateExpActivity extends AppCompatActivity {
    private static final String TAG = UpdateExpActivity.class.getSimpleName();
    EditText depart,lunch,returnexp,other, expname1, expname2, expname3, expname4;
    TextView date;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference ref;
    Expense expense;
    Button btnView;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_exp);
        Log.d(TAG, "Inside onCreate of UpdateActivity");

        date = (TextView) findViewById(R.id.date);
        depart = (EditText)findViewById(R.id.depart);
        lunch = (EditText)findViewById(R.id.lunch);
        returnexp = (EditText)findViewById(R.id.returnexp);
        other = (EditText)findViewById(R.id.other);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnView = (Button)findViewById(R.id.btnViewExp);

        expname1 = (EditText)findViewById(R.id.expname1);
        expname2 = (EditText)findViewById(R.id.expname2);
        expname3 = (EditText)findViewById(R.id.expname3);
        expname4 = (EditText)findViewById(R.id.expname4);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Expense");
        expense = new Expense();

        user = mAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        getIncomingIntents();



    }
    private void getIncomingIntents(){
        Log.d("getIncoming ", "getIncomingIntents checking" );
        if(getIntent().hasExtra("expdate") && getIntent().hasExtra("expname1")&& getIntent().hasExtra("expname2")&& getIntent().hasExtra("expname3")&& getIntent().hasExtra("expname4")&& getIntent().hasExtra("departexp")&& getIntent().hasExtra("lunchexp")&& getIntent().hasExtra("returnexp")&& getIntent().hasExtra("otherexp")){ //if intent has any extras
            String expdate = getIntent().getStringExtra("expdate");
            char [] dateArray = expdate.toCharArray();
            String monthname = ""+expdate.charAt(0)+expdate.charAt(1)+expdate.charAt(2);
            String year = ""+expdate.charAt(8)+expdate.charAt(9)+expdate.charAt(10)+expdate.charAt(11);
            String day = ""+expdate.charAt(4)+expdate.charAt(5);

            expdate = convertDateToFirebaseFormat(year,monthname,day); //converting back to firebase date format yyyy-mm-dd

            String expname1 = getIntent().getStringExtra("expname1");
            String expname2 = getIntent().getStringExtra("expname2");
            String expname3 = getIntent().getStringExtra("expname3");
            String expname4 = getIntent().getStringExtra("expname4");
            String departexp = getIntent().getStringExtra("departexp");
            String lunchexp = getIntent().getStringExtra("lunchexp");
            String returnexp = getIntent().getStringExtra("returnexp");
            String otherexp = getIntent().getStringExtra("otherexp");

            setValuesFromIntent(expdate,expname1, expname2, expname3, expname4, departexp,lunchexp,returnexp,otherexp);

        }

    }

    private void setValuesFromIntent(String dt, String exp1, String exp2, String exp3, String exp4, String depExp, String lunExp, String retExp, String othExp){
        Log.d("setValues", "Setting values in EditTexts");

        date.setText(dt);
        depart.setText(depExp);
        lunch.setText(lunExp);
        returnexp.setText(retExp);
        other.setText(othExp);
        expname1.setText(exp1);
        expname2.setText(exp2);
        expname3.setText(exp3);
        expname4.setText(exp4);

    }

    private void getValues(){
        expense.setDate(date.getText().toString());
        int dep = Integer.parseInt(depart.getText().toString());
        expense.setDepart(dep);
        int lun = Integer.parseInt(lunch.getText().toString());
        expense.setLunch(lun);
        int ret = Integer.parseInt(returnexp.getText().toString());
        expense.setReturnexp(ret);
        int oth = Integer.parseInt(other.getText().toString());
        expense.setOther(oth);
        expense.setTotal(dep,lun,ret,oth);

        expense.setExpname1(expname1.getText().toString());
        expense.setExpname2(expname2.getText().toString());
        expense.setExpname3(expname3.getText().toString());
        expense.setExpname4(expname4.getText().toString());

        if(expense.getExpname1().equals("")){
            expense.setExpname1("Unknown");
        }
        else if(expense.getExpname2().equals("")){
            expense.setExpname1("Unknown");
        }
        else if(expense.getExpname3().equals("")){
            expense.setExpname1("Unknown");
        }
        else if(expense.getExpname4().equals("")){
            expense.setExpname1("Unknown");
        }



    }
    public void btnUpdate(View view){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    getValues();

                    String dt = expense.getDate();
                    int total = expense.getTotal();

                    ref.child(userid).child(dt).setValue(expense); //date is the key and userid is to save the data for the logged in user
                    Toast.makeText(UpdateExpActivity.this, "    Data Updated\nTotal Expense: " + total, Toast.LENGTH_SHORT).show();


                }
                catch (Exception e){
                    Toast.makeText(UpdateExpActivity.this,"Amounts fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnViewExp(View view){ //this button helps to go back to the list of expenses and refresh the list

        ref.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent(UpdateExpActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String convertDateToFirebaseFormat(String year, String monthname, String day){
        String expense_date="";
        if(monthname.equals("Jan")){
            expense_date = year+"-"+"01"+"-"+day;
        }
        else if(monthname.equals("Feb")){
            expense_date = year+"-"+"02"+"-"+day;
        }
        else if(monthname.equals("Mar")){
            expense_date = year+"-"+"03"+"-"+day;
        }
        else if(monthname.equals("Apr")){
            expense_date = year+"-"+"04"+"-"+day;
        }
        else if(monthname.equals("May")){
            expense_date = year+"-"+"05"+"-"+day;
        }
        else if(monthname.equals("Jun")){
            expense_date = year+"-"+"06"+"-"+day;
        }
        else if(monthname.equals("Jul")){
            expense_date = year+"-"+"07"+"-"+day;
        }
        else if(monthname.equals("Aug")){
            expense_date = year+"-"+"08"+"-"+day;
        }
        else if(monthname.equals("Sep")){
            expense_date = year+"-"+"09"+"-"+day;
        }
        else if(monthname.equals("Oct")){
            expense_date = year+"-"+"10"+"-"+day;
        }
        else if(monthname.equals("Nov")){
            expense_date = year+"-"+"11"+"-"+day;
        }
        else if(monthname.equals("Dec")){
            expense_date = year+"-"+"12"+"-"+day;
        }
        return expense_date;
    }
}
