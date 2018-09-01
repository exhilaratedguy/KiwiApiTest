package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiCalls {

    public static void main(String[] args) throws Exception {
        ApiCalls http = new ApiCalls();

        String json = http.sendGet();
        http.printPrice(json, 3);

    }

    public String sendGet() throws Exception{
        String url = "https://api.skypicker.com/flights?";
        url += "flyFrom=FNC&to=europe&dateFrom=03/09/2018";
        url += "&dateTo=05/09/2018&typeFlight=oneway&oneforcity=1";
        url += "&directFlights=1&v=3&limit=3";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending GET request to URL: " + url);
        System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ){
            response.append(inputLine);
        }
        in.close();
        //System.out.println(response.toString());

        return response.toString();
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
                info += tempNode.get("cityTo").toString() + ", " + tempNode.get("countryTo").get("name").toString();
                //info += tempNode.get
                info = info.replaceAll("\"", "");
                System.out.println(info);
            }
        } catch (IOException e) { e.printStackTrace(); }

    }

    public String createSearchUrl(){
        String searchUrl="";



        return searchUrl;
    }

}
