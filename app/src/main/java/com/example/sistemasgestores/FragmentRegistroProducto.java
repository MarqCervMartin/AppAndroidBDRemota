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

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistroProducto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRegistroProducto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistroProducto extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Componentes *****************************************************************************************ICO MARES
    private String ipPHP ="192.168.1.3";

    private Button bru;
    private EditText idp,nombre, precio, costo,canditad,descripcion;
    //private TextView mensaje;
    //*******************************************************JSON
    private ProgressDialog progeso;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;

    private OnFragmentInteractionListener mListener;

    public FragmentRegistroProducto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegistroProducto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegistroProducto newInstance(String param1, String param2) {
        FragmentRegistroProducto fragment = new FragmentRegistroProducto();
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
        View vista = inflater.inflate(R.layout.fragment_fragment_registro_producto, container, false);

        bru = (Button) vista.findViewById(R.id.btnProductos);
        idp = (EditText) vista.findViewById(R.id.ETIP);
        nombre = (EditText) vista.findViewById(R.id.ETNP);
        precio = (EditText) vista.findViewById(R.id.ETPP);
        costo = (EditText) vista.findViewById(R.id.ETCP);
        canditad = (EditText) vista.findViewById(R.id.ETCANP);
        descripcion = (EditText) vista.findViewById(R.id.ETD);

        request = Volley.newRequestQueue(getContext());
        bru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensaje.setText("Click");
                if(nombre.getText().toString().isEmpty() || precio.getText().toString().isEmpty() || costo.getText().toString().isEmpty()
                        || canditad.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty() ) {
                    //Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                    //mensaje.setText("Llene los campos");


                }else{
                    //Aqui metemos lo de MySQL
                    /*
                    Usuarios obj = new Usuarios();
                    obj.setName(nombre.getText().toString());
                    obj.setUser(usuario.getText().toString());
                    obj.setPassword(pass.getText().toString());

                    dataReference.child("Usuarios").setValue(obj);
                    */
                    webService();
                    //mensaje.setText("Registrado");
                    limpiar();
                }
            }
        });

        return vista;
    }

    private void webService() {
        progeso = new ProgressDialog(getContext());
        progeso.setMessage("Enviando...");
        progeso.show();
        String url="http://"+ipPHP+"/BD_remota/wsJSONRegistroProducto.php?id="+idp.getText().toString()+"&precio="+ precio.getText().toString()
                +"&costo="+costo.getText().toString()+ "&nombre="+nombre.getText().toString()+"&cantidad="+canditad.getText().toString()
                +"&descripcion="+descripcion.getText().toString();

        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
        //request.setRetryPolicy
    }
    private void limpiar() {
        idp.setText("");
        nombre.setText("");
        precio.setText("");
        canditad.setText("");
        costo.setText("");
        descripcion.setText("");

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progeso.hide();
        Toast.makeText(getContext(), "Verificar"+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show();
        progeso.hide();
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
