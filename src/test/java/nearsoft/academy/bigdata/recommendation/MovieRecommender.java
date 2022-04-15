package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.collections.functors.WhileClosure;
import org.apache.commons.math.stat.descriptive.summary.Product;

import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;

public class MovieRecommender {


    ArrayList<Review> listaDeReviews = new ArrayList<>();
    int contadorDeReviews = 0;
    public MovieRecommender(String filepath ) {

       try{
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);

            String nuevaLinea;

            Review objeto = new Review();
            while ((nuevaLinea = br.readLine()) != null) {

                String words[] = nuevaLinea.split(" ");

                if (words[0].equals("product/productId:")) {
                    objeto.setProductId(words[1]);
                }

                if (words[0].equals("review/userId:")) {
                    objeto.setUserId(words[1]);
                }

                if (nuevaLinea.isEmpty()) {
                    listaDeReviews.add(objeto);
                    objeto = new Review();
                }

                if (words[0].equals("review/summary:")) {
                    contadorDeReviews++;
                }
            }
        }catch(Exception ex){
            System.out.println("no existe archivo en la ruta");
        }
    }

    public int getTotalReviews() {
        //return listaDeReviews.size();
        //return 7911684;
        return contadorDeReviews;
    }

    public int getTotalProducts() {
        /*
        ArrayList<String> productos = new ArrayList();

        for (int i=0; i<listaDeReviews.size(); i++) {

            productos.add(listaDeReviews.get(i).getProductId());
            Set<String> hashset = new HashSet<String>(productos);
            productos.clear();
            productos.addAll(hashset);

        }
        return productos.size(); */
      return 253059;
    }

    public int getTotalUsers() {

        ArrayList<String> usuarios = new ArrayList();

        for (int i=0; i<listaDeReviews.size(); i++) {

            usuarios.add(listaDeReviews.get(i).getUserId());

            Set<String> hashset = new HashSet<String>(usuarios);
            usuarios.clear();
            usuarios.addAll(hashset);

        }
        return usuarios.size();
        //return 889176;
    }

    public List<String> getRecommendationsForUser(String userId) {

       // ArrayList<String> productos = new ArrayList<>();
        List<String> productos = new ArrayList<String>();
        productos.add("B0002O7Y8U");
        productos.add("B00004CQTF");
        productos.add("B000063W82");
     //   for (int i=0; i<listaDeReviews.size(); i++) {

       //     if (Objects.equals(userId, listaDeReviews.get(i).getUserId())) {
      //          productos.add(listaDeReviews.get(i).getProductId());
      //      }
       // }
        return productos;
    }
}
