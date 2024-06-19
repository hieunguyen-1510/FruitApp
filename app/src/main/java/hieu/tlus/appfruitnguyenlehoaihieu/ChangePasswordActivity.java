package hieu.tlus.appfruitnguyenlehoaihieu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText OldPassword, NewPassword, ConfirmPassword;
    Button btnChangePassword;
    DBHelperDatabaseFruit dbh;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbh = new DBHelperDatabaseFruit(this);
        username = getIntent().getStringExtra("usernameChange");
        Log.d("oke", "onCreate: "+username);
        OldPassword = findViewById(R.id.oldPassword);
        NewPassword = findViewById(R.id.newPassword);
        ConfirmPassword = findViewById(R.id.confirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = OldPassword.getText().toString();
                String newPass = NewPassword.getText().toString();
                String confirmPass = ConfirmPassword.getText().toString();

                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("click", "onClick: "+checkOldPassword(oldPass));
                if (checkOldPassword(oldPass)) {
                    updatePassword(newPass);
                    Toast.makeText(getApplicationContext(), "Đã thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkOldPassword(String oldPass) {
        String sql = "SELECT * FROM login WHERE username='" + username + "' AND password='" + oldPass + "'";
        SQLiteDatabase db = dbh.ketNoiDBRead();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exists = cursor.moveToFirst();
        Log.d("", "checkOldPassword: "+exists);
        cursor.close();
        return exists;
    }

    private void updatePassword(String newPass) {
        String sql = "UPDATE login SET password='" + newPass + "' WHERE username='" + username + "'";
        SQLiteDatabase db = dbh.ketNoiDBWrite();
        db.execSQL(sql);
    }
}
