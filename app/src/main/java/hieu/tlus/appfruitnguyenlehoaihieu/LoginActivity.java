package hieu.tlus.appfruitnguyenlehoaihieu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    DBHelperDatabaseFruit dbh;
    SharedPreferences settings;
    TextView txtSignUp,txtforgotPassword;
    CheckBox ck;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CheckBox ck;
        EditText txtusername,txtpw;
        Button btnLogin,btnthoat;
        CheckBox ckremember;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ck = findViewById(R.id.ckremember);
        txtusername = findViewById(R.id.txtuser);
        txtpw = findViewById(R.id.txtpass);
        btnLogin = findViewById(R.id.btnlogin);
        txtSignUp = findViewById(R.id.txtregister);
        txtforgotPassword = findViewById(R.id.txtquenmatkhau);
        ckremember = findViewById(R.id.ckremember);
        dbh =new DBHelperDatabaseFruit(this);

        settings = getSharedPreferences("Login", 0);
        String username = settings.getString("user", "");
        String password = settings.getString("pass", "");
        if(username.isEmpty()){

        }else{
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u=txtusername.getText().toString();
                String p=txtpw.getText().toString();
                String sql="select * from login where username='"+u+"' and password='"+p+"'";
                SQLiteDatabase db=dbh.ketNoiDBRead();
                Cursor cs = db.rawQuery(sql, null);
                if(cs.moveToNext()){
                    if(ckremember.isChecked()){
                        settings.edit().putString("user", u).apply();
                        settings.edit().putString("pass", p).apply();
                    }

                    Intent in = new Intent(getApplicationContext(),MainActivity.class);
                    in.putExtra("username",u);
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(),"Tài khoản chưa đúng", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(in);
            }
        });
        txtforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(in);
            }
        });

    }
}