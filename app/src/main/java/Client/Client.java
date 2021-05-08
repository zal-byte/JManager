package Client;

import android.os.Environment;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

class Handler {
    protected String GET(String url) {
        try {
            URL site = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) site.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(270000);
            connection.setConnectTimeout(270000);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");

            StringBuilder result = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String POST(String url, HashMap<String, String> param) {
        try {
            URL site = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) site.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setReadTimeout(270000);
            connection.setConnectTimeout(270000);

            StringBuilder result = new StringBuilder();
            OutputStream os = connection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(getParamData(param));
            bw.flush();
            bw.close();
            os.flush();
            os.close();

            int CODE = connection.getResponseCode();
            if (CODE == HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    String getParamData(@NotNull HashMap<String, String> param) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean status = true;
        for (Map.Entry<String, String> map : param.entrySet()) {
            if (status)
                status = false;
            else
                sb.append("&");
            sb.append(URLEncoder.encode(map.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(map.getValue(), "UTF8"));
        }
        return sb.toString();
    }
}

public class Client extends Handler {
    public String url = "";

    public Client() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Jamuku/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        String filename = "config.txt";
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file + filename));
            byte data;
            StringBuilder final_url = new StringBuilder();
            while ((data = (byte) bis.read()) != -1) {
                final_url.append((char) data);
            }
            this.url = final_url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String POST(String url, HashMap<String, String> param) {
        return super.POST(url, param);
    }

    @Override
    protected String GET(String url) {
        return super.GET(url);
    }
}
