package mimosale.com.home.preferences;

public class PreferenceListModel {

    String pref_if,pref_name,pref_image,description,status;

    public PreferenceListModel(String pref_if, String pref_name, String pref_image,String description,String status) {
        this.pref_if = pref_if;
        this.pref_name = pref_name;
        this.pref_image = pref_image;
        this.description=description;
        this.status=status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPref_if() {
        return pref_if;
    }

    public void setPref_if(String pref_if) {
        this.pref_if = pref_if;
    }

    public String getPref_name() {
        return pref_name;
    }

    public void setPref_name(String pref_name) {
        this.pref_name = pref_name;
    }

    public String getPref_image() {
        return pref_image;
    }

    public void setPref_image(String pref_image) {
        this.pref_image = pref_image;
    }
}
