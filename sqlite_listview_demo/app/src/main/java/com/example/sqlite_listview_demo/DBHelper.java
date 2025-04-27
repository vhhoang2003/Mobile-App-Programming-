package com.example.sqlite_listview_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB"; // Tên file database
    private static final int DATABASE_VERSION = 1; // Phiên bản DB (tăng số này khi thay đổi cấu trúc bảng)
    private static final String TABLE_NAME = "students"; // Tên bảng
    private static final String COLUMN_ID = "id"; // Cột ID (khóa chính, tự tăng)
    private static final String COLUMN_NAME = "name"; // Cột lưu tên sinh viên

    // Lớp DBHelper kế thừa SQLiteOpenHelper để tạo và quản lý CSDL SQLite
    public DBHelper(Context context) {
        // Gọi constructor cha để tạo hoặc mở database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Lệnh SQL tạo bảng students với 2 cột: id (int, tự tăng), name (text)
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        // Xóa bảng nếu tồn tại khi truy cập DB
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Gọi lại onCreate để tạo bảng mới
        onCreate(db);
    }

    public void insertStudent(String name) {
        // Mở DB ở chế độ ghi
        SQLiteDatabase db = this.getWritableDatabase();

        // Tạo đối tượng ContentValues để lưu giá trị
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name); // Gán giá trị cho cột name

        db.insert(TABLE_NAME, null, cv); // Thêm dữ liệu vào bảng

        db.close(); // Đóng DB sau khi thao tác xong
    }

    public ArrayList<String> getAllStudents() {
        ArrayList<String> list = new ArrayList<>(); // Tạo danh sách kết quả

        SQLiteDatabase db = this.getReadableDatabase(); // Mở DB ở chế độ đọc

        // Truy vấn tất cả bản ghi trong bảng students
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // Nếu có dữ liệu, duyệt từng dòng và thêm vào danh sách
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            } while (cursor.moveToNext());
        }

        // Đóng cursor và DB sau khi truy vấn
        cursor.close();
        db.close();

        return list;// Trả về danh sách tên sinh viên
    }
}
