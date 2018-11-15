package br.com.bruno.pedidos;

import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CadastroItem extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_item);

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
                EditText categoria = findViewById(R.id.Password);

                final Item i = new Item();
                i.setNome(nome.getText().toString());
                i.setDescricao(descricao.getText().toString());
                i.setCategoria(categoria.getText().toString());

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        Banco banco = Banco
                                .obterInstanciaUnica(CadastroItem.this);
                        ItensDao itens = banco.itens();
                        itens.adicionar(i);

                        finish();
                    }
                });
            }
        });
        rodarNaThreadPrincipal(new Runnable() {
            @Override
            public void run() {
                Spinner categorias = findViewById(R.id.spinner);
                SpinnerAdapter sa = new SpinnerAdapter() {
                    @Override
                    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
                        return null;
                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

                    }

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

                    }

                    @Override
                    public int getCount() {
                        return 0;
                    }

                    @Override
                    public Object getItem(int i) {
                        return null;
                    }

                    @Override
                    public long getItemId(int i) {
                        return 0;
                    }

                    @Override
                    public boolean hasStableIds() {
                        return false;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        return null;
                    }

                    @Override
                    public int getItemViewType(int i) {
                        return 0;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                };
                categorias.setAdapter(sa);
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
