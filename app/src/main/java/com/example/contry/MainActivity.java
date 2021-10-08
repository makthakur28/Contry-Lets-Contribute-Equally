package com.example.contry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button getResults, addMember;
    TextView clear;
    ArrayList<Modal> list,posMember,negMember;
    RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        addMember= findViewById(R.id.addMember);
        getResults = findViewById(R.id.getResults);
        clear = findViewById(R.id.clear);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMember();
            }
        });

        getResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetResults();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList(list);
            }
        });

        list = new ArrayList<>();

        adapter = new RecyclerViewAdapter(list,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    public void AddMember(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_action);
        Button addBtn = dialog.findViewById(R.id.action_btn);
        EditText name = dialog.findViewById(R.id.name_new);
        EditText amt = dialog.findViewById(R.id.amt_new);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String member="",amount="";
                if(!name.getText().toString().equals("")){
                    member = name.getText().toString();
                }
                else{
                    Toast.makeText(MainActivity.this, "Plz enter Member name", Toast.LENGTH_SHORT).show();
                }
                if(!amt.getText().toString().equals("")){
                    amount = amt.getText().toString();
                }
                else{
                    Toast.makeText(MainActivity.this, "Plz enter amount", Toast.LENGTH_SHORT).show();
                }
                if(!member.equals("") && !amount.equals("")){
                    list.add(new Modal(member,Integer.parseInt(amount)));
                    adapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    public void GetResults(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.results);

        TextView tv1 =dialog.findViewById(R.id.tv1);

        int size = adapter.getItemCount();
        double amtForEach=0,sum=0;
        if(size==1 || size==0){
            Toast.makeText(MainActivity.this, "Get some friends to contribute.", Toast.LENGTH_LONG).show();
            return ;
        }
        for (int i=0;i<size;i++){
            double temp=list.get(i).getAmount();
            sum+=temp;
        }
        amtForEach = sum/size;
        negMember = new ArrayList<>();
        posMember = new ArrayList<>();
        for (int i=0;i<size;i++){
            double diff =list.get(i).getAmount() - amtForEach;
            String temp=list.get(i).getName();
            if(diff<0){
                negMember.add(new Modal(temp,Math.abs(diff)));
            }
            else{
                posMember.add(new Modal(temp,Math.abs(diff)));
            }
        }
        int i=0,j=0;
        while (i<posMember.size() && j<negMember.size()){
            if(posMember.get(i).getAmount()==negMember.get(j).getAmount()){
                tv1.append(String.format("→ %s will recieve Rs.%s from %s\n\n", posMember.get(i).getName().toString(), posMember.get(i).getAmount(), negMember.get(j).getName().toString()));
                posMember.get(i).setAmount(0);
                negMember.get(j).setAmount(0);
                i++;j++;
            }
            else if (posMember.get(i).getAmount()>negMember.get(j).getAmount()){
                tv1.append(String.format("→ %s will recieve Rs.%s from %s\n\n", posMember.get(i).getName().toString(), negMember.get(j).getAmount(), negMember.get(j).getName().toString()));
                posMember.get(i).setAmount(posMember.get(i).getAmount()-negMember.get(j).getAmount());
                negMember.get(j).setAmount(0);
                j++;
            }
            else{
                tv1.append(String.format("→ %s will recieve Rs.%s from %s\n\n", posMember.get(i).getName().toString(), posMember.get(i).getAmount(), negMember.get(j).getName().toString()));
                negMember.get(j).setAmount(negMember.get(j).getAmount()-posMember.get(i).getAmount());
                posMember.get(i).setAmount(0);
                i++;
            }
        }
        dialog.show();
    }

    public void clearList(ArrayList<Modal> list){
        list.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "List Deleted", Toast.LENGTH_SHORT).show();
    }

}

