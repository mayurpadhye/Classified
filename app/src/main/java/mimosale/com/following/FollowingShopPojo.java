package mimosale.com.following;

public class FollowingShopPojo {
    String shop_id,shop_name,shop_image;

    public FollowingShopPojo(String shop_id, String shop_name, String shop_image) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_image = shop_image;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }
}
