import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Run {
    public static final String CONST_AUSTRALIA_19002009 = "Australia19002009";
    public static final String CONST_AUSTRALIA_20102050 = "Australia20102050";

    public static void main(String[] args) {
        Netter netter = new Netter();
        Profiler profiler = new Profiler();
        Filer filer = new Filer();
        Logger logger = new Logger();

        runCompleteTask(netter, filer, logger);
    }

    private static void startNetworkCrawler(Netter netter, Filer filer, String country, int startIndex, int stopIndex, boolean fullCrawl, boolean closeAfterDone) {
        File jsonFile = new File("dataSet/" + country + "/FINAL-JSON.json");

        Profiler targetProfiler = filer.readDataFromJSONfile(jsonFile);

        System.out.println(country + " profiles : " + targetProfiler.getNumberOfEmails() + " / " + targetProfiler.getNumberOfProfiles());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (fullCrawl) {
            stopIndex = targetProfiler.getNumberOfProfiles();
        }

        for (int i = startIndex; i < stopIndex; i++) {
            System.out.println("|================================ " + i + " / " + stopIndex + " ================================|");
            try {
                Profile currentProfile = targetProfiler.getProfiles().get(i);
//                currentProfile.isBuggedHtmlDocument();
                // here we will get journals result html file and save it to profiles
                netter.setConfig(currentProfile.getFullName(), "100", "", "", "", "", new ArrayList<String>());
                netter.runManual(currentProfile, filer, country);

                //here will find profile's journals and set them based on html file saved before
                currentProfile.extractHtmlDocument();
                currentProfile.findMyJournals();

                netter.getProfileEmail(currentProfile, country);
            } catch (NullPointerException nullEx) {
                System.out.println("null exception catched.all systems nominal");
            }
        }

        filer.saveProfiler(targetProfiler, country);


        System.out.println(country + " profiles after : " + targetProfiler.getNumberOfEmails() + " / " + targetProfiler.getNumberOfProfiles());


        if (closeAfterDone) {
            System.exit(0);
        }
    }

    private static void updateEmailHardDiskData(Filer filer, boolean closeAfterDone) {
        File[] profilersFiles = new File("D:\\k1").listFiles();

        Profiler emailProfiler = new Profiler();

        int numOfAllProfiles = 0;

        for (File currentFile : profilersFiles) {
            if (currentFile.getName().startsWith("profiler-") && currentFile.getName().endsWith(".ksdc") && !currentFile.getName().equals("profiler-emailProfiler.ksdc") && !currentFile.getName().equals("profiler-fullProfiler.ksdc")) {

                Profiler assignedProfiler = filer.readProfilerFromDisk(currentFile);
                numOfAllProfiles += assignedProfiler.getNumberOfProfiles();

                System.out.println(currentFile.getName() + " profiles emails : " + assignedProfiler.getNumberOfEmails() + " / " + assignedProfiler.getNumberOfProfiles());

                for (Profile profile : assignedProfiler.getProfiles()) {
                    if (!(profile.getEmail() == null || profile.getEmail().isEmpty())) {
                        emailProfiler.addProfile(profile);
                    }
                }
            }
        }

        System.out.println("all : " + emailProfiler.getNumberOfProfiles() + " / " + numOfAllProfiles);

        filer.saveProfiler(emailProfiler, "emailProfiler");
        filer.exportProfilerToJson(emailProfiler, "emailProfilerCompleted", true, true, true, true, true, true);
        filer.exportProfilerToJson(emailProfiler, "emailProfilerWithoutJournals", true, false, true, true, true, true);
        filer.exportProfilerToJson(emailProfiler, "emailProfilerOnlyBasicInfo", true, false, false, false, false, false);


        if (closeAfterDone) {
            System.exit(0);
        }
    }

    private static void runCompleteTask(Netter netter, Filer filer, Logger logger) {

        System.out.println("Started Good.");
//        checkTeacher(netter, filer);

//        Profiler completeProfilerWithoutBuggs = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));
//
//        Profiler profiler = filer.readDataFromJSONfile(new File("dataSet/canada1900-1998/FINAL-JSON.json"));
//        System.out.println(profiler.getNumberOfEmails() + " / " + profiler.getNumberOfProfiles());
//        Profiler profiler1 = filer.readDataFromJSONfile(new File("dataSet/canada1999-2002/FINAL-JSON.json"));
//        System.out.println(profiler1.getNumberOfEmails() + " / " + profiler1.getNumberOfProfiles());
//        Profiler profiler2 = filer.readDataFromJSONfile(new File("dataSet/canada2003-2005/FINAL-JSON.json"));
//        System.out.println(profiler2.getNumberOfEmails() + " / " + profiler2.getNumberOfProfiles());
//        Profiler profiler3 = filer.readDataFromJSONfile(new File("dataSet/canada2006-2008/FINAL-JSON.json"));
//        System.out.println(profiler2.getNumberOfEmails() + " / " + profiler3.getNumberOfProfiles());
//        Profiler profiler4 = filer.readDataFromJSONfile(new File("dataSet/canada2013-2050/FINAL-JSON.json"));
//        System.out.println(profiler4.getNumberOfEmails() + " / " + profiler4.getNumberOfProfiles());


//        System.out.println("bugged profiles fixed : " + completeProfilerWithoutBuggs.getNumberOfEmails() + " / " + completeProfilerWithoutBuggs.getNumberOfProfiles());
//        findBuggedProfiles(netter, filer, logger);

//        startNetworkCrawler(netter, filer, "", 0, 0, true, false);
//        Profiler buggedProfiler = filer.readProfilerFromDisk(new File("profiler-usacanadahtmlfileBugged.ksdc"));
//        System.out.println(buggedProfiler.getNumberOfEmails() + " / " + buggedProfiler.getNumberOfProfiles());
//        for (Profile profile : buggedProfiler.getProfiles()) {
//            System.out.println(buggedProfiler.getProfiles().indexOf(profile) + " / " + buggedProfiler.getNumberOfProfiles());
//            try{
//                netter.setConfig(profile.getFullName(), "100", "", "", "", "", new ArrayList<String>());
//                netter.runManual(profile, filer, "usacanadabugged-fixed");
//
//                //here will find profile's journals and set them based on html file saved before
//                profile.extractHtmlDocument();
//                profile.findMyJournals();
//
//                netter.getProfileEmail(profile, "usacanadabugged-fixed");
//            }catch(Exception ex){
//                System.out.println("exception catched");
//            }
//
////            netter.runManual(profile,filer,"usacanadabugged-fixed");
//        }
//
//
//
//        filer.saveProfiler(buggedProfiler, "usacanadabugged-fixed");
//
//        System.out.println(buggedProfiler.getNumberOfEmails() + " / " + buggedProfiler.getNumberOfProfiles());


//        Profiler profiler = filer.readProfilerFromDisk(new File("profiler-usa1900-1992.ksdc"));
//        Profiler profiler1 = filer.readProfilerFromDisk(new File("profiler-usa1993-2002.ksdc"));
//        Profiler profiler2 = filer.readProfilerFromDisk(new File("profiler-usa2003-2005.ksdc"));
//        Profiler profiler3 = filer.readProfilerFromDisk(new File("profiler-usa2006-2007.ksdc"));
//        Profiler profiler4 = filer.readProfilerFromDisk(new File("profiler-usa2008-2009.ksdc"));
//        Profiler profiler5 = filer.readProfilerFromDisk(new File("profiler-usa2010-2011.ksdc"));
//        Profiler profiler6 = filer.readProfilerFromDisk(new File("profiler-usa2012-2014.ksdc"));
//        Profiler profiler7 = filer.readProfilerFromDisk(new File("profiler-usa2015-2050.ksdc"));


        Profiler profilerCompletedWithoutBugged = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));
        Profiler profiler4 = filer.readProfilerFromDisk(new File("profiler-bugged-fixed.ksdc"));
        Profiler profiler3 = filer.readProfilerFromDisk(new File("profiler-fullProfiler.ksdc"));
        System.out.println(profiler3.getNumberOfEmails() + " / " + profiler3.getNumberOfProfiles());
        System.out.println(profiler4.getNumberOfEmails() + " / " + profiler4.getNumberOfProfiles());
//        System.out.println(profiler2.getNumberOfEmails() + " / " + profiler2.getNumberOfProfiles());
//        System.exit(0);
//
        File profileDirectories = new File("F:\\sneedskahroba");
//
        File[] files = profileDirectories.listFiles();
//
//        int sum = 0, emailSum = 0;
//
        Profiler usaCandaWithoutBuggsProfiler = filer.readProfilerFromDisk(new File("profiler-usacanadaprofilerfullwithoutbugs.ksdc"));

//        System.out.println("I'm here");
        for(Profile profile : usaCandaWithoutBuggsProfiler.getProfiles()){
//            System.out.println("iterating...");
            Iterator it = profilerCompletedWithoutBugged.getProfiles().iterator();
            while(it.hasNext()){
                Profile profileToCheck = (Profile)it.next();
                if(profile.equals(profileToCheck)){
                    System.out.println("find duplicate : ");
                    System.out.println(profile.getFullName() + "  " + profileToCheck.getFullName());
                    it.remove();
//                    profilerCompletedWithoutBugged.addProfile(profile);
                    break;
                }
            }
        }

        for(Profile profile : usaCandaWithoutBuggsProfiler.getProfiles()){
            profilerCompletedWithoutBugged.addProfile(profile);
        }


        filer.saveProfiler(profilerCompletedWithoutBugged,"finalfinalfinalfinalall");

        System.out.println(profilerCompletedWithoutBugged.getNumberOfEmails() + " / " + profilerCompletedWithoutBugged.getNumberOfProfiles());

        //        for (File file : files) {
//            String fileName = file.getName();
//            Profiler tempProfiler;
//            if (fileName.startsWith("profiler-Australia") && fileName.endsWith(".ksdc")) {
//                tempProfiler = filer.readProfilerFromDisk(file);
//                for(Profile profile : tempProfiler.getProfiles()){
//
//                }
//            }
//        }


//        Profiler usacandaProfiler = new Profiler();
//        Profiler otherUsaCanadaProfiler = new Profiler();
//
//
//        for (File file : files) {
//            if (file.getName().startsWith("profiler-") && file.getName().endsWith(".ksdc")) {
//
//            }
//        }
//
//        for (File file : files) {
//            if (file.getName().startsWith("profiler-usa") && file.getName().endsWith(".ksdc")) {
//                Profiler profiler = filer.readProfilerFromDisk(file);
//                for (Profile profile : profiler.getProfiles()) {
//                    usacandaProfiler.addProfile(profile);
//                }
////                usacandaProfiler.setProfiles(profiler.getProfiles());
////                System.out.println(file.getName());
////                System.out.println(profiler.getNumberOfEmails() + " / " + profiler.getNumberOfProfiles());
////
////                sum += profiler.getNumberOfProfiles();
////                emailSum += profiler.getNumberOfEmails();
//            }
//        }
////
//////        System.out.println("Entire USA ( With Buggs ) : " + emailSum + " / " + sum);
////
////
//////        int entireSum = sum, entireEmailSum = emailSum;
////
//////        sum = 0;
//////        emailSum = 0;
////
//        for (File file : files) {
//            if (file.getName().startsWith("profiler-canada") && file.getName().endsWith(".ksdc")) {
//                Profiler profiler = filer.readProfilerFromDisk(file);
//                for (Profile profile : profiler.getProfiles()) {
//                    usacandaProfiler.addProfile(profile);
//                }
////                System.out.println(file.getName());
////                System.out.println(profiler.getNumberOfEmails() + " / " + profiler.getNumberOfProfiles());
//
////                sum += profiler.getNumberOfProfiles();
////                emailSum += profiler.getNumberOfEmails();
//            }
//        }
////
////
////        int buggedNum = 0;
//
//        Profiler usacanadaProfilerFixed = filer.readProfilerFromDisk(new File("profiler-usacanadabugged-fixed.ksdc"));
////        Profiler usacanadaProfiler = filer.readProfilerFromDisk(new File(""));
//        Profiler buggedHtmlFileProfiler = new Profiler();
//        Profiler buggedEmailHtmlFileProfiler = new Profiler();
//        Profiler bothBuggedProfiler = new Profiler();
//        Iterator it = usacandaProfiler.getProfiles().iterator();
//        while (it.hasNext()) {
//            Profile currentProfile = (Profile) it.next();
//            if (currentProfile.isBuggedHtmlFile()) {
//                it.remove();
//            }
//        }
//
//        for (Profile profile : usacanadaProfilerFixed.getProfiles()) {
//            usacandaProfiler.addProfile(profile);
//        }
//
//        filer.saveProfiler(usacandaProfiler, "usacanadaprofilerfullwithoutbugs");
//        System.out.println(usacandaProfiler.getNumberOfEmails() + " / " + usacandaProfiler.getNumberOfProfiles());
////        for (Profile profile : usacandaProfiler.getProfiles()) {
////            if (profile.isBuggedHtmlFile()) {
////                buggedHtmlFileProfiler.addProfile(profile);
//////                buggedNum++;
////            }
////            if (profile.isBuggedHtmlEmailFile()) {
////                buggedEmailHtmlFileProfiler.addProfile(profile);
////            }
////            if (profile.isBuggedHtmlFile() && profile.isBuggedHtmlEmailFile()) {
////                bothBuggedProfiler.addProfile(profile);
////            }
////        }
////
////        System.out.println("number of bugged main files : " + buggedHtmlFileProfiler.getNumberOfProfiles());
////        System.out.println("number of bugged email files : " + buggedEmailHtmlFileProfiler.getNumberOfProfiles());
////        System.out.println("number of bugged files at the same time : " + bothBuggedProfiler.getNumberOfProfiles());
////
////        filer.saveProfiler(buggedHtmlFileProfiler, "usacanadahtmlfileBugged");
//////        System.out.println(buggedHtmlFileProfiler.getNumberOfProfiles() + " / " + usacandaProfiler.getNumberOfProfiles());
//
//
////        System.out.println("Entire Canada ( With Buggs ) : " + emailSum + " / " + sum);
//
////       entireSum += sum;
////        entireEmailSum += emailSum;
////
////
////        System.out.println(entireEmailSum + " / " + entireSum);
////        int usaSum = profiler.getNumberOfProfiles() + profiler2.getNumberOfProfiles() +
////        System.out.println("1900-1992" + );
//
////        startNetworkCrawler(netter, filer, CONST_AUSTRALIA_19002009, 0, 0, true, false);
////        updateEmailHardDiskData(filer, false);
////        exportEntireDiskData(filer,false);
////        fixBuggedProfiles(netter, filer, logger);
////        Profiler completeProfiler = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));
////        filer.exportProfilerToJson(completeProfiler, "completeBugFix", true, false, true, true, true, true);
    }

    private static void fixBuggedProfiles(Netter netter, Filer filer, Logger logger) {
        Profiler buggedProfiler = filer.readProfilerFromDisk(new File("profiler-bugged.ksdc"));
        Profiler completeWithoutBugged = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));

        System.out.println(buggedProfiler.getNumberOfEmails() + " / " + buggedProfiler.getNumberOfProfiles());
        System.out.println(completeWithoutBugged.getNumberOfEmails() + " / " + completeWithoutBugged.getNumberOfProfiles());

        for (Profile profile : buggedProfiler.getProfiles()) {
            try {
                System.out.println("FIXING PROFILE INDEX : " + buggedProfiler.getProfiles().indexOf(profile) + " / " + buggedProfiler.getNumberOfProfiles());
                netter.setConfig(profile.getFullName(), "100", "", "", "", "", new ArrayList<String>());
                netter.runManual(profile, filer, "bugged");

                profile.extractHtmlDocument();
                profile.findMyJournals();
                netter.getProfileEmail(profile, "bugged");
                completeWithoutBugged.addProfile(profile);
            } catch (NullPointerException nullEx) {
                System.out.println("null exception catched.all systems nominal");
            }
        }

        filer.saveProfiler(buggedProfiler, "bugged-fixed");
        filer.saveProfiler(completeWithoutBugged, "complete-withoutBugged");

        System.out.println(buggedProfiler.getNumberOfEmails() + " / " + buggedProfiler.getNumberOfProfiles());
        System.out.println(completeWithoutBugged.getNumberOfEmails() + " / " + completeWithoutBugged.getNumberOfProfiles());
    }

    private static void findBuggedProfiles(Netter netter, Filer filer, Logger logger) {
        Profiler completeProfiler = filer.readProfilerFromDisk(new File("profiler-fullProfiler.ksdc"));
        System.out.println("number of all profiles before removing bugged ones : " + completeProfiler.getNumberOfProfiles());
//        profiler.printSummaryProfiles();
        Profiler buggedProfiler = new Profiler();
        Iterator iterator = completeProfiler.getProfiles().iterator();
        while (iterator.hasNext()) {
            Profile currentProfile = (Profile) iterator.next();
            currentProfile.extractHtmlDocument();
            if (currentProfile.isBuggedHtmlDocument()) {
                buggedProfiler.addProfile(currentProfile);
                iterator.remove();
            }
        }

        System.out.println("number of bugged profiles : " + buggedProfiler.getNumberOfProfiles());
        System.out.println("number of all profiles without bugged : " + completeProfiler.getNumberOfProfiles());
        filer.saveProfiler(buggedProfiler, "bugged");
        filer.saveProfiler(completeProfiler, "complete-withoutBugged");
    }

    private static void exportEntireDiskData(Filer filer, boolean closeAfterDone) {
        File[] profilersFiles = new File("D:\\k1").listFiles();

        Profiler fullProfiler = new Profiler();

        int numOfAllProfiles = 0;

        for (File currentFile : profilersFiles) {
            if (currentFile.getName().startsWith("profiler-") && currentFile.getName().endsWith(".ksdc") && !currentFile.getName().equals("profiler-emailProfiler.ksdc") && !currentFile.getName().equals("profiler-fullProfiler.ksdc")) {

                Profiler assignedProfiler = filer.readProfilerFromDisk(currentFile);

                filer.exportProfilerToJson(assignedProfiler, currentFile.getName().replace(".ksdc", "") + "WithoutJournals", true, false, true, true, true, true);
                filer.exportProfilerToJson(assignedProfiler, currentFile.getName().replace(".ksdc", "") + "OnlyBasicInfo", true, false, false, false, false, false);

                System.out.println("Export done for :" + currentFile.getName().replace(".ksdc", ""));

                for (Profile profile : assignedProfiler.getProfiles()) {
                    fullProfiler.addProfile(profile);
                }
            }
        }

//        System.out.println("all : " + emailProfiler.getNumberOfProfiles() + " / " + numOfAllProfiles);

        filer.saveProfiler(fullProfiler, "fullProfiler");
//        filer.exportProfilerToJson(emailProfiler, "fullProfilerCompleted", true, true, true, true, true, true);
        filer.exportProfilerToJson(fullProfiler, "fullProfilerWithoutJournals", true, false, true, true, true, true);
        filer.exportProfilerToJson(fullProfiler, "fullProfilerOnlyBasicInfo", true, false, false, false, false, false);
        System.out.println("Export done for all of them");


        if (closeAfterDone) {
            System.exit(0);
        }
    }

    private static void checkTeacher(Netter netter, Filer filer) {
        Profiler profiler = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));
        Profiler teacherProfiler = new Profiler();
        System.out.println(profiler.getNumberOfEmails() + " / " + profiler.getNumberOfProfiles());
        for (Profile profile : profiler.getProfiles()) {
            profile.extractHtmlEmailDocument();
//                if (profile.getHtmlEmailFile() != null) {
//                    profile.printSummary();
//                }
            System.out.println("Checking " + profiler.getProfiles().indexOf(profile) + " / " + profiler.getNumberOfProfiles());
            if (profile.isTeacher()) {
                teacherProfiler.addProfile(profile);
            }


        }
        teacherProfiler.printSummaryProfiles();
    }
}