import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.json.Json;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Netter {
    private URL sdurl;
    private HashMap<String, String> config;
    private ArrayList<String> keywords;
    private HttpsURLConnection netterConnection;
    private WebDriver chromeDriver;


    public Netter() {
        config = new HashMap<>();
        keywords = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", "D:\\k1\\src\\main\\java\\chromedriver.exe");
    }

    public void setConfig(String authors, String show, String title, String volume, String issue, String page, ArrayList<String> keywords) {
        config.clear();
        if (!authors.isEmpty()) {
            config.put("authors", authors);
        }
        if (!title.isEmpty()) {
            config.put("pub", title);
        }
        if (!volume.isEmpty()) {
            config.put("volume", volume);
        }
        if (!issue.isEmpty()) {
            config.put("issue", issue);
        }
        if (!page.isEmpty()) {
            config.put("page", page);
        }

        config.put("show", "100");

        this.keywords.clear();
        this.keywords.addAll(keywords);
    }

    private void setNetwork() {
        try {
            sdurl = new URL("https://www.sciencedirect.com/search?");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String getParametrizedString() {
        StringBuilder parametrizedConfigBuilder = new StringBuilder();
        if (keywords.size() > 0) {
            parametrizedConfigBuilder.append("qs=");
            for (String keyword : keywords) {
                parametrizedConfigBuilder.append(keyword + "&");
            }
        }

        Iterator iterator = config.keySet().iterator();
        while (iterator.hasNext()) {
            String currentKey = iterator.next().toString();
            try {
                parametrizedConfigBuilder.append(currentKey + "=" + URLEncoder.encode(config.get(currentKey), "UTF-8") + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return parametrizedConfigBuilder.toString();
    }

    public void runManual(Profile currentProfile, Filer filer, String country) {
        setNetwork();
        System.out.println(getParametrizedString());
        URL finalURL = null;
        try {
            finalURL = new URL(sdurl.toString() + getParametrizedString());
            System.out.println(finalURL.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Final URL : " + finalURL.toString());
            netterConnection = (HttpsURLConnection) finalURL.openConnection();
            netterConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = netterConnection.getResponseCode();
            System.out.println("Resposne Code : " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                System.out.println("Response Code OK");
                BufferedReader reader = new BufferedReader(new InputStreamReader(netterConnection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                filer.saveToFile(builder.toString(), config.toString(), currentProfile, country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getProfileEmail(Profile profile, String country) {
        try {
            if (profile.getHtmlFile() != null) {
                Document doc = Jsoup.parse(profile.getHtmlFile(), "UTF-8");
                Element linkElement = doc.selectFirst("a.result-list-title-link");
                if (linkElement != null) {
                    String linkDir = linkElement.attr("href");
                    URL url = new URL("https://www.sciencedirect.com/" + linkDir + "#!");
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
                    int responseCode = httpsURLConnection.getResponseCode();
                    System.out.println(responseCode);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                    String line;
                    StringBuilder stringBuffer = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    httpsURLConnection.disconnect();
                    File target = new File("dataEmailDir\\" + country + "\\" + profile.getFullName() + ".html");
//                    target.getParentFile().mkdirs();
                    target.createNewFile();
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(target));
                    bufferedWriter.write(stringBuffer.toString());
                    bufferedWriter.close();

                    profile.setHtmlEmailFile(target);
                    Document document = Jsoup.parse(stringBuffer.toString());
                    Element scriptElement = document.select("script[data-iso-key='_0']").first();
//            System.out.println(scriptElement);
                    String txt = scriptElement.toString().replace("<script type=\"application/json\" data-iso-key=\"_0\">", "").replace("</script>", "");

                    JSONParser jsonParser = new JSONParser();
//            System.out.println(scriptElement.text());

                    Object object = jsonParser.parse(txt);
                    JSONObject jsonObject = (JSONObject) object;
                    JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("authors")).get("content");
                    JSONObject contentObj = (JSONObject) jsonArray.get(0);
                    JSONArray authors = (JSONArray) contentObj.get("$$");

                    for (int i = 0; i < authors.size(); i++) {
                        JSONObject author = (JSONObject) authors.get(i);
                        JSONArray authorsInfoArr = (JSONArray) author.get("$$");
                        String givenName = "";
                        String givenSurname = "";
                        String email = "";

                        for (int j = 0; j < authorsInfoArr.size(); j++) {
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
                        if (profile.getFullName().toLowerCase().contains(givenName.toLowerCase()) && profile.getFullName().toLowerCase().contains(givenSurname.toLowerCase())) {
                            profile.setEmail(email);
                            break;
                        }
                    }
                } else {
                    System.out.println("no journal link");
                }
            } else {
                System.out.println("no html file");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}