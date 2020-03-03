package com.prueba.webservices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnEditar,btnBuscar,btnAgregar,btnEliminar;

    EditText edtCodigo,edtPrecio,edtProducto,edtFabricante;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtCodigo=findViewById(R.id.edtCodigo);
        edtFabricante=findViewById(R.id.edtFabricante);
        edtPrecio=findViewById(R.id.edtPrecio);
        edtProducto=findViewById(R.id.edtProducto);

        btnAgregar=findViewById(R.id.btnAgregar);
        btnBuscar=findViewById(R.id.btnBuscar);
        btnEditar=findViewById(R.id.btnEditar);
        btnEliminar=findViewById(R.id.btnEliminar);


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ejecutarServicio("http://192.168.64.2/developer/eliminar_producto.php");

            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ejecutarServicio("http://192.168.64.2/developer/editar_producto.php");


            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ejecutarServicio("http://192.168.64.2/developer/insertar_producto.php");

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buscarDatos("http://192.168.64.2/developer/buscar_producto.php?codigo="
                        +edtCodigo.getText().toString().trim()+"");

            }
        });

    }


    private void ejecutarServicio(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "Operacion bien papu", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("codigo",edtCodigo.getText().toString());
                parametros.put("producto",edtProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                parametros.put("fabricante",edtFabricante.getText().toString());



                return parametros;
            }
        };

         requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void buscarDatos(String url ){

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {


                    try {


                        jsonObject = response.getJSONObject(i);
                        edtProducto.setText(jsonObject.getString("producto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));

                    } catch (JSONException e) {

                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error",error.getMessage());

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }


    private void eliminarProducto(String url){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "El producto se elimino", Toast.LENGTH_SHORT).show();

                limpiarCaja();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("codigo",edtCodigo.getText().toString());

                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void limpiarCaja(){

        edtProducto.setText("");

        edtPrecio.setText("");
        edtFabricante.setText("");
        edtCodigo.setText("");
    }

}
