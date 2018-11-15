package br.com.bruno.pedidos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VisualizarPedido extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pedido);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        findViewById(R.id.voltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rodarNaThreadDoBanco(new Runnable() {
            @Override
            public void run() {
                Banco banco = Banco
                        .obterInstanciaUnica(VisualizarPedido.this);
                PedidoDao pedidos = banco.pedidos();


                int id = getIntent().getIntExtra("Id",0);
                Pedido pedido = new Pedido();
                if(id >= 0) {
                    pedido = pedidos.listar().get(id);
                }else{
                    pedido.setCliente_nome("");
                    pedido.setCobertura("");
                    pedido.setHorario("");
                    pedido.setMassa("");
                }
                TextView nome = findViewById(R.id.nome);
                TextView cobertura = findViewById(R.id.senha);
                TextView massa = findViewById(R.id.massa);
                TextView horario = findViewById(R.id.horario);

                nome.setText("Nome do Cliente: " + pedido.getCliente_nome());
                cobertura.setText("Cobertura: " + pedido.getCobertura());
                massa.setText("Massa: "+ pedido.getMassa());
                horario.setText("Horário: " + pedido.getHorario());
            }
        });



    }


    void rodarNaThreadPrincipal(Runnable acao) {
        handlerThreadPrincipal.post(acao);
    }

    void rodarNaThreadDoBanco(Runnable acao) {
        executorThreadDoBanco.execute(acao);
    }

}
