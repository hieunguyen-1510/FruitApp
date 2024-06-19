package hieu.tlus.appfruitnguyenlehoaihieu;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import hieu.tlus.appfruitnguyenlehoaihieu.model.Fruit;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    ArrayList<Fruit> arrFruits;
    AdapterFruit adapter;
    ListView lvFruits;
    DBHelperDatabaseFruit dbh;
    EditText txtsearch;
    Button btnThem, btnXoa, btnSua,btnLuu, btnExport;
    EditText txtId, txtName, txtSoluong, txtGia, txtNhacc, txtNgaymua;
    int flag = 0;
    SQLiteDatabase db;
    Cursor cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách trái cây");
        toolbar.setBackgroundColor(Color.parseColor("#E0FFFF"));
        setSupportActionBar(toolbar);
        dbh = new DBHelperDatabaseFruit(this);
        initView();
        cs = dbh.initRecordFirstDB();
        updateRecord();
        showDataListView();
    }

    private void showDialogconfirmLogOut(){
        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý đăng xuất không ?");
//      Nút Ok
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
            }
        });
//      Nút Cancel
        b.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
//      Tạo dialog
        AlertDialog al = b.create();
//      Hiển thị
        al.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.log_out){
            showDialogconfirmLogOut();
        }else if(item.getItemId() == R.id.action_changepassword){
            SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
            settings.edit().clear().commit();
            String username = getIntent().getStringExtra("username");
            Log.d("eror", "onOptionsItemSelected: "+username);
            Intent in = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            in.putExtra("usernameChange",username);
            startActivity(in);
        }else if(item.getItemId() == R.id.action_thongke){
            thongKe();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDataListView() {
        db = dbh.ketNoiDBRead();
        String s = txtsearch.getText().toString().trim();
        arrFruits = new ArrayList<>();
        String query = "SELECT * FROM FRUITS WHERE id LIKE ? OR name LIKE ? OR price LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + s + "%", "%" + s + "%", "%" + s + "%"});

        try {
            while (cursor.moveToNext()) {
                Fruit fruit = new Fruit(
                        cursor.getString(0),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Float.parseFloat(cursor.getString(3)),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                arrFruits.add(fruit);
            }
            adapter = new AdapterFruit(this, arrFruits);
            lvFruits.setAdapter(adapter);
        } finally {
            cursor.close();
        }
    }
    void initDB() {
        try {
            db = openOrCreateDatabase("fruits.db", MODE_PRIVATE, null);
            cs = db.rawQuery("SELECT * FROM FRUITS", null);
        } catch (Exception e) {
            finish();
        }
        cs.moveToNext();

        updateRecord();
    }

    void updateRecord() {
        txtId.setText(cs.getString(0));
        txtName.setText(cs.getString(1));
        txtSoluong.setText(cs.getString(2));
        txtGia.setText(cs.getString(3));
        txtNhacc.setText(cs.getString(4));
        txtNgaymua.setText(cs.getString(5));

    }

    void initView() {
        lvFruits = (ListView) findViewById(R.id.lvfruit);
        txtsearch = (EditText) findViewById(R.id.txts);
        btnThem = (Button) findViewById(R.id.btnthem);
        btnXoa =(Button)findViewById(R.id.btnxoa);
        btnSua =(Button) findViewById(R.id.btnsua);
        btnLuu = (Button) findViewById(R.id.btnluu);
        btnExport =(Button) findViewById(R.id.btnxuatcsv);
        txtId =(EditText) findViewById(R.id.edtid);
        txtName =(EditText) findViewById(R.id.edtname);
        txtSoluong =(EditText) findViewById(R.id.edtquantity);
        txtGia =(EditText) findViewById(R.id.edtprice);
        txtNhacc =(EditText) findViewById(R.id.edtsupplier);
        txtNgaymua = (EditText)findViewById(R.id.edtpurchaseDate);
        // Nút thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1; // Chế độ thêm
                resetView();
                txtId.requestFocus();
                btnThem.setEnabled(false);
                btnSua.setEnabled(true);
            }
        });
        // Nút sửa
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2; // Chế độ sửa
                btnThem.setEnabled(true);
                btnSua.setEnabled(false);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fruitid = txtId.getText().toString();
                String name = txtName.getText().toString();
                String quantity = txtSoluong.getText().toString();
                String price = txtGia.getText().toString();
                String supplier = txtNhacc.getText().toString();
                String purchaseDate = txtNgaymua.getText().toString();

                DBHelperDatabaseFruit dbHelper = new DBHelperDatabaseFruit(getApplicationContext());
                SQLiteDatabase db = dbHelper.ketNoiDBWrite();

                if (flag == 1) {
                    Log.d("Thao tác: ", "Thêm" + flag);
                    String sql = "INSERT INTO " + DBHelperDatabaseFruit.TABLE_FRUITS + " (name, quantity, price, supplier, purchaseDate) " +
                            "VALUES ('" + name + "', '" + quantity + "', '" + price + "', '" + supplier + "', '" + purchaseDate + "')";
                    try {
                        db.execSQL(sql);
                        dbHelper.close();
                        showDataListView();
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (flag == 2) {
                    Log.d("Thao tác: ", "Sửa");
                    String sql = "UPDATE " + DBHelperDatabaseFruit.TABLE_FRUITS + " SET name='" + name + "', quantity='" + quantity + "', " +
                            "price='" + price + "', supplier='" + supplier + "', purchaseDate='" + purchaseDate + "' " +
                            "WHERE id='" + fruitid + "'";
                    try {
                        db.execSQL(sql);
                        dbHelper.close();
                        showDataListView();
                        Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thao tác", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                showDialogConfirm(name);
            }
        });
        txtsearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    showDataListView();
                }
                return false;
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermissions();
            }
        });
        lvFruits.setOnItemClickListener((parent, view, position, id) -> {
            Fruit selectedFruit = arrFruits.get(position);
            txtId.setText(selectedFruit.getFruitid());
            txtName.setText(selectedFruit.getName());
            txtSoluong.setText(String.valueOf(selectedFruit.getQuantity()));
            txtGia.setText(String.valueOf(selectedFruit.getPrice()));
            txtNhacc.setText(selectedFruit.getSupplier());
            txtNgaymua.setText(selectedFruit.getPurchaseDate());
        });

    }
    private void thongKe() {
        db = dbh.ketNoiDBRead();
        String query = "SELECT name, SUM(quantity) as totalQuantity, SUM(price * quantity) as totalPrice FROM FRUITS GROUP BY name";
        Cursor cursor = db.rawQuery(query, null);

        try {
            StringBuilder thongKeResult = new StringBuilder("Thống kê trái cây:\n");
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int totalQuantity = cursor.getInt(cursor.getColumnIndex("totalQuantity"));
                @SuppressLint("Range") float totalPrice = cursor.getFloat(cursor.getColumnIndex("totalPrice"));
                thongKeResult.append("Tên: ").append(name).append(", Số lượng: ").append(totalQuantity).append(", Tổng giá: ").append(totalPrice).append("\n");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thống kê trái cây");
            builder.setMessage(thongKeResult.toString());
            builder.setPositiveButton("OK", null);
            builder.show();
        } finally {
            cursor.close();
        }
    }
    private void showDialogConfirm(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa " + name + " không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFruit(name);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteFruit(String name) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        int result = db.delete("FRUITS", "name=?", new String[]{name});
        if (result > 0) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            showDataListView();
            resetView();
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    private void resetView() {
        txtId.setText("");
        txtName.setText("");
        txtSoluong.setText("");
        txtGia.setText("");
        txtNhacc.setText("");
        txtNgaymua.setText("");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            showDataListView();
        }
    }
    private void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            exportToCSV();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền lưu trữ được cấp phép", Toast.LENGTH_SHORT).show();
                exportToCSV();
            } else {
                Toast.makeText(this, "Quyền lưu trữ bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exportToCSV() {
        String csvFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fruits.csv";
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("ID,Name,Quantity,Price,Supplier,Purchase Date\n");
            for (Fruit fruit : arrFruits) {
                writer.append(fruit.getFruitid()).append(",");
                writer.append(fruit.getName()).append(",");
                writer.append(String.valueOf(fruit.getQuantity())).append(",");
                writer.append(String.valueOf(fruit.getPrice())).append(",");
                writer.append(fruit.getSupplier()).append(",");
                writer.append(fruit.getPurchaseDate()).append("\n");
            }
            writer.flush();
            Toast.makeText(MainActivity.this, "Xuất csv thành công: " + csvFile, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Xuất csv thất bại:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


