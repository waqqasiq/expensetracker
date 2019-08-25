package com.example.expensetrackauth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static String TAG = SummaryAdapter.class.getSimpleName();
    Context context;
    ArrayList<Expense> expense;
    ArrayList<String> yearmonthlist;
    ArrayList<Integer> yearmonthtotal;
    ArrayList<Integer> numOfDays;
    ArrayList<Double> averageperday;


    public SummaryAdapter(Context c, ArrayList<Expense>e, ArrayList<String> yearmonthlist, ArrayList<Integer> yearmonthtotal,ArrayList<Integer> numDays, ArrayList<Double> avgperday){
        this.context = c;
        this.expense = e;
        this.yearmonthlist = yearmonthlist;
        this.yearmonthtotal = yearmonthtotal;
        this.numOfDays = numDays;
        this.averageperday = avgperday;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_summary, parent, false);
        SummaryAdapter.ExpenseSummaryViewHolder item = new SummaryAdapter.ExpenseSummaryViewHolder(row); // pass the view to View Holder
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        ((ExpenseSummaryViewHolder) holder).monthyear.setText("" + yearmonthlist.get(position));
        ((ExpenseSummaryViewHolder) holder).totalexpense.setText("Total Expense: " + yearmonthtotal.get(position));
        ((ExpenseSummaryViewHolder) holder).numdays.setText("NumOfDays: " + numOfDays.get(position));
        ((ExpenseSummaryViewHolder) holder).average.setText("Avg per day: " + String.format("%.2f",averageperday.get(position)) );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, yearmonthlist.get(position)+" -> "+"Total: "+yearmonthtotal.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return yearmonthlist.size();
    }

    public class ExpenseSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView monthyear, totalexpense, numdays, average;// init the item view's


        public ExpenseSummaryViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            monthyear = (TextView) itemView.findViewById(R.id.monthyear);
            totalexpense = (TextView) itemView.findViewById(R.id.totalexpense);
            numdays = (TextView) itemView.findViewById(R.id.numdays);
            average = (TextView) itemView.findViewById(R.id.average);






        }
    }

}
