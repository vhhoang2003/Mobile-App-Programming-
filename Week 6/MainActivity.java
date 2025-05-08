public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<Student> students;
    StudentDatabaseHelper db;
    long lastClickTime = 0;
    int lastClickPosition = -1;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        db = new StudentDatabaseHelper(this);
        listView = findViewById(R.id.listView);

        students = db.getAllStudents();
        listView.setAdapter(new StudentAdapter(this, students));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            long currentTime = System.currentTimeMillis();
            if (position == lastClickPosition && currentTime - lastClickTime < 500) {
                Intent i = new Intent(MainActivity.this, StudentDetailActivity.class);
                i.putExtra("id", students.get(position).getId());
                startActivity(i);
            }
            lastClickTime = currentTime;
            lastClickPosition = position;
        });
    }
}
