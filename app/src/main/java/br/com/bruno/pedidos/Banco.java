package br.com.bruno.pedidos;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by bbbru on 10/05/2018.
 */

@Database(entities = {Pedido.class,Categoria.class,Item.class},
        version = 1, exportSchema = false)
public abstract class Banco extends RoomDatabase {

    private static Banco instancia;

    public static Banco obterInstanciaUnica(
            Context context) {
        synchronized (Banco.class) {
            if (instancia == null)
                instancia = Room.databaseBuilder(
                        context.getApplicationContext(),
                        Banco.class,
                        "banco").build();

            return instancia;
        }
    }

    public abstract PedidoDao pedidos();

    public abstract CategoriaDao categorias();

    public abstract ItensDao itens();
}
