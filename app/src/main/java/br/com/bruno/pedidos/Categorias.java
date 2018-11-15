package br.com.bruno.pedidos;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Categorias extends AppCompatActivity implements Serializable {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;
    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        mTextMessage = (TextView) findViewById(R.id.message);

        findViewById(R.id.cadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Categorias.this,
                        CadastroCategoria.class),0);
            }
        });
        findViewById(R.id.limpar).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rodarNaThreadDoBanco(new Runnable() {
                            @Override
                            public void run() {
                                Banco banco = Banco
                                        .obterInstanciaUnica(Categorias.this);
                                CategoriaDao categorias = banco.categorias();
                                categorias.limpar();

                                atualizar();
                            }
                        });
                    }
                }
        );
        atualizar();

    }

    void atualizar() {
        rodarNaThreadDoBanco(new Runnable() {
            @Override
            public void run() {
                Banco banco = Banco
                        .obterInstanciaUnica(Categorias.this);
                CategoriaDao categorias = banco.categorias();

                final int quantidade = categorias.quantidade();
                final List<Categoria> lista = categorias.listar();


                rodarNaThreadPrincipal(new Runnable() {
                    @Override
                    public void run() {
                        ListView listaCategorias = findViewById(R.id.visaoListaCategorias);
                        TextView quantidadeDeCategorias = findViewById(R.id.quantidadeDeItens);

                        quantidadeDeCategorias.setText("Quantidade de Categorias: " + quantidade);

                        ArrayAdapter<Categoria> adaptador = new ArrayAdapter<>(
                                Categorias.this,
                                android.R.layout.simple_list_item_1,
                                lista);
                        listaCategorias.setAdapter(adaptador);
                        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent objetoVisualizar = new Intent(view.getContext(), VisualizarCategoria.class);
                                objetoVisualizar.putExtra("Id", i);
                                startActivity(objetoVisualizar);
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        atualizar();
    }

    void rodarNaThreadPrincipal(Runnable acao) {
        handlerThreadPrincipal.post(acao);
    }

    void rodarNaThreadDoBanco(Runnable acao) {
        executorThreadDoBanco.execute(acao);
    }
}
