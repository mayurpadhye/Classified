package mimosale.com.shop.pojoClass;

public class ShopImagesPojo {
    String id,shop_image;

    public ShopImagesPojo(String id, String shop_image) {
        this.id = id;
        this.shop_image = shop_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }
}
