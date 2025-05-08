public class StudentDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_student_detail);

        int id = getIntent().getIntExtra("id", -1);
        StudentDatabaseHelper db = new StudentDatabaseHelper(this);
        Student s = db.getStudentById(id);

        ((TextView) findViewById(R.id.tvNameDetail)).setText(s.getName());
        ((TextView) findViewById(R.id.tvMssvDetail)).setText(s.getMssv());
        Bitmap bm = BitmapFactory.decodeByteArray(s.getAvatar(), 0, s.getAvatar().length);
        ((ImageView) findViewById(R.id.imgAvatarDetail)).setImageBitmap(bm);
    }
}
