package com.example.contry;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    ArrayList<Modal> list;
    Context context;

    public RecyclerViewAdapter(ArrayList<Modal> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_card,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Modal modal = list.get(position);

        holder.name.setText(modal.getName());
        holder.amtpaid.setText(String.format("Rs. %s", modal.getAmount()));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_action);
                Button addBtn = dialog.findViewById(R.id.action_btn);
                EditText name = dialog.findViewById(R.id.name_new);
                EditText amt = dialog.findViewById(R.id.amt_new);
                TextView title = dialog.findViewById(R.id.title);

                addBtn.setText("Update");
                name.setText(list.get(pos).getName().toString());
                amt.setText(String.format("%s", list.get(pos).getAmount()));
                title.setText("UPDATE MEMBER");
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String member="",amount="";
                        if(!name.getText().toString().equals("")){
                            member = name.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Plz enter Member name", Toast.LENGTH_SHORT).show();
                        }
                        if(!amt.getText().toString().equals("")){
                            amount = amt.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Plz enter amount", Toast.LENGTH_SHORT).show();
                        }
                        if(!member.equals("") && !amount.equals("")){
                            list.set(pos,new Modal(member,Integer.parseInt(amount)));
                            notifyItemChanged(pos);
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,amtpaid;
        ImageView delete,edit;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);
            amtpaid = itemView.findViewById(R.id.amtpaid);
            delete = itemView.findViewById(R.id.delete_member);
            edit = itemView.findViewById(R.id.edit_member);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });

        }
    }
}
