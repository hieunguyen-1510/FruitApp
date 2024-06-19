package hieu.tlus.appfruitnguyenlehoaihieu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText txtUsername;
    Button btnSubmit;
    TextView txtpassWord;
    DBHelperDatabaseFruit dbh;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        txtUsername = findViewById(R.id.txtusername);
        btnSubmit = findViewById(R.id.btnsubmit);
        txtpassWord = findViewById(R.id.txtpass);
        dbh = new DBHelperDatabaseFruit(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString().trim();
                if (!username.isEmpty()) {
                    String password = getPasswordByUsername(username);
                    if (password != null) {
                        txtpassWord.setText("Mật khẩu của bạn là: " + password);
//                        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
//                        startActivity(in);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getPasswordByUsername(String username) {
        String sql = "SELECT password FROM login WHERE username='" + username + "'";
        SQLiteDatabase db = dbh.ketNoiDBRead();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        }
        cursor.close();
        return null;
    }
}
