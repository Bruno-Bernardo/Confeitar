package br.com.bruno.pedidos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by bbbru on 10/05/2018.
 */

@Dao
public interface CategoriaDao {

    @Insert
    void adicionar(Categoria c);

    @Query("SELECT * from Categoria")
    List<Categoria> listar();

    @Query("SELECT count(*) from Categoria")
    int quantidade();

    @Query("SELECT * from Categoria where id = :id")
    Categoria categoriaPorId(int id);

    @Query("DELETE from Categoria")
    void limpar();

}
