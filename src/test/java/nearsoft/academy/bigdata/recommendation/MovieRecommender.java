package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.collections.functors.WhileClosure;
import org.apache.commons.math.stat.descriptive.summary.Product;

import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;

public class MovieRecommender {

    private ArrayList<Review> reviewList = new ArrayList<>();
    private Set<String> products = new HashSet<String>();
    private Set<String> users = new HashSet<String>();
    public MovieRecommender(String filepath ) {

        try{
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            String newLine;

            Review object = new Review();

            while ((newLine = br.readLine()) != null) {

                String words[] = newLine.split(":");

                if (words[0].equals("product/productId")) {

                    reviewList.add(object);
                    object = new Review();

                    object.setProductId(words[1].trim());
                    products.add(words[1].trim());

                }

               else if (words[0].equals("review/userId")) {

                    object.setUserId(words[1].trim());

                    users.add(words[1].trim());

                }
            }

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

    public List<String> getRecommendationsForUser(String userId) {

        List<String> productlist = new ArrayList<String>();

           for (int i=0; i<reviewList.size(); i++) {

               String username = reviewList.get(i).getUserId();

                  if(Objects.equals(username, userId)){

             productlist.add(reviewList.get(i).getProductId());
          }

         }
        return productlist;
    }
}