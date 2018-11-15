package br.com.bruno.pedidos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by bbbru on 10/05/2018.
 */

@Dao
public interface ItensDao {

    @Insert
    void adicionar(Item i);

    @Query("SELECT * from Item")
    List<Item> listar();

    @Query("SELECT count(*) from Item")
    int quantidade();

    @Query("SELECT * from Item where id = :id")
    Item itemPorId(int id);

    @Query("DELETE from Item")
    void limpar();

}
