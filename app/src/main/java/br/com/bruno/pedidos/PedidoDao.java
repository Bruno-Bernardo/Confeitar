package br.com.bruno.pedidos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by bbbru on 10/05/2018.
 */

@Dao
public interface PedidoDao {

    @Insert
    void adicionar(Pedido p);

    @Query("SELECT * from Pedido")
    List<Pedido> listar();

    @Query("SELECT count(*) from Pedido")
    int quantidade();

    @Query("SELECT * from Pedido where id = :id")
    Pedido pedidoPorId(int id);

    @Query("DELETE from Pedido")
    void limpar();

}
