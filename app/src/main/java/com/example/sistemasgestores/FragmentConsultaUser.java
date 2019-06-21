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
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConsultaUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConsultaUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConsultaUser extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ////****************************************************mares
    private String ipPHP ="192.168.1.3";

    private EditText username;
    private TextView id,nombre,usuario,pass,turno,salario;
    private Button btnConsulta;
    private ProgressDialog progreso;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;


    public FragmentConsultaUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConsultaUser.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConsultaUser newInstance(String param1, String param2) {
        FragmentConsultaUser fragment = new FragmentConsultaUser();
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
        View vista =inflater.inflate(R.layout.fragment_fragment_consulta_user, container, false);

        id = (TextView) vista.findViewById(R.id.TVID);
        nombre = (TextView) vista.findViewById(R.id.TVN);
        usuario = (TextView) vista.findViewById(R.id.TVU);
        pass = (TextView) vista.findViewById(R.id.TVPASS);
        turno = (TextView) vista.findViewById(R.id.TVT);
        salario = (TextView) vista.findViewById(R.id.TVS);

        username = (EditText) vista.findViewById(R.id.ETU);
        btnConsulta = (Button) vista.findViewById(R.id.btnBuscar);

        request = Volley.newRequestQueue(getContext());
        
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    webservice();
                }
            }
        });

        return vista;
    }

    private void webservice() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando Consulta...");
        progreso.show();


        String url = "http://"+ipPHP+"/BD_remota/wsJSONConsultaUser.php?Nom_usu="+username.getText().toString();

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
        String i="",n="",u="",c="",t="",s="";
        try {
            jsonObject= json.getJSONObject(0);
            i = jsonObject.optString("id_usuario");
            n = jsonObject.optString("nombre");
            u = jsonObject.optString("usuario");
            c = jsonObject.optString("contrasenia");
            t = jsonObject.optString("turno");
            s = jsonObject.optString("salario");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        id.setText("ID"+i);
        nombre.setText("Nombre "+n);
        usuario.setText("Usuario "+u);
        pass.setText("Contraseña "+c);
        turno.setText("Turno "+t);
        salario.setText("Salario "+s);
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
