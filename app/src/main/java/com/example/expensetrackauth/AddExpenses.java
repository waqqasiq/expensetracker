package com.example.expensetrackauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddExpenses extends AppCompatActivity {

    private static final String TAG = AddExpenses.class.getSimpleName();
    EditText date,depart,lunch,returnexp,other, expname1, expname2, expname3, expname4;
    Button btnSave;
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference ref;
    Expense expense;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        date = (EditText)findViewById(R.id.date);
        String datenow = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        date.setText(datenow); //by default today's date will be shown and can be edited by user
        depart = (EditText)findViewById(R.id.depart);
        lunch = (EditText)findViewById(R.id.lunch);
        returnexp = (EditText)findViewById(R.id.returnexp);
        other = (EditText)findViewById(R.id.other);
        btnSave = (Button)findViewById(R.id.btnSave);

        expname1 = (EditText)findViewById(R.id.expname1);
        expname2 = (EditText)findViewById(R.id.expname2);
        expname3 = (EditText)findViewById(R.id.expname3);
        expname4 = (EditText)findViewById(R.id.expname4);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Expense");
        expense = new Expense();

        user = mAuth.getInstance().getCurrentUser();
        userid = user.getUid();

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
    public void btnInsert(View view){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    getValues();

                    String dt = expense.getDate();
                    int total = expense.getTotal();
                    if (dt.length() == 10) {
                        ref.child(userid).child(dt).setValue(expense); //date is the key and userid is to save the data for the logged in user
                        Toast.makeText(AddExpenses.this, "    Data Inserted\nTotal Expense: " + total, Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(AddExpenses.this, "Input date in the specified format", Toast.LENGTH_SHORT).show();

                    }
                }
                catch (Exception e){
                    Toast.makeText(AddExpenses.this,"Date and amounts fields must be filled", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AddExpenses.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
