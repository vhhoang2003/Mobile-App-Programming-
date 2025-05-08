public class StudentAdapter extends ArrayAdapter<Student> {
    public StudentAdapter(Context context, List<Student> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_student, parent, false);

        Student s = getItem(pos);
        ((TextView) convertView.findViewById(R.id.tvName)).setText(s.getName());
        ((TextView) convertView.findViewById(R.id.tvMssv)).setText(s.getMssv());
        ImageView img = convertView.findViewById(R.id.imgAvatar);
        Bitmap bm = BitmapFactory.decodeByteArray(s.getAvatar(), 0, s.getAvatar().length);
        img.setImageBitmap(bm);
        return convertView;
    }
}
