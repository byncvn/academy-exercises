package nearsoft.academy.bigdata.recommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MovieRecommender {

    private final String PRODUCT_ID_IDENTIFIER = "product/productId";
    private final String USER_ID_IDENTIFIER = "review/userId";
    private final String SCORE_IDENTIFIER = "review/score";

    private List<Review> reviewList = new ArrayList();
    private int reviewCounter = 0;
    private Map<String, Integer> usersMap = new HashMap<>();
    private Map<String, Integer> productsMap = new HashMap<>();
    private Map<Integer, String> productsIdMap = new HashMap<>();

    public MovieRecommender(String filepath)  {//no need for the extra throws words
        File csvFile = new File("dataBase.csv");

        //All the lines (46-48) are using resources that we need to close after the processing, this "try-with-resources" will handle that so we dont need to do it manually
        try (FileReader fr = new FileReader(filepath);
             BufferedReader br = new BufferedReader(fr);
             PrintWriter out = new PrintWriter(csvFile)) {

            String newLine;
            Review review = new Review();
            String currentProduct = "";
            String currentUser = "";
            while ((newLine = br.readLine()) != null) {
                if (newLine.contains(":")) {//Using this to avoid splitting and processing empty lines
                    String line[] = newLine.split(":");
                    String trimmedValue = line[1].trim();
                    switch (line[0]) {//Added a switch statement to avoid multiple ifs
                        case PRODUCT_ID_IDENTIFIER:// added the identifier words as constant, for better readability
                            //We created a new review if we found a ProductId identifier
                            // reviewList.add(review);//This is an scalable solution if we want to process the reviews on future iteration but for now we only need the size
                            reviewCounter++;
                            review = new Review();
                            review.setProductId(trimmedValue);
                            currentProduct = trimmedValue;
                            if(!productsMap.containsKey(currentProduct)){
                                int index= productsMap.size();
                                productsMap.put(currentProduct,index);
                                productsIdMap.put(index,currentProduct);//created another map to store id-value structure for better processing
                            }
                            break;
                        case USER_ID_IDENTIFIER:
                            review.setUserId(trimmedValue);
                            currentUser = trimmedValue;
                            if(!usersMap.containsKey(currentUser)){
                                int index= usersMap.size();
                                usersMap.put(currentUser,index);
                            }
                            break;
                        case SCORE_IDENTIFIER:
                            review.setScore(trimmedValue);
                            out.println(usersMap.get(currentUser)+ "," +productsMap.get(currentProduct) + "," + review.getScore());
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("There was an exception running the process:" + ex.getStackTrace());//Added a more clear error description
        }
    }

    public int getTotalReviews() {
        return reviewCounter;
    }

    public int getTotalProducts() {
        return productsMap.size();
    }

    public int getTotalUsers() {
        return usersMap.size();
    }

    public List<String> getRecommendationsForUser(String userId) throws IOException, TasteException {
        DataModel model = new FileDataModel(new File("dataBase.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(usersMap.get(userId), 3);

        return recommendations.stream().map(r->productsIdMap.get((int)r.getItemID())).collect(Collectors.toList());//using Java 8 streams to search on the productIdMap
    }

}
