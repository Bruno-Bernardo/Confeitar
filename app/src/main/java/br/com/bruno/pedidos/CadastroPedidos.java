package br.com.bruno.pedidos;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CadastroPedidos extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedidos);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        findViewById(R.id.voltar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                finish();
             }
         });

        findViewById(R.id.salvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nome = findViewById(R.id.nome);
                EditText cobertura = findViewById(R.id.senha);
                EditText massa = findViewById(R.id.massa);
                EditText horario = findViewById(R.id.horario);

                final Pedido p = new Pedido();
                p.setCliente_nome(nome.getText().toString());
                p.setCobertura(cobertura.getText().toString());
                p.setMassa(massa.getText().toString());
                p.setHorario(horario.getText().toString());

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        Banco banco = Banco
                                .obterInstanciaUnica(CadastroPedidos.this);
                        PedidoDao pedidos = banco.pedidos();
                        pedidos.adicionar(p);

                        finish();
                    }
                });
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
