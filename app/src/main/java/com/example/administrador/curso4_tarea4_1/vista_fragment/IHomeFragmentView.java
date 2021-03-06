package com.example.administrador.curso4_tarea4_1.vista_fragment;

import com.example.administrador.curso4_tarea4_1.adapter.MascotaAdapter;
import com.example.administrador.curso4_tarea4_1.pojo.Mascota;

import java.util.ArrayList;

/**
 * Created by administrador on 27/05/17.
 */

public interface IHomeFragmentView {

    public void generarLinearLayoutVertical();

    public MascotaAdapter crearAdaptador(ArrayList<Mascota> mascotas);

    public void inicializarAdaptadorRV(MascotaAdapter adaptador);
}
