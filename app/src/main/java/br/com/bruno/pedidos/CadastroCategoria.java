package br.com.bruno.pedidos;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CadastroCategoria extends AppCompatActivity {
    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_categoria);

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
                EditText descricao = findViewById(R.id.senha);

                final Categoria c = new Categoria();
                c.setNome(nome.getText().toString());
                c.setDescricao(descricao.getText().toString());

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        Banco banco = Banco
                                .obterInstanciaUnica(br.com.bruno.pedidos.CadastroCategoria.this);
                        CategoriaDao categoria = banco.categorias();
                        categoria.adicionar(c);

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

