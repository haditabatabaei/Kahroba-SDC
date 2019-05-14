import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Filer {
    private ArrayList<File> files;

    public Filer() {
        files = new ArrayList<>();
    }

    public Profiler readDataFromJSONfile(File jsonFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            StringBuilder builder = new StringBuilder();
            String line;
            int counter = 0;
            JSONArray listOfGermany = new JSONArray();
            JSONParser parser = new JSONParser();

            while ((line = reader.readLine()) != null) {
                counter++;
//                System.out.println("#" + counter + " : " + line);
                JSONObject currentStudentObject = (JSONObject) parser.parse(line);
                listOfGermany.add(currentStudentObject);
            }

            reader.close();


            Profiler profiler = new Profiler();

            for (Object student : listOfGermany) {
                JSONObject studentObj = (JSONObject) student;
                profiler.addEmptyProfile();
                Profile currentProfile = profiler.getRecentlyAddedProfile();
                String id = (String) studentObj.get("id");
                String fulLName = (String) studentObj.get("name");
                String email = (String) studentObj.get("email");
                String phone = (String) studentObj.get("phone");
                JSONArray websites = (JSONArray) studentObj.get("websites");
                JSONArray skills = (JSONArray) studentObj.get("skills");
                JSONArray universities = (JSONArray) studentObj.get("universities");
                JSONArray experiences = (JSONArray) studentObj.get("experiences");


                currentProfile.setFullName(fulLName);
                currentProfile.setId(id);
                currentProfile.setEmail(email);
                currentProfile.setPhone(phone);

                if (websites != null) {
                    for (Object webObj : websites) {
                        currentProfile.getWebsites().add((String) webObj);
                    }
                }


                for (Object skill : skills) {
                    currentProfile.getSkills().add((String) skill);
                }

                if (universities != null) {
                    for (Object university : universities) {
                        JSONObject uniJson = (JSONObject) university;
                        String uniName = (String) uniJson.get("name");
                        String degree = (String) uniJson.get("degree");
                        String fieldOfStudy = (String) uniJson.get("field of study");
                        String date = (String) uniJson.get("date");
                        currentProfile.getUniversities().add(new University(uniName, degree, fieldOfStudy, date));
                    }
                }


                if (experiences != null) {
                    for (Object experience : experiences) {
                        JSONObject exJson = (JSONObject) experience;
                        String title = (String) exJson.get("title");
                        String company = (String) exJson.get("company");
                        currentProfile.getExperiences().add(new Experience(title, company));
                    }
                }


            }
            return profiler;
//            profiler.printSummaryProfiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Profiler readProfilerFromDisk(File targetFile) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(targetFile));
            return (Profiler) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveProfileToTxtFile(Profile toSaveProfile) {
        File targetTxtFile = new File("txtData\\" + toSaveProfile.getFullName() + "-" + toSaveProfile.getJournals().size() + ".txt");
        try {
            targetTxtFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetTxtFile));
            writer.write(toSaveProfile.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getListOfFiles() {
        String fullPath = "D:\\k1\\";
        File dir = new File(fullPath);
        if (dir.isDirectory()) {
            System.out.println("Dir is director");
        }

        File[] filesArr = dir.listFiles();
        for (File file : filesArr) {
            if (file.getName().endsWith(".html")) {
                files.add(file);
            }
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void updateProfiler(Profiler profiler) {
        for (File file : files) {
            profiler.addEmptyProfile();
            Profile profile = profiler.getRecentlyAddedProfile();
            String fullName = file.getName().substring(19, file.getName().length() - 1);
            profile.setFullName(fullName);
            String[] splitedName = fullName.split(" ");

            switch (splitedName.length) {
                case 1:
                    profile.setFirstName(splitedName[0]);
                    break;
                case 2:
                    profile.setFirstName(splitedName[0]);
                    profile.setLastName(splitedName[1]);
                    break;

                case 3:
                    profile.setFirstName(splitedName[0]);
                    profile.setMiddleName(splitedName[1]);
                    profile.setLastName(splitedName[2]);
                    break;
                case 4:
                    profile.setFirstName(splitedName[0] + " " + splitedName[1]);
                    profile.setLastName(splitedName[2] + " " + splitedName[3]);
                    break;
            }

            profile.setHtmlFile(file);
        }
    }

    public void saveProfiler(Profiler profiler, String country) {
        File target = new File("profiler-" + country + ".ksdc");
//        if (!target.exists()) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(target));
            objectOutputStream.writeObject(profiler);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }

    public Profiler readCsvData(File file) {
        Profiler profiler = new Profiler();
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);
        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, StandardCharsets.UTF_8);
            for (CsvRow row : csv.getRows()) {
                profiler.addEmptyProfile();
                Profile currentProfile = profiler.getRecentlyAddedProfile();
                currentProfile.setFullName(row.getField("name"));
                currentProfile.setEmail(row.getField("email"));
                currentProfile.setPhone(row.getField("phone"));
                String universities = row.getField("universities")
                        .replaceAll("\\[", "")
                        .replaceAll("]", "")
                        .replaceAll("'", "")
                        .replaceAll("\"", "")
                        .trim();

//                System.out.println(universities);
                if (!universities.isEmpty()) {
//                System.out.println(universities);
                    String[] universitiesArr = universities.split(",");

                    for (int i = 0; i < universitiesArr.length; i++) {

                        String fullUniString = universitiesArr[i].trim();

//                        System.out.println(fullUniString);
                        // System.out.println(fullUniString.trim().split("||"));

                        String[] currentUniData = fullUniString.split("\\|\\|");

//                        System.out.println(currentUniData[0] + currentUniData[1] + currentUniData[2] + currentUniData[3]);
                        String tempName = " ", tempDegree = " ", tempStudyField = " ", tempDate = " ";
                        switch (currentUniData.length) {
                            case 1:
                                tempName = currentUniData[0].trim().replace("University name:", "").trim();
                                break;
                            case 2:
                                tempName = currentUniData[0].trim().replace("University name:", "").trim();
                                tempDegree = currentUniData[1].trim().replace("Degree:", "").trim();
                                break;
                            case 3:
                                tempName = currentUniData[0].trim().replace("University name:", "").trim();
                                tempDegree = currentUniData[1].trim().replace("Degree:", "").trim();
                                tempStudyField = currentUniData[2].trim().replace("Field Of Study:", "").trim();
                                break;
                            case 4:
                                tempName = currentUniData[0].trim().replace("University name:", "").trim();
                                tempDegree = currentUniData[1].trim().replace("Degree:", "").trim();
                                tempStudyField = currentUniData[2].trim().replace("Field Of Study:", "").trim();
                                tempDate = currentUniData[3].trim().replace("Date:", "").trim();
                                break;
                        }
                        currentProfile.getUniversities().add(new University(tempName, tempDegree, tempStudyField, tempDate));
                    }
                }


                String skillsString = row.getField("skills");
                String experiencesString = row.getField("experiences");
                String websitesString = row.getField("websites");


                String workableSkills = skillsString.replace("]", "").replace("\\]", "");

                if (!workableSkills.isEmpty()) {
                    workableSkills = workableSkills.replaceAll("'", "");
                    String[] skillsArr = workableSkills.split(",");
                    for (String skill : skillsArr) {
                        currentProfile.getSkills().add(skill);
                    }
                }

                String workableExperiences = experiencesString.replace("]", "").replace("\\]", "").replaceAll("\"", "");
                if (!workableExperiences.isEmpty()) {
                    workableExperiences = workableExperiences.replaceAll("'", "").replaceAll("\"", "");
                    String[] workEntries = workableExperiences.split(",");
                    for (String workEntry : workEntries) {
                        String[] workEntryInfos = workEntry.split("\\|");
                        for (String string : workEntryInfos)
                            System.out.println(string);
                        String entryTitle = workEntryInfos[0].replace("Title:", "").trim();
                        String entryCompany = workEntryInfos[1].replace("Company:", "").trim();
                        currentProfile.getExperiences().add(new Experience(entryTitle, entryCompany));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return profiler;
    }

    public void addDataToProfiler(Profiler profiler, File file) {
//        File file = new File("foo.csv");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
//              System.out.println(line);
                String[] tempCurrentLineRecords = line.split(",");
//              System.out.println(tempCurrentLineRecords.length);
                profiler.addEmptyProfile();
                Profile tempProfile = profiler.getRecentlyAddedProfile();
                tempProfile.setId(tempCurrentLineRecords[0]);
                tempProfile.setFullName(tempCurrentLineRecords[1]);
                tempProfile.setEmail(tempCurrentLineRecords[3]);
                tempProfile.setPhone(tempCurrentLineRecords[4]);
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String toSaveString, String title, Profile currentProfile, String country) {
        File target = new File("newDir\\" + country + "\\" + title + ".html");
        try {
            target.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            Document defaultDocument = Jsoup.parse(toSaveString);

            defaultDocument.select("script").remove();
            defaultDocument.select("path").remove();
            defaultDocument.select("iframe").remove();
            defaultDocument.select("head").remove();

            BufferedWriter writer = new BufferedWriter(new FileWriter(target));
            writer.write(defaultDocument.toString());
            writer.close();

            currentProfile.setHtmlFile(target);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportProfilerToJson(Profiler profiler, String country, boolean hasBasicInfo, boolean hasJournals, boolean hasSkills, boolean hasWebsies, boolean hasUniversities, boolean hasExperiences) {
        JSONArray jsonArray = new JSONArray();
        for (Profile profile : profiler.getProfiles()) {
            JSONObject jsonObject = new JSONObject();

            if (hasBasicInfo) {
                jsonObject.put("id", profile.getId());
                jsonObject.put("name", profile.getFullName());
                jsonObject.put("email", profile.getEmail());
                jsonObject.put("phone", profile.getPhone());
            }

            if (hasWebsies) {
                JSONArray websitesArr = new JSONArray();
                for (String website : profile.getWebsites()) {
                    websitesArr.add(website);
                }

                jsonObject.put("websites", websitesArr);

            }


            if (hasSkills) {

                JSONArray skillsArr = new JSONArray();
                for (String skill : profile.getSkills()) {
                    skillsArr.add(skill);
                }
                jsonObject.put("skills", skillsArr);

            }

            if (hasUniversities) {
                JSONArray universitiesArr = new JSONArray();
                for (University university : profile.getUniversities()) {
                    JSONObject universityObj = new JSONObject();
                    universityObj.put("name", university.getName());
                    universityObj.put("degree", university.getDegree());
                    universityObj.put("field of study", university.getFieldOfStudy());
                    universityObj.put("date", university.getDate());
                    universitiesArr.add(universityObj);
                }

                jsonObject.put("universities", universitiesArr);

            }

            if (hasExperiences) {
                JSONArray expArr = new JSONArray();
                for (Experience experience : profile.getExperiences()) {
                    JSONObject exObj = new JSONObject();
                    exObj.put("title", experience.getTitle());
                    exObj.put("company", experience.getCompany());
                    expArr.add(exObj);
                }
                jsonObject.put("experiences", expArr);

            }

            if (hasJournals) {
                JSONArray journalsArr = new JSONArray();
                for (Journal journal : profile.getJournals()) {
                    JSONObject jourObj = new JSONObject();

                    jourObj.put("title", journal.getTitle());
                    jourObj.put("date", journal.getDateStr());
                    jourObj.put("url", journal.getUrl());

                    JSONArray authorsArr = new JSONArray();
                    for (String author : journal.getAuthors()) {
                        authorsArr.add(author);
                    }

                    jourObj.put("authors", authorsArr);

                    journalsArr.add(jourObj);
//                    System.out.println(journal.toString());
                }


                jsonObject.put("journals", journalsArr);

            }


            jsonArray.add(jsonObject);
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("exportedJSON-" + country + ".json")));
            bufferedWriter.write(jsonArray.toJSONString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkBuggedHtmlFile(File file) {
        boolean bugged = false;
//        if()
        return bugged;
    }
}
