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
        checkTeacher(netter, filer);
//        Profiler buggedProfiler = filer.readProfilerFromDisk(new File("profiler-bugged.ksdc"));

//        System.out.println("bugged profiles fixed : " + buggedProfiler.getNumberOfEmails() + " / " + buggedProfiler.getNumberOfProfiles());
//        findBuggedProfiles(netter, filer, logger);

//        startNetworkCrawler(netter, filer, CONST_AUSTRALIA_20102050, 0, 0, true, false);
//        startNetworkCrawler(netter, filer, CONST_AUSTRALIA_19002009, 0, 0, true, false);

//        updateEmailHardDiskData(filer, false);

//        exportEntireDiskData(filer,false);

//        fixBuggedProfiles(netter, filer, logger);

//        Profiler completeProfiler = filer.readProfilerFromDisk(new File("profiler-complete-withoutBugged.ksdc"));
//        filer.exportProfilerToJson(completeProfiler, "completeBugFix", true, false, true, true, true, true);
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