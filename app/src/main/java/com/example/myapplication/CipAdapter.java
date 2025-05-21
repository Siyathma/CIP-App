package com.example.myapplication;

import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup; import android.widget.TextView;

import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList; import java.util.List;

public class CipAdapter extends RecyclerView.Adapter<CipAdapter.CipViewHolder> { private List cipList;

    public CipAdapter(List<CipRecord> cipList) {
        this.cipList = cipList != null ? cipList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cip, parent, false);
        return new CipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CipViewHolder holder, int position) {
        if (cipList != null && position < cipList.size()) {
            CipRecord record = (CipRecord) cipList.get(position);

            if (record != null) {
                // Set task as the main title
                holder.tvTask.setText(record.getTask());

                // Set other fields
                holder.tvLineType.setText("Line: " + nullSafe(record.getLineType()));
                holder.tvTank.setText("Tank: " + nullSafe(record.getTank()));
                holder.tvDate.setText("Date: " + nullSafe(record.getDate()));
                holder.tvStartTime.setText("Start: " + nullSafe(record.getStartedTime()));
                holder.tvEndTime.setText("End: " + nullSafe(record.getEndTime()));
                holder.tvWaterVol.setText("Water: " + nullSafe(record.getWaterVol()) + " L");
                holder.tvWaterTemp.setText("Temp: " + nullSafe(record.getWaterTemperature()) + " °C");

                // Employee info
                String employeeInfo = "Employee: ";
                if (record.getEmployeeName() != null && !record.getEmployeeName().equals("null")) {
                    employeeInfo += record.getEmployeeName() + " (" + record.getEmployeeId() + ")";
                } else {
                    employeeInfo += "ID: " + record.getEmployeeId();
                }
                holder.tvEmployee.setText(employeeInfo);

                // Ingredients (show only if not null)
                if (record.getIngredients() != null && !record.getIngredients().equals("null")) {
                    holder.tvIngredients.setVisibility(View.VISIBLE);
                    String ingredientInfo = "Ingredients: " + record.getIngredients();
                    if (record.getIngredientQty() != null && !record.getIngredientQty().equals("null")) {
                        ingredientInfo += " (" + record.getIngredientQty() + ")";
                    }
                    holder.tvIngredients.setText(ingredientInfo);
                } else {
                    holder.tvIngredients.setVisibility(View.GONE);
                }

                // Remarks (show only if not null)
                if (record.getRemark() != null && !record.getRemark().equals("null")) {
                    holder.tvRemark.setVisibility(View.VISIBLE);
                    holder.tvRemark.setText("Remarks: " + record.getRemark());
                } else {
                    holder.tvRemark.setVisibility(View.GONE);
                }
            }
        }
    }

    // Helper method to handle null values
    private String nullSafe(String value) {
        if (value == null || value.equals("null")) {
            return "N/A";
        }
        return value;
    }

    @Override
    public int getItemCount() {
        return cipList != null ? cipList.size() : 0;
    }

    public void updateData(List<CipRecord> newData) {
        this.cipList = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class CipViewHolder extends RecyclerView.ViewHolder {
        TextView tvTask, tvLineType, tvTank, tvDate;
        TextView tvStartTime, tvEndTime, tvWaterVol, tvWaterTemp;
        TextView tvEmployee, tvIngredients, tvRemark;

        public CipViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tv_task);
            tvLineType = itemView.findViewById(R.id.tv_line_type);
            tvTank = itemView.findViewById(R.id.tv_tank);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStartTime = itemView.findViewById(R.id.tv_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_end_time);
            tvWaterVol = itemView.findViewById(R.id.tv_water_vol);
            tvWaterTemp = itemView.findViewById(R.id.tv_water_temp);
            tvEmployee = itemView.findViewById(R.id.tv_employee);
            tvIngredients = itemView.findViewById(R.id.tv_ingredients);
            tvRemark = itemView.findViewById(R.id.tv_remark);
        }
    }
}