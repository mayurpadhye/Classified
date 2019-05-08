package mimosale.com.my_posting;

public class MySaleModel {
    String id,name,shop_id,user_id,start_date,end_date,min_discount,max_discount,description,web_url,hash_tags,status1,image1,image2,shop_name;

    public MySaleModel(String id, String name, String shop_id, String user_id, String start_date, String end_date, String min_discount, String max_discount, String description, String web_url, String hash_tags, String status1, String image1, String image2, String shop_name) {
        this.id = id;
        this.name = name;
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.min_discount = min_discount;
        this.max_discount = max_discount;
        this.description = description;
        this.web_url = web_url;
        this.hash_tags = hash_tags;
        this.status1 = status1;
        this.image1 = image1;
        this.image2 = image2;
        this.shop_name = shop_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getMin_discount() {
        return min_discount;
    }

    public void setMin_discount(String min_discount) {
        this.min_discount = min_discount;
    }

    public String getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(String max_discount) {
        this.max_discount = max_discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getHash_tags() {
        return hash_tags;
    }

    public void setHash_tags(String hash_tags) {
        this.hash_tags = hash_tags;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
