package mimosale.com.preferences;

public class AllPrefPojo {
    String pref_name,pref_image,pref_id,selected;

    public AllPrefPojo(String pref_name, String pref_image, String pref_id, String selected) {
        this.pref_name = pref_name;
        this.pref_image = pref_image;
        this.pref_id = pref_id;
        this.selected = selected;
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

    public String getPref_id() {
        return pref_id;
    }

    public void setPref_id(String pref_id) {
        this.pref_id = pref_id;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
