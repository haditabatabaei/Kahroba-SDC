import java.io.Serializable;

public class University implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String degree;
    private String fieldOfStudy;
    private String date;

    public University(String name, String degree, String fieldOfStudy, String date) {
        this.name = name;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.date = date;
    }

    public University(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString(){
        return name + " | Degree : " + degree + " | study field : " + fieldOfStudy + " | date : " + date;
    }

    public void print(){
        System.out.println(toString());
    }
}
