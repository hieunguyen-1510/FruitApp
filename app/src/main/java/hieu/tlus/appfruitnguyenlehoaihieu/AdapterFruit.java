package hieu.tlus.appfruitnguyenlehoaihieu;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import hieu.tlus.appfruitnguyenlehoaihieu.model.Fruit;

import java.util.ArrayList;

public class AdapterFruit extends ArrayAdapter<Fruit> {
    private ArrayList<Fruit> arrfruit;
    private final Activity context;
    private int lastPosition = -1;
    public AdapterFruit(Activity context, ArrayList<Fruit> arrfruits) {
        super(context, R.layout.item_fruit, arrfruits);
        this.context = context;
        this.arrfruit = arrfruits;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_fruit, null, true);
        Fruit fruit = arrfruit.get(position);

        TextView txtma = rowView.findViewById(R.id.txtmatc);
        ImageView imgfruit = rowView.findViewById(R.id.imgfruit);
        TextView txtten = rowView.findViewById(R.id.txttentc);
        TextView txtgia = rowView.findViewById(R.id.txtgiatc);

        txtma.setText(fruit.getFruitid());
        txtten.setText(fruit.getName());
        txtgia.setText(String.valueOf(fruit.getPrice()));
        imgfruit.setImageBitmap(StringToBitMap(fruit.getAnhfruit()));;
        Log.d("chuoihinhanh",""+StringToBitMap(fruit.getAnhfruit()));

        Button btnchitiet = rowView.findViewById(R.id.btnchitiet);
        btnchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DeTailActivityFruit.class);
                in.putExtra("ObjectDetails", fruit);
                context.startActivityForResult(in, 200);
                //context.startActivity(in);

            }
        });

        Button btndelete = rowView.findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirm(fruit, position);
            }
        });
        lastPosition = position;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtId = rowView.getRootView().findViewById(R.id.edtid);
                EditText txtName = rowView.getRootView().findViewById(R.id.edtname);
                EditText txtSoluong = rowView.getRootView().findViewById(R.id.edtquantity);
                EditText txtGia = rowView.getRootView().findViewById(R.id.edtprice);
                EditText txtNhacc = rowView.getRootView().findViewById(R.id.edtsupplier);
                EditText txtNgaymua = rowView.getRootView().findViewById(R.id.edtpurchaseDate);

                txtId.setText(arrfruit.get(position).getFruitid());
                txtName.setText(arrfruit.get(position).getName());
                txtSoluong.setText(""+arrfruit.get(position).getQuantity());
                txtGia.setText(""+arrfruit.get(position).getPrice());
                txtNhacc.setText("" + arrfruit.get(position).getSupplier());
                txtNgaymua.setText(arrfruit.get(position).getPurchaseDate());
            }
        });

        return rowView;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void showDialogConfirm(Fruit fruit, int position) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý xóa mục " + fruit.getName() + " này không?");
        b.setPositiveButton("Đồng ý", (dialog, id) -> deleteFruit(fruit.getName(), position));
        b.setNegativeButton("Không đồng ý", (dialog, id) -> dialog.cancel());
        AlertDialog al = b.create();
        al.show();
    }

    private void deleteFruit(String fruitName, int position) {
        try {

            arrfruit.remove(position);
            notifyDataSetChanged(); // Cập nhật ListView
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Không thể xóa mặt hàng", Toast.LENGTH_SHORT).show();
        }
    }
}