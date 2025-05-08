public class StudentDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "students.db";
    private static final int DB_VERSION = 1;

    public StudentDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mssv TEXT, avatar BLOB)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS student");
        onCreate(db);
    }

    public void insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("mssv", student.getMssv());
        values.put("avatar", student.getAvatar());
        db.insert("student", null, values);
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM student", null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String mssv = c.getString(2);
            byte[] avatar = c.getBlob(3);
            list.add(new Student(id, name, mssv, avatar));
        }
        c.close();
        return list;
    }

    public Student getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM student WHERE id = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            return new Student(c.getInt(0), c.getString(1), c.getString(2), c.getBlob(3));
        }
        return null;
    }
}
