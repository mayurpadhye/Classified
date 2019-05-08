package mimosale.com.home.fragments;

public class AllProductPojo {

String id,name,shop_id,user_id,description,price,hash_tag,status,product_images,image2,discount;

    public AllProductPojo(String id, String name, String shop_id, String user_id, String description, String price, String hash_tag, String status, String product_images,String image2) {
        this.id = id;
        this.name = name;
        this.shop_id = shop_id;
        this.user_id = user_id;
        this.description = description;
        this.price = price;
        this.hash_tag = hash_tag;
        this.status = status;
        this.image2=image2;
        this.product_images = product_images;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHash_tag() {
        return hash_tag;
    }

    public void setHash_tag(String hash_tag) {
        this.hash_tag = hash_tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_images() {
        return product_images;
    }

    public void setProduct_images(String product_images) {
        this.product_images = product_images;
    }
}
