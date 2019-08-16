package com.example.expensetrackauth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static String TAG = Adapter.class.getSimpleName();
    Context context;
    ArrayList<Expense> expense;


    public Adapter(Context c, ArrayList<Expense>e){
        this.context = c;
        this.expense = e;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        ExpenseViewHolder item = new ExpenseViewHolder(row); // pass the view to View Holder
        return item;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ExpenseViewHolder) holder).date.setText("Date: " + expense.get(position).getDate());
        ((ExpenseViewHolder) holder).departexp.setText(expense.get(position).getExpname1()+": " + expense.get(position).getDepart());
        ((ExpenseViewHolder) holder).lunchexp.setText(expense.get(position).getExpname2()+": " + expense.get(position).getLunch());
        ((ExpenseViewHolder) holder).returnexp.setText(expense.get(position).getExpname3()+": " + expense.get(position).getReturnexp());
        ((ExpenseViewHolder) holder).otherexp.setText(expense.get(position).getExpname4()+": " + expense.get(position).getOther());
        ((ExpenseViewHolder) holder).totalexp.setText("Total: " + expense.get(position).getTotal());


        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalcost = expense.get(position).getTotal();
                // display a toast with person name on item click
                Toast.makeText(context, "     " + expense.get(position).getDate() + "\nTotal Expense: " + totalcost, Toast.LENGTH_SHORT).show();
            }
        });
        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int p=position;
                String pos = ""+p;
                System.out.println(pos);
                Log.d(TAG, pos);
                deleteItem(p);
                return false;// returning true instead of false, works for me
            }
        });*/

    }
    /*private void deleteItem(int pos){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userid = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Expense").child(userid).child(expense.get(pos).getDate());
        databaseReference.removeValue();
        Log.d(TAG,"removed value on long click");
        Toast.makeText(context,"Expense of "+expense.get(pos).getDate() +"removed",Toast.LENGTH_SHORT ).show();
    }*/



    @Override
    public int getItemCount() {
        return expense.size();
    }
    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView date, departexp, lunchexp, returnexp, otherexp, totalexp;// init the item view's

        public ExpenseViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            date = (TextView) itemView.findViewById(R.id.date);
            departexp = (TextView) itemView.findViewById(R.id.departExp);
            lunchexp = (TextView) itemView.findViewById(R.id.lunchExp);
            returnexp = (TextView) itemView.findViewById(R.id.returnExp);
            otherexp = (TextView) itemView.findViewById(R.id.otherExp);
            totalexp = (TextView) itemView.findViewById(R.id.totalExp);




        }
    }
}
