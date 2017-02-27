package ddapp_project.ddapp;

public class Courses
{
    private String name;

    private String mark;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMark ()
    {
        return mark;
    }

    public void setMark (String mark)
    {
        this.mark = mark;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", mark = "+mark+"]";
    }
}
