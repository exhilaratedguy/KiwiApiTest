package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiCalls {

    private static final String TESTURL = "https://api.skypicker.com/flights?flyFrom=FNC&to=europe&dateFrom=18/10/2018&dateTo=30/10/2018&typeFlight=oneway&oneforcity=1&directFlights=1&v=3&limit=5";

    public static void main(String[] args) throws Exception {
        ApiCalls http = new ApiCalls();

        String json = http.sendGet(TESTURL);
        http.printPrice(json, 5);

    }

    public String sendGet(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending GET request to URL: " + url);
            System.out.println("Response code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

            return response.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void printPrice(String json, int n_limit){

        ObjectMapper objMapper = new ObjectMapper();
        try{
            JsonNode rootNode = objMapper.readTree(json);

            JsonNode tempNode = rootNode;
            tempNode = tempNode.get("data");

            //for searches with multiple results
            for(int i=0; i<n_limit; i++)
            {
                tempNode = rootNode;
                tempNode = tempNode.get("data");
                tempNode = tempNode.get(i);
                String info = tempNode.get("conversion").get("EUR").toString() + "€ \t";

                long dateUnix = Long.parseLong(tempNode.get("dTimeUTC").toString());
                Date date = new Date (dateUnix * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
                info += sdf.format(date) + "\t";

                info += tempNode.get("cityTo").toString() + ", " + tempNode.get("countryTo").get("name").toString();

                info = info.replaceAll("\"", "");
                System.out.println(info);


                Calendar cal = Calendar.getInstance();
                System.out.println();
            }
        } catch (IOException e) { e.printStackTrace(); }

    }

    public JsonNode rootNodeTree(String url) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode rootNode = objMapper.readTree(sendGet(url));
        return rootNode;
    }

    public String createSearchUrl(String dFrom, String dTo, String type, int max){
        String url = "https://api.skypicker.com/flights?flyFrom=FNC&to=europe";

        url += "&dateFrom=" + dFrom +"&dateTo=" + dTo;
        url += "&typeFlight=oneway&oneforcity=1";
        url += "&directFlights=" + type + "&v=3&limit=" + max;

        return url;
    }

}
