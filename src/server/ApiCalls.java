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

        System.out.println("Testing 1 - Send Http GET request");
        String json = http.sendGet();
        http.printPrice(json);

    }

    public String sendGet() throws Exception{
        String url = "https://api.skypicker.com/flights?";
        url += "flyFrom=FNC&to=OPO&dateFrom=02/09/2018";
        url += "&dateTo=03/09/2018&typeFlight=oneway&oneforcity=1";
        url += "&directFlights=1&v=3";

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

    public void printPrice(String json){

            ObjectMapper objMapper = new ObjectMapper();
            try{
                JsonNode rootNode = objMapper.readTree(json);

                for(int i=0; i<1; i++)
                {
                    JsonNode tempNode = rootNode;
                    switch (i)
                    {
                        case(0): String data = tempNode.get("data").toString();
                                 data = data.substring(1, data.length()-1);
                                 tempNode = objMapper.readTree(data);
                                 System.out.println(tempNode.get("conversion").get("EUR").toString()); break;

                        default: break;
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }

    }

    public String createSearchUrl(){
        String searchUrl="";



        return searchUrl;
    }

}
