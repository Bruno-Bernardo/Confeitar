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

public class ListaPedidos extends AppCompatActivity implements Serializable{

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;
    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        mTextMessage = (TextView) findViewById(R.id.message);




        findViewById(R.id.cadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListaPedidos.this,
                        CadastroPedidos.class),0);
            }
        });
        findViewById(R.id.categorias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListaPedidos.this,
                        Categorias.class),0);
            }
        });
        findViewById(R.id.itens).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ListaPedidos.this,
                        ListaItensCategoria.class),0);
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
                                    .obterInstanciaUnica(ListaPedidos.this);
                            PedidoDao pedidos = banco.pedidos();
                            pedidos.limpar();

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
                        .obterInstanciaUnica(ListaPedidos.this);
                PedidoDao pedidos = banco.pedidos();

                final int quantidade = pedidos.quantidade();
                final List<Pedido> lista = pedidos.listar();


                rodarNaThreadPrincipal(new Runnable() {
                    @Override
                    public void run() {
                        ListView listaPedidos = findViewById(R.id.visaoLista);
                        TextView quantidadeDePedidos = findViewById(R.id.quantidadeDeItens);

                        quantidadeDePedidos.setText("Quantidade de Pedidos: " + quantidade);

                        ArrayAdapter<Pedido> adaptador = new ArrayAdapter<>(
                                ListaPedidos.this,
                                android.R.layout.simple_list_item_1,
                                lista);
                        listaPedidos.setAdapter(adaptador);
                        listaPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent objetoVisualizar = new Intent(view.getContext(), VisualizarPedido.class);
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
