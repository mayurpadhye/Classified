package mimosale.com.post;

public class SalePreviewPojo {
    String product_image;
    String product_name;
    String product_id;

    public SalePreviewPojo(String product_image, String product_name, String product_id) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
