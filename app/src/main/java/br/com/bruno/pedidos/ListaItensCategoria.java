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

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListaItensCategoria extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;
    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_itens_categoria);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        mTextMessage = (TextView) findViewById(R.id.message);

        findViewById(R.id.cadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListaItensCategoria.this,
                        CadastroItem.class),0);
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
                                        .obterInstanciaUnica(ListaItensCategoria.this);
                                ItensDao itens = banco.itens();
                                itens.limpar();

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
                        .obterInstanciaUnica(ListaItensCategoria.this);
                ItensDao itens = banco.itens();

                final int quantidade = itens.quantidade();
                final List<Item> lista = itens.listar();


                rodarNaThreadPrincipal(new Runnable() {
                    @Override
                    public void run() {
                        ListView listaItens = findViewById(R.id.visaoListaItens);
                        TextView quantidadeDeItens = findViewById(R.id.quantidadeDeItens);

                        quantidadeDeItens.setText("Quantidade de Itens: " + quantidade);

                        ArrayAdapter<Item> adaptador = new ArrayAdapter<>(
                                ListaItensCategoria.this,
                                android.R.layout.simple_list_item_1,
                                lista);
                        listaItens.setAdapter(adaptador);
                        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
