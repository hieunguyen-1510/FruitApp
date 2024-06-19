package hieu.tlus.appfruitnguyenlehoaihieu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperDatabaseFruit extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "managefruits.db";
    private static final int DATABASE_VERSION = 61;
    static final String TABLE_FRUITS = "fruits";
    private static final String TABLE_LOGIN = "login";

    public DBHelperDatabaseFruit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqltext = "CREATE TABLE " + TABLE_FRUITS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "supplier TEXT, " +
                "purchaseDate TEXT, " +
                "fruitimage TEXT);\n" +
                "INSERT INTO " + TABLE_FRUITS + " (name, quantity, price, supplier, purchaseDate, fruitimage) VALUES " +
                "('Apple', 50, 0.99, 'Supplier A', '2023-06-14', '');\n" +
                "INSERT INTO " + TABLE_FRUITS + " (name, quantity, price, supplier, purchaseDate, fruitimage) VALUES " +
                "('Banana', 30, 0.50, 'Supplier B', '2023-06-15', '');\n" +
                "INSERT INTO " + TABLE_FRUITS + " (name, quantity, price, supplier, purchaseDate, fruitimage) VALUES " +
                "('Cherry', 20, 2.00, 'Supplier C', '2023-06-16', '');\n" +
                "INSERT INTO " + TABLE_FRUITS + " (name, quantity, price, supplier, purchaseDate, fruitimage) VALUES " +
                "('Orange', 20, 2.00, 'Supplier C', '2023-05-16', '');";

        for (String sql : sqltext.split("\n")) {
            db.execSQL(sql);
        }

        String sqlLogin = "CREATE TABLE login(username text PRIMARY KEY, password text, fullname text, sex text, date_of_birth int);";
        db.execSQL(sqlLogin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xoá bảng cũ (nếu tồn tại)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRUITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Tạo lại cấu trúc cơ sở dữ liệu mới
        onCreate(db);
    }

    Cursor initRecordFirstDB() {
        Cursor cs = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cs = db.rawQuery("SELECT * FROM " + TABLE_FRUITS, null);
            if (cs != null && cs.moveToFirst()) {
                cs.moveToNext();
                return cs;
            }
        } catch (Exception io) {

        }
        return null;
    }

    SQLiteDatabase ketNoiDBRead() {
        SQLiteDatabase db = getReadableDatabase();
        return db;
    }
    SQLiteDatabase ketNoiDBWrite() {
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }
}
