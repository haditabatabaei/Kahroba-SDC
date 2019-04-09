import java.io.Serializable;

public class Experience implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String company;

    public Experience(String title, String company) {
        this.title = title;
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String toString(){
        return (title + " AT " + company);
    }

    public void print(){
        System.out.println(toString());
    }
}
