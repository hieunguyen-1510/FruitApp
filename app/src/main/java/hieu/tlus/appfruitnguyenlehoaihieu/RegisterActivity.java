package hieu.tlus.appfruitnguyenlehoaihieu;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    DBHelperDatabaseFruit dbh;
    RadioButton rdMen, rdWomen;
    Button btnSignUp;
    EditText txtusername, txtpassword, txtfullname;
    String valueBirth;

    Spinner spbirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbh =new DBHelperDatabaseFruit(this);
        rdMen = findViewById(R.id.rdnam);
        rdWomen = findViewById(R.id.rdnu);
        btnSignUp = findViewById(R.id.btntao);
        txtusername = findViewById(R.id.edtuser);
        txtpassword = findViewById(R.id.edtpass);
        txtfullname = findViewById(R.id.edtfullname);
        spbirth = findViewById(R.id.spnamsinh);

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1990; i<= 2030; i++){
            arrayList.add(""+i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbirth.setAdapter(arrayAdapter);

        rdMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdWomen.setChecked(false);
            }
        });

        rdWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdMen.setChecked(false);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String user = txtusername.getText().toString();
                    String pass = txtpassword.getText().toString();
                    String fullname = txtfullname.getText().toString();
                    String gender = "Nam";
                    if(rdWomen.isChecked()){
                        gender = "Nu";
                    }

                    String sql = "insert into login values('"+user+"','"+pass+"','"+fullname+"','"+gender+"', '"+valueBirth+"')";
                    SQLiteDatabase db = dbh.ketNoiDBWrite();
                    db.execSQL(sql);
                    Log.d("sql", "sql: "+sql);
                    finish();
                }catch (Exception e) {
                    Log.d("error", "error: " + e);
                }
            }
        });

        spbirth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueBirth = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}