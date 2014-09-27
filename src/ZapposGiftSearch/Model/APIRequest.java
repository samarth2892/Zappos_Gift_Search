package ZapposGiftSearch.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Class to make Http request and process JSON
 * @version 1.0
 * @author Samarth Patel
 */
public class APIRequest {

    private HttpClient client = HttpClientBuilder.create().build();
    private static final String zapposSearchUrl = "http://api.zappos.com/Search";
    private static final String KEY = "key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";
    private String sortOption;
    private String limit;
    private String excludes;

    /**
     * search method is used to make Http call and given the constraints
     * limit of results, keyword to search related items, upper price limit
     * @param noOfProducts Limit of results
     * @param keyword Keyword for what we are looking for.
     * @param priceLimit upper price limit
     * @return ArrayList<Product> (returns an ArrayList of GiftSearchModel.Product objects)
     */
    public ArrayList<Product> search(double noOfProducts, String keyword, double priceLimit) {

        ArrayList<Product> products = new ArrayList<Product>();

        try {
            sortOption = "sort=" + URLEncoder.encode("{\"price\":\"asc\"}", "UTF-8");
            limit = "limit=" + Integer.toString((int) Math.round(noOfProducts));
            excludes = "excludes=" +  URLEncoder.encode("[\"styleId\",\"originalPrice\",\"productUrl\"," +
                    "\"colorId\",\"percentOff\",\"productId\"]", "UTF-8");
            String url = zapposSearchUrl + "?" + KEY + "&" + limit + "&" + sortOption + "&" + excludes;

            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = client.execute(getRequest);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            JsonParser parser = new JsonParser();
            JsonObject resultJson = (JsonObject) parser.parse(responseString);
            JsonArray resultsArray = resultJson.getAsJsonArray("results");

            for(JsonElement r : resultsArray) {
                JsonObject result = r.getAsJsonObject();
                String price = result.get("price").toString().replace("\"", "");
                String name = result.get("productName").toString().replace("\"", "");
                String brandName = result.get("brandName").toString().replace("\"", "");
                String thumnailURL = result.get("thumbnailImageUrl").toString().replace("\"", "");

                if(Double.parseDouble(price.substring(1)) > priceLimit)
                   break;
                Product product = new Product(name, brandName, price, thumnailURL);
                products.add(product);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
