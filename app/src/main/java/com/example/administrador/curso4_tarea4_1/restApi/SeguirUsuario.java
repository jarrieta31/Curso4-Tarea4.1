package com.example.administrador.curso4_tarea4_1.restApi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.administrador.curso4_tarea4_1.Activity.Activity2;
import com.example.administrador.curso4_tarea4_1.Activity.MainActivity;
import com.example.administrador.curso4_tarea4_1.restApi.adapter.RestApiAdapter;
import com.example.administrador.curso4_tarea4_1.restApi.model.FollowResponseInstagram;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by administrador on 28/10/17.
 */

public class SeguirUsuario extends BroadcastReceiver {

    DatosPreferencias datosPreferencias;
    Handler mhandler=new Handler();
    String estadoSaliente;
    String estadoEntrante;
    String usuarioObjetivoPrivado;
    private static final String TAG = "SeguirUsuario";
    public String idUsuarioInstagram;
    public String usuarioInstagram = "Juan";
    public String tokenInstagram = ConstantesRestApi.ACCESS_TOKEN;
    public String accion = "5557461888";
    public Context miContexto;


    @Override
    public void onReceive(Context context, Intent intent) {
        String KEY_ACTION_FOLLOW = "SEGUIR_USUARIO";
        String accionIntent = intent.getAction();
        miContexto = context;
//        idUsuarioInstagram = datosPreferencias.getIdUsuarioApi();
//        usuarioInstagram = datosPreferencias.getUsuarioApi();

        //****** Seguir o dejar de seguir a un usuario en instagram *****************
        if (KEY_ACTION_FOLLOW.equals(accionIntent)){// Chequea el codigo de la acción recibida
            //Toast.makeText(context, "Activaste seguir al usuario", Toast.LENGTH_SHORT).show();
            checkStatusFollow(idUsuarioInstagram, tokenInstagram);  // Consulta si se está siguiendo a ese usuario

            mhandler.postDelayed(new Runnable() { // Espera 1 segundo para recibir el estado y actuar en función
                @Override
                public void run() {
                    if (estadoSaliente.equalsIgnoreCase("follows")){ // Si se lo esta siguiendo se lo deja de seguir
                        accion = "unfollow";
                        followUnfollow(idUsuarioInstagram, tokenInstagram, accion);
                        Toast toast1 = Toast.makeText( miContexto ,"Se ha dejado de seguir al usuario " + usuarioInstagram,
                                Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);
                        toast1.show();
                    }
                    else if (estadoSaliente.equalsIgnoreCase("none")){
                        accion = "follow";
                        followUnfollow(idUsuarioInstagram, tokenInstagram, accion);
                        Toast toast2 = Toast.makeText( miContexto ,"Se ha comensado a seguir al usuario " + usuarioInstagram,
                                Toast.LENGTH_LONG);
                        toast2.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);
                        toast2.show();
                    }else {
                        Toast toast3 = Toast.makeText( miContexto ,"Huvo un error al intentar seguir al usuario " + usuarioInstagram,
                                Toast.LENGTH_LONG);
                        toast3.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);
                        toast3.show();
                    }
                }
            },1800);
        }
    }


    //*** Método para chequear si se está seguiendo a un usuario de instagram  ****
    private String checkStatusFollow (String idUsuarioInstagram, String tokenInstagram){

     //   public String estadoSaliente;
        RestApiAdapter restApiAdapter = new RestApiAdapter(); // Instancia un objeto restApiAdapter
        Gson gsonFollow = restApiAdapter.construyeGesonDeserializadorFollow();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonFollow); // Crea la endpoint con la conexion a usar
        Call<FollowResponseInstagram> followResponseInstagramCall = endpointsApi.querySeguirUsuarioInstagram(idUsuarioInstagram, tokenInstagram);

        followResponseInstagramCall.enqueue(new Callback<FollowResponseInstagram>() {
            @Override
            public void onResponse(Call<FollowResponseInstagram> call, Response<FollowResponseInstagram> response) {

                FollowResponseInstagram followResponseInstagram = response.body();
                if (followResponseInstagram != null){
                    estadoSaliente = followResponseInstagram.getEstadoSaliente();
                    estadoEntrante = followResponseInstagram.getEstadoEntrante();
                    usuarioObjetivoPrivado = followResponseInstagram.getUsuarioObjetivoPrivado();
                    Log.d(TAG, "CheckStatusFollow está funcionando OK");
                }
            }

            @Override
            public void onFailure(Call<FollowResponseInstagram> call, Throwable t) {//
                Log.e(TAG, t.toString());
                Log.d(TAG, "Error en el método CheckStatusFollow");
            }
        });

        return estadoSaliente;
    }

    //*** Método para seguir a un usuario de instagram  ****
    private void followUnfollow (String idUsuarioInstagram, String tokenInstagram, String accion){
        RestApiAdapter restApiAdapter = new RestApiAdapter(); // Instancia un objeto restApiAdapter
        Gson gsonFollow = restApiAdapter.construyeGesonDeserializadorFollow();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonFollow); // Crea la endpoint con la conexion a usar
        Call<FollowResponseInstagram> followResponseInstagramCall = endpointsApi.seguirUsuarioInstagram(idUsuarioInstagram,tokenInstagram, accion);

        followResponseInstagramCall.enqueue(new Callback<FollowResponseInstagram>() {
            @Override
            public void onResponse(Call<FollowResponseInstagram> call, Response<FollowResponseInstagram> response) {
                FollowResponseInstagram followResponseInstagram = response.body();
                if (followResponseInstagram != null){
                    Log.d(TAG, "¡El estado de seguimiento a cambiado! OK");
                //    Toast.makeText(getApplitacionContext() , "Has comensado a seguir a " + followResponseInstagram.getEstadoSaliente(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FollowResponseInstagram> call, Throwable t) {
//                Toast.makeText(context, "¡Algo pasó al dar seguir al usuario! Verifica la conexión", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
                Log.d(TAG, "Error en el método followUnfollow");
            }
        });
    }

}
