package ZapposGiftSearch.Model;

/**
 * This class is used to store product information
 * received from API call.
 */
public class Product {
    private String name;
    private String brandName;
    private String price;
    private String thumbnailURL;

    public Product(String name, String brandName, String price, String thumbnailURL) {
        this.name = name;
        this.brandName = brandName;
        this.price = price;
        this.thumbnailURL = thumbnailURL;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
