import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Profiler implements Serializable {
    //default serialVersion id
    private static final long serialVersionUID = 1L;

    private ArrayList<Profile> profiles;

    public Profiler() {
        profiles = new ArrayList<Profile>();
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public void addEmptyProfile() {
        profiles.add(new Profile());
    }

    public Profile getRecentlyAddedProfile() {
        return profiles.get(profiles.size() - 1);
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public Profile findProfileByFullName(String fullName) {
        for (Profile profile : profiles) {
            if (profile.getFullName().equals(fullName))
                return profile;
        }
        return null;
    }

    public int getNumberOfProfiles() {
        return profiles.size();
    }

    public int getNumberOfEmails() {
        int counter = 0;
        for (Profile profile : profiles) {
            if (!(profile.getEmail() == null || profile.getEmail().isEmpty())) {
                counter++;
            }
        }
        return counter;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public void printAllProfilesWithEmail() {
        for (Profile profile : profiles) {
            if (!(profile.getEmail() == null || profile.getEmail().isEmpty())) {
                profile.printSummary();
            }
        }
    }

    public int getNumberOfTeachers() {
        int count = 0;
        for (Profile profile : profiles) {
            if (profile.isTeacher()) {
                count++;
            }
        }
        return count;
    }

    public Profiler getProfilesWithEmail() {
        Profiler toReturn = new Profiler();
        for (Profile profile : profiles) {
            if (!(profile.getEmail() == null || profile.getEmail().isEmpty())) {
                toReturn.addProfile(profile);
            }
        }
        return toReturn;
    }

    public void printProfiles() {
        for (Profile profile : profiles) {
            System.out.println("PROFILE INDEX : " + profiles.indexOf(profile) + " / " + profiles.size());
            profile.print();
        }
    }

    public void printSummaryProfiles() {
        for (Profile profile : profiles) {
            System.out.println("PROFILE NUM : " + (profiles.indexOf(profile) + 1) + " / " + profiles.size());
            profile.printSummary();
        }
    }

    public ArrayList<Profile> hasProfile(Profile profileToCheck) {
        ArrayList<Profile> result = new ArrayList<>();
        for (Profile profile : profiles) {
//            System.out.println("COPIES OF : ");
//            profile.printSummary();
//            System.out.println("--------------------------------------------------->");
            if (profile.getFullName().equals(profileToCheck.getFullName())) {
//                profile.print();
                result.add(profile);
//                profile.printSummary();

            }

        }
        return result;
    }

    public Profiler findAndRemoveDuplicates2() {
        Profiler toReturn = new Profiler();
        ArrayList<Profile> duplicates = new ArrayList<>();
        int numOfDups = 0, counter = 0;

        Iterator wrapperIterator = profiles.iterator();

//        for(Profile profile : profiles){
//            System.out.println(hasProfile(profile).size());
//        }

//        System.out.println(profiles.size());


        ArrayList<ArrayList<Profile>> allIndexes = new ArrayList<>();
        for (Profile profile : profiles) {
            allIndexes.add(hasProfile(profile));
        }

        int count = 0;
        for (ArrayList<Profile> profileCopies : allIndexes) {
            count += profileCopies.size();
//            System.out.println(profileCopies.get(0).getCountry());
            for (int i = 1; i < profileCopies.size(); i++) {
//                Profile currentProfile = profileCopies.get(i);
                profiles.remove(profileCopies.get(i));
            }
        }
//        System.out.println(count);

//        System.out.println(profiles.size());

//q
//        for(Profile profile : profiles){
//            System.out.println(hasProfile(profile).size());
//        }

//        System.out.println(getNumberOfEmails() + " / " + getNumberOfProfiles());
        return this;
//        System.exit(0);
//
//        while(wrapperIterator.hasNext()){
////            ArrayList<Integer> indexes = hasProfile((Profile) wrapperIterator.next());
////            for(int i = 1 ; i < indexes.size() ; i++){
////                profiles.remove();
////            }
//        }
//
//        for(Profile profile : profiles){
//            Iterator childIterator = profiles.iterator();
////            Profile selectedProfile = (Profile) wrapperIterator.next();
//            counter = 0;
//            while(childIterator.hasNext()){
//                Profile selectedToCompare = (Profile) childIterator.next();
//                if(profile.getFullName().equals(selectedToCompare.getFullName())){
//                    counter++;
//                    if(counter > 1){
//                        childIterator.remove();
//                    }
//                }
//            }
//        }
//
//        System.out.println(profiles.size());
//        System.exit(0);
//        for (Profile profile : profiles) {
//            counter = 0;
//            for (Profile profile1 : profiles) {
//                if (profile.getFullName().equals(profile1.getFullName())) {
//                    if (profiles.indexOf(profile1) > profiles.indexOf(profile)) {
//                        if (!duplicates.contains(profile1)) {
//                            duplicates.add(profile1);
//                        }
//                    }
//                }
//            }
////            if(counter > 1)
////                numOfDups++;
//        }
//
//        for (Profile dupProfile : duplicates) {
//            profiles.remove(dupProfile);
//        }
//        System.out.println(profiles.size());
//        System.exit(0);
    }

    public void findAndRemoveDuplicates() {
        ArrayList<Profile> finalProfiles = new ArrayList<>();
        ArrayList<Profile> similarProfiles = new ArrayList<>();
        for (Profile profile : profiles) {
            int counter = 0;
            for (Profile profile1 : profiles) {
                if (profile.getFullName().equals(profile1.getFullName())) {
                    counter++;
                    if (counter > 1) {
                        profiles.remove(profile1);
                    }
                }
            }

            System.out.println(profiles.size());


            System.exit(0);


//
//
//
////        profiles.get(6846).printSummary();
////        profiles.get(9560).printSummary();
////        profiles.get(10377).printSummary();
//        System.out.println(profiles.size());
////        System.exit(0);
//        ArrayList<Profile> profilesWithoutDuplicate = new ArrayList<>();
//        ArrayList<Profile> tempSimilarProfiles = new ArrayList<>();
//        ArrayList<Profile> profilesHasEmailFile = new ArrayList<>();
//        ArrayList<Profile> profileDoesntHaveEmailFile = new ArrayList<>();
//        int allDuplucates = 0;
//        for (Profile profile : profiles) {
//
//            int counter = 0;
////            System.out.println("duplicates to : " + profile.getFullName() + " : ");
//
//            for (Profile profile2 : profiles) {
//                if (profile.getFullName().equals(profile2.getFullName())) {
//                    tempSimilarProfiles.add(profile2);
////                    counter++;
////                    System.out.println(profiles.indexOf(profile2));
//                }
//            }
//
//
////            boolean anyEmailFileAvailable = false;
//            if (tempSimilarProfiles.size() == 1) {
//                profilesWithoutDuplicate.add(tempSimilarProfiles.get(0));
//            } else {
//                for (Profile similarProfile : tempSimilarProfiles) {
//                    if (similarProfile.getHtmlEmailFile() != null) {
//                        profilesHasEmailFile.add(similarProfile);
//                    } else {
//                        profileDoesntHaveEmailFile.add(similarProfile);
//                    }
////                similarProfile.printSummary();
//                }
//
//                if (profilesHasEmailFile.size() > 0) {
//                    int fixCounter = 0;
//                    for (Profile hasEmailFileProfile : profilesHasEmailFile) {
//                        if (hasEmailFileProfile.getHtmlEmailFile().getAbsolutePath().toLowerCase().contains("fix")) {
//                            profilesWithoutDuplicate.add(hasEmailFileProfile);
//                            System.out.println("added to fixed : ");
//                            profilesWithoutDuplicate.get(profilesWithoutDuplicate.size() - 1).printSummary();
//                            break;
//                        } else {
//                            fixCounter++;
//                        }
//                    }
//                    if (fixCounter == profilesHasEmailFile.size()) {
//                        profilesWithoutDuplicate.add(profilesHasEmailFile.get(0));
//                        System.out.println("added to fixed : ");
//                        profilesWithoutDuplicate.get(profilesWithoutDuplicate.size() - 1).printSummary();
//                        break;
//                    }
//                } else {
//                    profilesWithoutDuplicate.add(profileDoesntHaveEmailFile.get(0));
//                    System.out.println("added to fixed : ");
//                    profilesWithoutDuplicate.get(profilesWithoutDuplicate.size() - 1).printSummary();
//                }
//            }
//
//
//            tempSimilarProfiles.clear();
//            profilesHasEmailFile.clear();
//            profileDoesntHaveEmailFile.clear();
//
////            System.out.println("-----------------------------------------------");
////            tempSimilarProfiles.clear();
//
////            counter--;
////            allDuplucates += counter;
////            System.out.println("---------------------------------");
//        }
////        Profile profiler = new Profile()
//        System.out.println(profilesWithoutDuplicate.size());
//        int full = 0 ;
//        for(Profile profile : profilesWithoutDuplicate){
//            int counter = 0;
//
//            for (Profile profile2 : profilesWithoutDuplicate) {
//                if (profile.getFullName().equals(profile2.getFullName())) {
//                    tempSimilarProfiles.add(profile2);
//                    counter++;
////                    System.out.println(profiles.indexOf(profile2));
//                }
//            }
//            full += counter;
//        }
//        System.out.println("----------" + full + " ----------------");
////        System.out.println(allDuplucates);
////        for(Profile profile : profiles){
////            if(!newProfiler.getProfiles().contains(profile)){
////                newProfiler.getProfiles().add(profile);
////            }
//        }
//        System.out.println(getNumberOfEmails() + " / " + getNumberOfProfiles());
//        System.out.println(newProfiler.getNumberOfEmails() + " / " + newProfiler.getNumberOfProfiles());
        }
    }

    public int getNumberOfProfilesWithJorunals() {
        int counter = 0;
        for (Profile profile : profiles) {
            if (profile.getJournals().size() > 0)
                counter++;
        }
        return counter;
    }
}
