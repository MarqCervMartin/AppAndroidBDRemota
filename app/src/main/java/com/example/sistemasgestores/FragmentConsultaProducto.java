package com.example.sistemasgestores;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConsultaProducto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultaProducto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultaProducto extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ////*****************,***********************************mares
    private String ipPHP ="192.168.1.3";

    private EditText idET;
    private EditText id,nombre,precio,costo,cantidad,descripcion;
    private Button btnCProducto,btnelimina,btnactualiza;
    private ProgressDialog progreso;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;

    public FragmentConsultaProducto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultaProducto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultaProducto newInstance(String param1, String param2) {
        FragmentConsultaProducto fragment = new FragmentConsultaProducto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =inflater.inflate(R.layout.fragment_fragment_consulta_producto, container, false);

        id = (EditText) vista.findViewById(R.id.TVCI);
        nombre = (EditText) vista.findViewById(R.id.TVCN);
        precio = (EditText) vista.findViewById(R.id.TVCP);
        costo = (EditText) vista.findViewById(R.id.TVCC);
        cantidad = (EditText) vista.findViewById(R.id.TVCCAN);
        descripcion = (EditText) vista.findViewById(R.id.TVCD);

        idET = (EditText) vista.findViewById(R.id.ETIC);
        btnCProducto = (Button) vista.findViewById(R.id.btnCP);
        btnelimina = (Button) vista.findViewById(R.id.btnElimina);
        btnactualiza = (Button) vista.findViewById(R.id.btnActualiza);

        request = Volley.newRequestQueue(getContext());

        btnCProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    webservice();
                }
            }
        });
        btnelimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    webserviceBorrar();
                }
            }
        });
        btnactualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    webserviceActualizar();
                }
            }
        });
        return vista;
    }

    private void webserviceBorrar() {
        String url = "http://"+ipPHP+"/BD_remota/wsJSONBorraProducto.php?id="+id.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
        limpiar();
    }

    private void limpiar() {
        id.setText("");
        nombre.setText("");
        precio.setText("");
        costo.setText("");
        cantidad.setText("");
        descripcion.setText("");

    }

    private void webserviceActualizar() {
        String url = "http://"+ipPHP+"/BD_remota/wsJSONActualizaProducto.php?id="+id.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
        limpiar();
    }

    private void webservice() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando Consulta...");
        progreso.show();


        String url = "http://"+ipPHP+"/BD_remota/wsJSONConsultaProducto.php?id="+idET.getText().toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "Verifique"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(getContext(), "Mensaje :"+response, Toast.LENGTH_SHORT).show();

        JSONArray json =response.optJSONArray("user");
        JSONObject jsonObject = null;
        String i="",n="",p="",c="",can="",d="";
        try {
            jsonObject= json.getJSONObject(0);
            i = jsonObject.optString("id");
            n = jsonObject.optString("nombre");
            p = jsonObject.optString("precio");
            c = jsonObject.optString("costo");
            can = jsonObject.optString("cantidad");
            d = jsonObject.optString("descripcion");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        id.setText(i);
        nombre.setText(n);
        precio.setText(p);
        costo.setText(c);
        cantidad.setText(can);
        descripcion.setText(d);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
