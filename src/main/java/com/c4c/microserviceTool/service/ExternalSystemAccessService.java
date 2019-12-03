package com.c4c.microserviceTool.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExternalSystemAccessService {


    public static final String HOST_ET_CORESYS = "https://et.coresystems.net";

    public static final String HOST_QT_CORESYS = "https://qt.coresystems.net";

    public static final String HOST_SB_CORESYS = "https://sb.coresystems.net";

    public static final String ENDPOINT_PRODUCT = "/cloud-product-service/api/product/v1/products";

    public static final String ENDPOINT_PRODUCT_STATUS = "/cloud-product-service/api/product/v1/configuration/product-status";

    public String getData(){
        try{
            URL url = new URL("https://qt.coresystems.net/cloud-product-service/api/product/v1/configuration/product-status");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString();
        }catch(IOException ex){
            return null;
        }
    }

    public int postData(String dataContent, String systemHost, String endPoint){
        try {
            URL url  = new URL(systemHost + endPoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-User-Name", "i043125");
            conn.setDoOutput(true);
            try(OutputStream os = conn.getOutputStream()){
                byte[] input = dataContent.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = conn.getResponseCode();
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 500;
    }

    public Map<Integer, String> postDataArray(List<String> dataContentList, String systemHost, String endPoint){
        HttpURLConnection conn = null;
        DataOutputStream wr = null;
        Map<Integer, String> responesMap = new HashMap<>();
        try {
            URL url  = new URL(systemHost + endPoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-User-Name", "i043125");
            conn.setDoOutput(true);
            for(String dataContent: dataContentList){
                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(dataContent);
                wr.flush();
                int responseCode = conn.getResponseCode();
                responesMap.put(responseCode, dataContent);
            }
            if(wr!= null){
                wr.close();
            }
            return responesMap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(conn != null){
                conn.disconnect();
            }
            return responesMap;
        }
    }
}
