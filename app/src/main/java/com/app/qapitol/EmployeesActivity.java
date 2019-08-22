package com.app.qapitol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeesActivity extends AppCompatActivity {

    final String BASE_URI = "http://192.168.1.110:8080/employee";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles);

        Button btnNewEmployee = findViewById(R.id.btnNewEmployee);

        btnNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeesActivity.this, EmployeeFormActivity.class);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    LinearLayout linearLayout = findViewById(R.id.linearLayout);
                    TableLayout tableLayout = new TableLayout(EmployeesActivity.this);
                    TableRow tableRow = new TableRow(EmployeesActivity.this);
                    TextView tvId = new TextView(EmployeesActivity.this);
                    tvId.setText("ID");
                    tvId.setPadding(15,15,15,15);
                    tableRow.addView(tvId);
                    TextView tvName = new TextView(EmployeesActivity.this);
                    tvName.setText("Name");
                    tvName.setPadding(15,15,15,15);
                    tableRow.addView(tvName);
                    TextView tvPhone = new TextView(EmployeesActivity.this);
                    tvPhone.setText("Phone");
                    tvPhone.setPadding(2,2,2,2);
                    tableRow.addView(tvPhone);
                    TextView tvDept = new TextView(EmployeesActivity.this);
                    tvDept.setText("Dept");
                    tvDept.setPadding(2,2,2,2);
                    tableRow.addView(tvDept);
                    TextView tvActions = new TextView(EmployeesActivity.this);
                    tvActions.setText("Actions");
                    tvActions.setPadding(2,2,2,2);
                    tableRow.addView(tvActions);
                    tableLayout.addView(tableRow);
                    Log.d("array",jsonArray.toString());
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        TableRow tableRowData = new TableRow(EmployeesActivity.this);
                        final TextView tvIdData = new TextView(EmployeesActivity.this);
                        tvIdData.setPadding(15,15,15,15);
                        tvIdData.setText(""+data.getString("id"));
                        tableRowData.addView(tvIdData);
                        TextView tvNameData = new TextView(EmployeesActivity.this);
                        tvNameData.setPadding(15,15,15,15);
                        tvNameData.setText(data.getString("name"));
                        tableRowData.addView(tvNameData);
                        TextView tvPhoneData = new TextView(EmployeesActivity.this);
                        tvPhoneData.setPadding(15,15,15,15);
                        tvPhoneData.setText(data.getString("phone"));
                        tableRowData.addView(tvPhoneData);
                        TextView tvDeptData = new TextView(EmployeesActivity.this);
                        tvDeptData.setPadding(15,15,15,15);
                        tvDeptData.setText(data.getString("dept"));
                        tableRowData.addView(tvDeptData);
                        Button editButton = new Button(EmployeesActivity.this);
                        editButton.setText("Edit");
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EmployeesActivity.this, EmployeeFormActivity.class);
                                intent.putExtra("id",tvIdData.getText());
                                Toast.makeText(EmployeesActivity.this,"Edit "+tvIdData.getText(),Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
                        tableRowData.addView(editButton);
                        Button deleteButton = new Button(EmployeesActivity.this);
                        deleteButton.setText("Delete");
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestQueue requestQueue = Volley.newRequestQueue(EmployeesActivity.this);
                                StringRequest request = new StringRequest(Request.Method.DELETE,BASE_URI+"/"+tvIdData.getText(),null,null);
                                requestQueue.add(request);
                                Toast.makeText(EmployeesActivity.this,"Record Deleted",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                        tableRowData.addView(deleteButton);
                        tableLayout.addView(tableRowData);
                    }

                    linearLayout.addView(tableLayout);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(stringRequest);
    }
}
