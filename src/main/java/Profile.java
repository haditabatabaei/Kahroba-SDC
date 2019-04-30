import org.checkerframework.checker.formatter.FormatUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

public class Profile implements Serializable {
    //default serialVersion id
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String fullName;
    private String middleName;
    private String email;
    private String phone;
    private ArrayList<Journal> journals;
    private ArrayList<University> universities;
    private ArrayList<String> skills;
    private ArrayList<Experience> experiences;
    private ArrayList<String> websites;
    private String linkedIn;
    private String mendeley;
    private String id;
    private File htmlFile;
    private File htmlEmailFile;
    private transient Document htmlDocument;
    private transient Document htmlEmailDocument;
    private int numberOfPredictedJournals;

    public Profile() {
        journals = new ArrayList<Journal>();
        universities = new ArrayList<University>();
        skills = new ArrayList<String>();
        experiences = new ArrayList<Experience>();
        websites = new ArrayList<String>();
    }

    public ArrayList<String> getWebsites() {
        return websites;
    }

    public void setWebsites(ArrayList<String> websites) {
        this.websites = websites;
    }

    public ArrayList<University> getUniversities() {
        return universities;
    }

    public void setUniversities(ArrayList<University> universities) {
        this.universities = universities;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public ArrayList<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(ArrayList<Experience> experiences) {
        this.experiences = experiences;
    }

    public int getNumberOfPredictedJournals() {
        return numberOfPredictedJournals;
    }

    public void setNumberOfPredictedJournals(int numberOfPredictedJournals) {
        this.numberOfPredictedJournals = numberOfPredictedJournals;
    }

    public File getHtmlEmailFile() {
        return htmlEmailFile;
    }

    public void setHtmlEmailFile(File htmlEmailFile) {
        this.htmlEmailFile = htmlEmailFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
        extractHtmlDocument();
    }

    public Document getHtmlDocument() {
        return htmlDocument;
    }

    public void setHtmlDocument(Document htmlDocument) {
        this.htmlDocument = htmlDocument;
    }

    public void extractHtmlDocument() {
        try {
            if (htmlFile != null) {
                BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder = builder.append(line);
                }

                htmlDocument = Jsoup.parse(builder.toString());
            } else {
//                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBuggedHtmlDocument() {
        if (htmlDocument != null) {
            return htmlDocument.body().text().contains("Something isn't working. If there's still a problem, please contact your administrator or helpdesk.");
        }
        return false;
    }

    public void findMyJournals() {
        if (htmlFile != null) {
            Element wrapperOrderedList = htmlDocument.select("ol.search-result-wrapper").first();
            if (wrapperOrderedList != null) {
                Elements resultListItems = wrapperOrderedList.select("li.ResultItem");
                if (resultListItems != null) {
                    for (Element result : resultListItems) {
                        Element currentJournalTitleLink = result.select("a.result-list-title-link").first();
                        String titleText = null, url = null, date = null;
                        ArrayList<String> authors = new ArrayList<>();
                        titleText = currentJournalTitleLink.text();
                        url = currentJournalTitleLink.attr("href");
                        try {
                            Element dateElement = result.select("ol.SubType").first().children().get(2);
                            if (dateElement != null) {
                                date = dateElement.text();
                            }
                        } catch (IndexOutOfBoundsException iobe) {
                            continue;
                        }

                        Element authorsWrapper = result.selectFirst("ol.Authors");
                        Elements authorItemElements = authorsWrapper.children();
                        for (Element authorElement : authorItemElements) {
                            authors.add(authorElement.text());
                        }

                        String allegedAuthor = "";

                        for (String author : authors) {
                            if (fullName.contains(author) && author.contains(fullName)) {
                                allegedAuthor = author;
                                System.out.println(" Alleged: " + author + " | REAL : " + fullName);
                                break;
                            }
                        }

                        if (!allegedAuthor.isEmpty()) {
                            journals.add(new Journal(titleText, url, authors, date));
                        }
//                    tempJournal.print();
                    }
                }
            }
        }

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        String[] splitedName = fullName.split(" ");

        switch (splitedName.length) {
            case 1:
                setFirstName(splitedName[0]);
                break;
            case 2:
                setFirstName(splitedName[0]);
                setLastName(splitedName[1]);
                break;
            case 3:
                setFirstName(splitedName[0]);
                setMiddleName(splitedName[1]);
                setLastName(splitedName[2]);
                break;
            case 4:
                setFirstName(splitedName[0] + " " + splitedName[1]);
                setLastName(splitedName[2] + " " + splitedName[3]);
                break;
            default:
                setFirstName(splitedName[0] + " " + splitedName[1]);
                setMiddleName(splitedName[2]);
                StringBuilder lastNameBuilder = new StringBuilder();
                for (int i = 3; i < splitedName.length; i++)
                    lastNameBuilder = lastNameBuilder.append(splitedName[i] + " ");

                setLastName(lastNameBuilder.toString());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Journal> getJournals() {
        return journals;
    }

    public void addJournal(Journal journal) {
        journals.add(journal);
    }

    public void setJournals(ArrayList<Journal> journals) {
        this.journals = journals;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        StringBuilder toReturnBuilder = new StringBuilder();
        toReturnBuilder.append("|------------------ Profile(Full Name) : " + fullName + " ------------------|\n");
        if (htmlFile != null) {
            toReturnBuilder.append("| HTML FILE PATH : " + htmlFile.getAbsolutePath() + "\n");
        } else {
            toReturnBuilder.append("| HTML FILE PATH : " + "NO FILE ASSIGNED" + "\n");
        }

        if (htmlEmailFile != null) {
            toReturnBuilder.append("| HTML EMAIL FILE PATH : " + htmlEmailFile.getAbsolutePath() + "\n");
        } else {
            toReturnBuilder.append("| HTML EMAIL FILE PATH : " + "NO FILE ASSIGNED" + "\n");
        }

        toReturnBuilder.append("| id : " + id + "\n| FirstName :" + firstName + "\n| middleName : " + middleName + "\n| LastName : " + lastName + "\n| phone : " + phone + "\n| email : " + email + "\n| Journals : \n");

        for (Journal journal : journals) {
            toReturnBuilder.append("| Journal #" + journals.indexOf(journal) + " :\n" + journal.toString() + "\n");
        }
        toReturnBuilder.append("| Universities:\n");
        for (University university : universities) {
            toReturnBuilder = toReturnBuilder.append("| University #" + universities.indexOf(university) + ":\n| " + university.toString() + "\n");
        }

        toReturnBuilder.append("| Skills : \n");
        for (String skill : skills) {
            toReturnBuilder.append("| Skill #" + skills.indexOf(skill) + " : " + skill + "\n");
        }

        toReturnBuilder.append("| Experiences :\n");

        for (Experience experience : experiences) {
            toReturnBuilder.append("| Experience #" + experiences.indexOf(experience) + " : " + experience.toString() + "\n");
        }

        return toReturnBuilder.toString();
    }

    public String toStringSummary() {
        StringBuilder toReturnBuilder = new StringBuilder();
        toReturnBuilder.append("|------------------ Profile(Full Name) : " + fullName + " ------------------|\n");

        if (htmlFile != null) {
            toReturnBuilder.append("| HTML FILE PATH : " + htmlFile.getAbsolutePath() + "\n");
        } else {
            toReturnBuilder.append("| HTML FILE PATH : " + "NO FILE ASSIGNED" + "\n");
        }

        if (htmlEmailFile != null) {
            toReturnBuilder.append("| HTML EMAIL FILE PATH : " + htmlEmailFile.getAbsolutePath() + "\n");
        } else {
            toReturnBuilder.append("| HTML EMAIL FILE PATH : " + "NO FILE ASSIGNED" + "\n");
        }

        toReturnBuilder.append("| FirstName :" + firstName + "\n| middleName : " + middleName + "\n| LastName : " + lastName + "\n| phone : " + phone + "\n| email : " + email + "\n| Journals [ " + journals.size() + " ] : \n");

        toReturnBuilder.append("| Universities [ " + universities.size() + " ] :\n");

        toReturnBuilder.append("| Skills [ " + skills.size() + " ] :\n");

        toReturnBuilder.append("| Experiences [ " + experiences.size() + " ] :\n");

        return toReturnBuilder.toString();
    }

    public void printSummary() {
        System.out.println(toStringSummary());
    }

    public void print() {
        System.out.println(toString());
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getMendeley() {
        return mendeley;
    }

    public void setMendeley(String mendeley) {
        this.mendeley = mendeley;
    }

    public boolean equals(Profile profile) {
        return profile.getFullName().equals(profile.getFullName());
    }

    public void extractHtmlEmailDocument() {
        if (htmlEmailFile != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(htmlFile));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                bufferedReader.close();
                htmlEmailDocument = Jsoup.parse(builder.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTeacher() {
        boolean isTeacher = false;
//        extractHtmlEmailDocument();
        if (htmlEmailFile != null) {
            if (htmlEmailDocument != null) {
                try {

                    Element scriptElement = htmlEmailDocument.select("script[data-iso-key='_0']").first();
                    String txt = null;
                    if (scriptElement != null) {
                        txt = scriptElement.toString().replace("<script type=\"application/json\" data-iso-key=\"_0\">", "").replace("</script>", "");
                        JSONParser jsonParser = new JSONParser();
//            System.out.println(scriptElement.text());

                        Object object = jsonParser.parse(txt);
                        JSONObject jsonObject = (JSONObject) object;
                        JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("authors")).get("content");
                        JSONObject contentObj = (JSONObject) jsonArray.get(0);
                        JSONArray contentInfoBox = (JSONArray) contentObj.get("$$");


                        for (int i = 0; i < contentInfoBox.size(); i++) {
                            JSONObject contentBox = (JSONObject) contentInfoBox.get(i);

                            String contentBoxTitle = (String) contentBox.get("#name");

                            String givenName = "";
                            String givenSurname = "";
                            String email = "";
                            String authorId = null;
                            if (contentBoxTitle.equals("author")) {
                                JSONArray authorsInfoArr = (JSONArray) contentBox.get("$$");
                                JSONObject idObj = (JSONObject) contentBox.get("$");
                                authorId = (String) idObj.get("author-id");


                                //THIS IS WILL EXTRACT USER PERSONAL DATA
                                for (int j = 0; j < authorsInfoArr.size(); j++) {
//                    System.out.println("Getting mini info");
                                    JSONObject authorInfoBox = (JSONObject) authorsInfoArr.get(j);
                                    String boxName = (String) authorInfoBox.get("#name");
                                    String boxValue = (String) authorInfoBox.get("_");

                                    switch (boxName) {
                                        case "given-name":
                                            givenName = boxValue;
                                            break;
                                        case "surname":
                                            givenSurname = boxValue;
                                            break;
                                        case "e-address":
                                            email = boxValue;
                                            break;
                                    }
                                }

                            } else if (contentBoxTitle.equals("affiliation")) {
                                for (int j = 0; j < contentInfoBox.size(); j++) {
                                    JSONObject contentBoxDup = (JSONObject) contentInfoBox.get(i);

                                    String contentBoxTitleDup = (String) contentBox.get("#name");
                                    if (contentBoxTitleDup.equals("affiliation")) {
                                        String affToDistinc = (String) (((JSONObject) contentBoxDup.get("$")).get("affiliation-id"));
                                        if (authorId.equals(affToDistinc)) {
                                            JSONArray $$jsonArray = (JSONArray) contentBoxDup.get("$$");
                                            for (int k = 0; k < $$jsonArray.size(); k++) {
                                                JSONObject current$$Obj = (JSONObject) $$jsonArray.get(k);
                                                String objName = (String) current$$Obj.get("#name");
                                                String objValue = (String) current$$Obj.get("_");
                                                if (objName.equals("textfn")) {
                                                    String lower = objValue.toLowerCase();
                                                    System.out.println(lower);
                                                    if (lower.contains("assistant") || lower.contains("professor") || lower.contains("assistance") || lower.contains("assistant professor")) {
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}