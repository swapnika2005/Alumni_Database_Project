package alumni.model;

public class Alumni {
    private int id;
    private String name;
    private String email;
    private int gradYear;
    private String department;
    private String company;
    private String position;
    private String location;
    private String mobile;
    private String maritalStatus;
    private String photoPath;

    // Default constructor
    public Alumni() {}

    // Constructor for new alumni (without ID)
    public Alumni(String name, String email, int gradYear, String department,
                  String company, String position, String location,
                  String mobile, String maritalStatus, String photoPath) {
        this.name = name;
        this.email = email;
        this.gradYear = gradYear;
        this.department = department;
        this.company = company;
        this.position = position;
        this.location = location;
        this.mobile = mobile;
        this.maritalStatus = maritalStatus;
        this.photoPath = photoPath;
    }

    // Constructor for existing alumni (with ID)
    public Alumni(int id, String name, String email, int gradYear, String department,
                  String company, String position, String location,
                  String mobile, String maritalStatus, String photoPath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gradYear = gradYear;
        this.department = department;
        this.company = company;
        this.position = position;
        this.location = location;
        this.mobile = mobile;
        this.maritalStatus = maritalStatus;
        this.photoPath = photoPath;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getGradYear() { return gradYear; }
    public void setGradYear(int gradYear) { this.gradYear = gradYear; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    @Override
    public String toString() {
        return "Alumni{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gradYear=" + gradYear +
                ", department='" + department + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", location='" + location + '\'' +
                ", mobile='" + mobile + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }
}
