package nearsoft.academy.bigdata.recommendation;

public class Review {
    private String productId;
    private String userId;


    public Review(){

        }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}