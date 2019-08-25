package com.example.expensetrackauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SummaryActivity extends AppCompatActivity {
    private static final String TAG = SummaryActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    SummaryAdapter adapter;
    DatabaseReference ref;
    ArrayList<Expense> expenseList;
    String userid;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        mAuth = mAuth.getInstance();//

        currentUser = mAuth.getInstance().getCurrentUser();//

        if (currentUser == null) {
//No one signed in
            startActivity(new Intent(this, SigninActivity.class));
            this.finish();
        }
        else{
            //Toast.makeText(SummaryActivity.this,"      Signed in as \n"+currentUser.getEmail(),Toast.LENGTH_SHORT).show();
            expenseList = new ArrayList<Expense>();
            recyclerView = (RecyclerView)findViewById(R.id.recyclerviewsummary);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ref = FirebaseDatabase.getInstance().getReference("Expense").child(currentUser.getUid());//
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Expense e = dataSnapshot1.getValue(Expense.class);//
                        expenseList.add(e);
                        Log.d(TAG, ""+e);
                    }

                    ArrayList<Expense>filteredList;
                    filteredList = new ArrayList<Expense>();
                    for(int k=0;k<expenseList.size();k++){ // naive way of not showing the days where expense was 0
                        if(expenseList.get(k).getTotal()>0){
                            filteredList.add(expenseList.get(k));
                        }
                    }
                    ///////////The following code snippet is used for summarizing monthly total expense, number of days of expense, and average
                    ArrayList<String> yearmonthlist= new ArrayList<String>();
                    ArrayList<Integer> yearmonthtotal = new ArrayList<Integer>();
                    ArrayList<Integer> numberOfDays = new ArrayList<Integer>();

                    int i=0;
                    ArrayList<Integer>indexUsed = new ArrayList<Integer>();
                    while(i<filteredList.size()){
                        int count =0; //counter is used to count the number of days of a month

                        String di = filteredList.get(i).getDate();
                        String [] yearMonthDayi = di.split("-",3); //splitting the string into string array {yyyy, MM, dd}
                        String yearmonthi = yearMonthDayi[0]+"-"+yearMonthDayi[1]; // example: 2019-08
                        if(yearmonthlist.size()==0){
                            yearmonthtotal.add(filteredList.get(i).getTotal());
                            yearmonthlist.add(yearmonthi);
                            indexUsed.add(i); //at first the first index will always be used
                        }
                        else {
                            for (int k = 0; k < indexUsed.size(); k++) {
                                if (i == indexUsed.get(k)) {
                                    i++; //if i is already used then need to go to next index
                                    break;
                                } else {
                                    yearmonthtotal.add(filteredList.get(i).getTotal());
                                    yearmonthlist.add(yearmonthi);
                                    break;
                                }
                            }
                        }


                        int j=0;
                        if(yearmonthtotal.size()!=0) {
                            for (j = 0; j < filteredList.size(); j++) {
                                if (i != j) {
                                    String dj = filteredList.get(j).getDate();
                                    String[] yearMonthDayj = dj.split("-", 3); //splitting the string into string array {yyyy, MM, dd}
                                    String yearmonthj = yearMonthDayj[0] + "-"+ yearMonthDayj[1]; //example: 2019-08

                                    if (yearmonthi.equals(yearmonthj)) { //index i matched with index j that is same month
                                        yearmonthlist.set(i, yearmonthi);
                                        yearmonthtotal.set(i, yearmonthtotal.get(i) + filteredList.get(j).getTotal()); //try to trace the code in a rough copy
                                        indexUsed.add(j); //this index j matched already so adding in indexUsed
                                        count++; //meaniing that the current month has 1 more day in it since if condition is successful, so need to ++ counter
                                    }


                                }
                            }


                        }
                        i++;

                        count+=1; //need to add 1 because for each match with j, the counter is count++ but for first i it 1 was not added
                        numberOfDays.add(count);


                    }
                    ArrayList<String> yearmonthlistunique= new ArrayList<String>();
                    ArrayList<Integer> yearmonthtotalunique = new ArrayList<Integer>();
                    ArrayList<Integer> numOfDaysUnique = new ArrayList<Integer>();



                    for(int k=0;k<yearmonthlist.size();k++){

                        if(!yearmonthtotalunique.contains(yearmonthtotal.get(k)) && !yearmonthlistunique.contains(yearmonthlist.get(k))){
                            yearmonthlistunique.add(yearmonthlist.get(k));
                            yearmonthtotalunique.add(yearmonthtotal.get(k));
                            numOfDaysUnique.add(numberOfDays.get(k));
                        }

                    }
                    ArrayList<Double> averageperday = new ArrayList<Double>(); //average expense per day in a month
                    for(int k=0;k<numOfDaysUnique.size();k++){
                        averageperday.add((yearmonthtotalunique.get(k)*1.0)/numOfDaysUnique.get(k));
                    }
                    ///////
                    ArrayList<String> tempList = new ArrayList<String>();
                    for(int k = 0;k<yearmonthlistunique.size(); k++){
                        String dt = yearmonthlistunique.get(k);
                        String monthNum = ""+dt.charAt(5)+dt.charAt(6);
                        String year = ""+dt.charAt(0)+dt.charAt(1)+dt.charAt(2)+dt.charAt(3);
                        if(monthNum.equals("01")){
                            tempList.add("January "+year);
                        }else if(monthNum.equals("02")){
                            tempList.add("February "+year);
                        }else if(monthNum.equals("03")){
                            tempList.add("March "+year);
                        }else if(monthNum.equals("04")){
                            tempList.add("April "+year);
                        }else if(monthNum.equals("05")){
                            tempList.add("May "+year);
                        }else if(monthNum.equals("06")){
                            tempList.add("June "+year);
                        }else if(monthNum.equals("07")){
                            tempList.add("July "+year);
                        }
                        else if(monthNum.equals("08")){
                            tempList.add("August "+year);
                        }
                        else if (monthNum.equals("09")){
                            tempList.add("September "+year);
                        }
                        else if(monthNum.equals("10")){
                            tempList.add("October "+year);
                        }else if(monthNum.equals("11")){
                            tempList.add("November "+year);
                        }else if(monthNum.equals("12")){
                            tempList.add("December "+year);
                        }

                    }


                    adapter = new SummaryAdapter(SummaryActivity.this, filteredList, tempList, yearmonthtotalunique, numOfDaysUnique, averageperday);//may use expenseList instead
                    recyclerView.setAdapter(adapter);//
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
