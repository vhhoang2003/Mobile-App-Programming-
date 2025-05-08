public class Student {
    private int id;
    private String name;
    private String mssv;
    private byte[] avatar; // Lưu ảnh dưới dạng byte array

    public Student(int id, String name, String mssv, byte[] avatar) {
        this.id = id;
        this.name = name;
        this.mssv = mssv;
        this.avatar = avatar;
    }

    // Getter + Setter
}
