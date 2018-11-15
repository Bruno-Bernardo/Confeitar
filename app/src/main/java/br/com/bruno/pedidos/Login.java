package br.com.bruno.pedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;
    private TextView mTextMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTextMessage = (TextView) findViewById(R.id.message);

        findViewById(R.id.entrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ((EditText) (findViewById(R.id.login))).getText().toString().equals("Bruno")) {
                    startActivityForResult(new Intent(Login.this,
                            ListaPedidos.class), 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setCancelable(true);
                    builder.setTitle("Usuario não cadastrado");
                    builder.setMessage("Caso não possua uma conta cadastre-se");
                    builder.setPositiveButton("Confirmar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        findViewById(R.id.cadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Login.this,
                        CadastroLogin.class),0);
            }
        });
//        findViewById(R.id.limpar).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rodarNaThreadDoBanco(new Runnable() {
//                            @Override
//                            public void run() {
//                                Banco banco = Banco
//                                        .obterInstanciaUnica(Login.this);
//                                PedidoDao pedidos = banco.pedidos();
//                                pedidos.limpar();
//
//                                atualizar();
//                            }
//                        });
//                    }
//                }
//        );
//        atualizar();
    }
}
