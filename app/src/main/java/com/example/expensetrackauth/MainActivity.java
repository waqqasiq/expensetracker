package com.example.expensetrackauth;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    Adapter adapter;
    DatabaseReference ref;
    ArrayList<Expense> expenseList;
    ArrayList<Expense>filteredList_dateConverted;
    String userid;
    FirebaseUser currentUser;
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab); //fab is in activity_main.xml
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddExpenses.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        //btnLogout = (Button)findViewById(R.id.btnLogout);
        //item1 = (View)findViewById(R.id.action_settings);

        mAuth = FirebaseAuth.getInstance();//

        currentUser = mAuth.getCurrentUser();//

        if (currentUser == null) {
        //No one signed in
            startActivity(new Intent(this, SigninActivity.class));
            this.finish();
        }
        else{
            if(isSignIn()) {
                Toast.makeText(MainActivity.this, "      Signed in as \n" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            }
            expenseList = new ArrayList<Expense>();
            recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
            searchView = (SearchView)findViewById(R.id.searchView);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ref = FirebaseDatabase.getInstance().getReference("Expense").child(currentUser.getUid());//
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Expense e = dataSnapshot1.getValue(Expense.class);//
                        expenseList.add(e);
                        Log.d(TAG, ""+expenseList);
                    }

                    final ArrayList<Expense>filteredList;
                    filteredList = new ArrayList<Expense>();
                    for(int i=0;i<expenseList.size();i++){ // naive way of not showing the days where expense was 0
                        if(expenseList.get(i).getTotal()>0){
                            filteredList.add(expenseList.get(i));
                        }
                    }


                    filteredList_dateConverted = new ArrayList<Expense>();
                    filteredList_dateConverted = convertDateOfList(filteredList); // turning date into user readable format


                    adapter = new Adapter(MainActivity.this, filteredList_dateConverted);//may use expenseList instead
                    recyclerView.setAdapter(adapter);//

                    //https://www.youtube.com/watch?v=PmqYd-AdmC0
                    //Search View video tutorial
                    /*for(int i=0;i<filteredList.size();i++){
                        tempList.add(filteredList.get(i));
                    }*/

                    if(searchView!=null){

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                ArrayList<Expense> searchList = new ArrayList<Expense>();


                                for(Expense object: filteredList_dateConverted){
                                    if(object.getDate().toLowerCase().contains(newText.toLowerCase()) || object.getExpname1().toLowerCase().contains(newText.toLowerCase()) || object.getExpname2().toLowerCase().contains(newText.toLowerCase()) || object.getExpname3().toLowerCase().contains(newText.toLowerCase()) || object.getExpname4().toLowerCase().contains(newText.toLowerCase())){
                                        searchList.add(object);

                                    }
                                }

                                Adapter newAdapter = new Adapter(MainActivity.this, searchList);
                                recyclerView.setAdapter(newAdapter);
                                return true;
                            }
                        });

                    }
                    else{
                        adapter = new Adapter(MainActivity.this, filteredList);//may use expenseList instead
                        recyclerView.setAdapter(adapter);//

                    }



                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { //action_settings is in menu_menu.xml
            mAuth.signOut();//
            startActivity(new Intent(MainActivity.this, SigninActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            MainActivity.this.finish();
        }
        else if(id==R.id.summary){
            startActivity(new Intent(MainActivity.this, SummaryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSignIn(){
        if(getIntent().hasExtra("btnSignIn")) {
            return getIntent().getBooleanExtra("btnSignIn", false);
        }
        else{
            return false;
        }

    }

    private ArrayList<Expense> convertDateOfList(ArrayList<Expense> originalList){
        ArrayList<Expense> tempList = new ArrayList<Expense>(originalList);

        for(int i=0;i<tempList.size();i++){ // making user's life easier
            String dt = originalList.get(i).getDate();
            String monthNum = ""+dt.charAt(5)+dt.charAt(6);
            String year = ""+dt.charAt(0)+dt.charAt(1)+dt.charAt(2)+dt.charAt(3);
            String day = ""+dt.charAt(8)+dt.charAt(9);
            if(monthNum.equals("01")){
                tempList.get(i).setDate("Jan "+day+", "+year);
            }else if(monthNum.equals("02")){
                tempList.get(i).setDate("Feb "+day+", "+year);
            }else if(monthNum.equals("03")){
                tempList.get(i).setDate("Mar "+day+", "+year);
            }else if(monthNum.equals("04")){
                tempList.get(i).setDate("Apr "+day+", "+year);
            }else if(monthNum.equals("05")){
                tempList.get(i).setDate("May "+day+", "+year);
            }else if(monthNum.equals("06")){
                tempList.get(i).setDate("Jun "+day+", "+year);
            }else if(monthNum.equals("07")){
                tempList.get(i).setDate("Jul "+day+", "+year);
            }
            else if(monthNum.equals("08")){
                tempList.get(i).setDate("Aug "+day+", "+year);
            }
            else if (monthNum.equals("09")){
                tempList.get(i).setDate("Sep "+day+", "+year);
            }
            else if(monthNum.equals("10")){
                tempList.get(i).setDate("Oct "+day+", "+year);
            }else if(monthNum.equals("11")){
                tempList.get(i).setDate("Nov "+day+", "+year);
            }else if(monthNum.equals("12")){
                tempList.get(i).setDate("Dec "+day+", "+year);
            }

        }
        return tempList;
    }

}
