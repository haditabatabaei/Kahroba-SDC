import java.io.Serializable;
import java.util.ArrayList;

public class Profiler implements Serializable {
    //default serialVersion id
    private static final long serialVersionUID = 1L;

    private ArrayList<Profile> profiles;

    public Profiler(){
        profiles = new ArrayList<Profile>();
    }

    public void addProfile(Profile profile){
        profiles.add(profile);
    }

    public void addEmptyProfile(){
        profiles.add(new Profile());
    }

    public Profile getRecentlyAddedProfile(){
        return profiles.get(profiles.size() - 1);
    }

    public ArrayList<Profile> getProfiles(){
        return profiles;
    }

    public Profile findProfileByFullName(String fullName){
        for(Profile profile : profiles){
            if(profile.getFullName().equals(fullName))
                return profile;
        }
        return null;
    }

    public int getNumberOfProfiles(){
        return profiles.size();
    }

    public int getNumberOfEmails(){
        int counter = 0;
        for(Profile profile : profiles){
            if(!(profile.getEmail() == null || profile.getEmail().isEmpty())){
                counter++;
            }
        }
        return counter;
    }

    public void printAllProfilesWithEmail(){
        for(Profile profile : profiles){
            if(!(profile.getEmail() == null || profile.getEmail().isEmpty())){
                profile.printSummary();
            }
        }
    }

    public void printProfiles(){
        for(Profile profile : profiles){
            System.out.println("PROFILE INDEX : " + profiles.indexOf(profile) + " / " + profiles.size());
            profile.print();
        }
    }

    public void printSummaryProfiles(){
        for(Profile profile : profiles){
            System.out.println("PROFILE NUM : " + (profiles.indexOf(profile) + 1 ) + " / " + profiles.size());
            profile.printSummary();
        }
    }
}
