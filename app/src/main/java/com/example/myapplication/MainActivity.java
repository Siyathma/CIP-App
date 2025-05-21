package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle; import android.util.Log; import android.view.View; import android.widget.ProgressBar; import android.widget.TextView; import android.widget.Toast;

import com.android.volley.Request; import com.android.volley.RequestQueue; import com.android.volley.Response; import com.android.volley.VolleyError; import com.android.volley.toolbox.JsonObjectRequest; import com.android.volley.toolbox.Volley;

import org.json.JSONArray; import org.json.JSONException; import org.json.JSONObject;

import java.util.ArrayList; import java.util.List;

public class MainActivity extends AppCompatActivity { private static final String TAG = "MainActivity"; private RecyclerView recyclerView; private CipAdapter adapter; private List cipRecordList = new ArrayList<>(); private ProgressBar progressBar; private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewCip);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CipAdapter(cipRecordList);
        recyclerView.setAdapter(adapter);

        // Fetch data
        fetchCipRecords();
    }

    private void fetchCipRecords() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        String url = "http://152.42.179.58/v3/SMPMS-VER-2.0-API/manf-api/all-cip-records";
        Log.d(TAG, "Fetching data from: " + url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "API Response received");
                        try {
                            // Check if response has data
                            if (response.has("data")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                Log.d(TAG, "Found " + dataArray.length() + " records");

                                cipRecordList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    CipRecord record = new CipRecord();
                                    record.setRecId(obj.optString("rec_id", ""));
                                    record.setLineType(obj.optString("line_type", ""));
                                    record.setEmployeeId(obj.optString("employee_id", ""));
                                    record.setEmployeeName(obj.optString("employee_name", ""));
                                    record.setTask(obj.optString("task", ""));
                                    record.setTank(obj.optString("tank", ""));
                                    record.setStartedTime(obj.optString("started_time", ""));
                                    record.setEndTime(obj.optString("end_time", ""));
                                    record.setWaterVol(obj.optString("water_vol", ""));
                                    record.setWaterTemperature(obj.optString("water_temperature", ""));
                                    record.setIngredients(obj.optString("ingredients", ""));
                                    record.setIngredientQty(obj.optString("ingredient_qty", ""));
                                    record.setDate(obj.optString("date", ""));
                                    record.setTime(obj.optString("time", ""));
                                    record.setRemark(obj.optString("remark", ""));

                                    cipRecordList.add(record);
                                }

                                // Update UI
                                adapter.updateData(cipRecordList);
                                showRecyclerView();

                                if (cipRecordList.isEmpty()) {
                                    showError("No records found");
                                }
                            } else {
                                Log.e(TAG, "No data field in response");
                                showError("No data available");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                            showError("Error parsing data: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Network error: " + error.toString());
                        String errorMessage = "Network error";
                        if (error.networkResponse != null) {
                            errorMessage += " (Status code: " + error.networkResponse.statusCode + ")";
                        }
                        showError(errorMessage);
                    }
                });

        queue.add(request);
        Log.d(TAG, "Request added to queue");
    }

    private void showRecyclerView() {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
