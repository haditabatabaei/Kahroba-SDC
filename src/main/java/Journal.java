import java.io.Serializable;
import java.util.ArrayList;

public class Journal implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;
    private ArrayList<String> authors;
    private String dateStr;

    public Journal(String title, String url, ArrayList<String> authors,String dateStr) {
        this.title = title;
        this.url = url;
        this.authors = authors;
        this.dateStr = dateStr;
    }

    public String getTitle() {
        return title;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder = builder.append("title:" + title + "\nURL:" + url + "\nDATE:" + dateStr + "\n");
        for(String authorName : authors){
            builder = builder.append("AUTHOR #" + authors.indexOf(authorName) + " : " + authorName + "\n");
        }
        return builder.toString();
    }

    public void print(){
        System.out.println(toString());
    }

}
