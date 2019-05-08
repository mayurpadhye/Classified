package mimosale.com.home;

public class Section {
    String cat_id,cat_name,image;

    public boolean isExpanded;

    public Section(String cat_name, String image) {
        this.cat_name = cat_name;
        this.image = image;
        isExpanded = true;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return cat_name;
    }
}
