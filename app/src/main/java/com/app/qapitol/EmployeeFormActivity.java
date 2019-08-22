package com.app.qapitol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeFormActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etDept;
    Button button;

    final String BASE_URI = "http://192.168.1.110:8080/employee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_form);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDept = findViewById(R.id.etDept);
        button = findViewById(R.id.save);

        final String id = getIntent().getStringExtra("id");
        if(null!=id){
            RequestQueue requestQueue = Volley.newRequestQueue(EmployeeFormActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URI + "/" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("data",response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        etName.setText(jsonObject.getString("name"));
                        etPhone.setText(jsonObject.getString("phone"));
                        etDept.setText(jsonObject.getString("dept"));
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
            requestQueue.add(stringRequest);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                final String name = etName.getText().toString().trim();
                final String phone = etPhone.getText().toString().trim();
                final String dept = etDept.getText().toString().trim();
                Log.d("error","Hello World");
                if(name.equals("") || phone.equals("") || dept.equals("")){
                    Toast.makeText(EmployeeFormActivity.this,"All Fields Are Required",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URI, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(EmployeeFormActivity.this,"Employee saved",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeFormActivity.this, EmployeesActivity.class);
                            Log.d("error","Response "+response.toString());
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error","Error "+error.toString());
                        }
                    }){
                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }

                        @Override
                        public byte[] getBody() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                if(null!=id){
                                    jsonObject.put("id",id);
                                }
                                jsonObject.put("name",name);
                                jsonObject.put("phone",phone);
                                jsonObject.put("dept",dept);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return jsonObject.toString().getBytes();
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}
