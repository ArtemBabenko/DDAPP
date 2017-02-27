package ddapp_project.ddapp;

public class Student
{
    private String id;

    private String birthday;

    private String lastName;

    private Courses[] courses;

    private String firstName;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBirthday ()
    {
        return birthday;
    }

    public void setBirthday (String birthday)
    {
        this.birthday = birthday;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public Courses[] getCourses ()
    {
        return courses;
    }

    public void setCourses (Courses[] courses)
    {
        this.courses = courses;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", birthday = "+birthday+", lastName = "+lastName+", courses = "+courses+", firstName = "+firstName+"]";
    }
}