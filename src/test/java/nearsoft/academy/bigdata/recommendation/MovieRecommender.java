package nearsoft.academy.bigdata.recommendation;

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

import java.io.*;
import java.util.*;

public class MovieRecommender {

    private ArrayList<Review> reviewList = new ArrayList<>();
    private Set<String> products = new HashSet<String>();
    private Set<String> users = new HashSet<String>();

    private HashMap<String, Integer> usersInts = new HashMap<String, Integer>();

    private HashMap<String, Integer> productsInts = new HashMap<String, Integer>();
    private int counterUser = 0;
    private int counterProduct = 0;
    private String currentProduct;
    private String currentUser;

   // String score;

    public MovieRecommender(String filepath ) throws FileNotFoundException, TasteException {


        try{
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            String newLine;

            Review object = new Review();

            File csvFile = new File("dataBase.csv");
            PrintWriter out = new PrintWriter(csvFile);

            while ((newLine = br.readLine()) != null) {

                String words[] = newLine.split(":");

                if (words[0].equals("product/productId")) {

                    reviewList.add(object);
                    object = new Review();

                    object.setProductId(words[1].trim());
                    products.add(words[1].trim());

                    if(!productsInts.containsKey(words[1].trim())) {
                        productsInts.put(words[1].trim(), counterProduct);
                        counterProduct++;
                    }
                    currentProduct = words[1].trim();

                }

               else if (words[0].equals("review/userId")) {

                    object.setUserId(words[1].trim());

                    users.add(words[1].trim());

                    if(!usersInts.containsKey(words[1].trim())) {
                        usersInts.put(words[1].trim(), counterUser);
                        counterUser++;
                    }
                    currentUser = words[1].trim();


                }
                else if (words[0].equals("review/score")) {

                    object.setScore(words[1].trim());

                    out.println(usersInts.get(currentUser) + "," +productsInts.get(currentProduct) + "," + object.getScore());

                }

            }
            out.close();

        }catch(Exception ex){
            System.out.println("there is no file in the path");
        }
    }

    public int getTotalReviews() {

        return reviewList.size();

    }

    public int getTotalProducts() {

        return products.size();

    }

    public int getTotalUsers() {

        return users.size();

    }

    public List<String> getRecommendationsForUser(String userId) throws IOException, TasteException{

        long userIntValue = usersInts.get(userId);
        List<String> recommendsList = new ArrayList<String>();

        DataModel model = new FileDataModel(new File("dataBase.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        List<RecommendedItem> recommendations = recommender.recommend(userIntValue, 3);
        for (RecommendedItem recommendation : recommendations) {
            recommendsList.add(getProductString((int)recommendation.getItemID()));

        }
        return recommendsList;
    }
    private String getProductString(int val) {
        for(String key : this.productsInts.keySet()){
            if(this.productsInts.get(key) == val){
                return key;
            }
        }
        return null;
    }
}
