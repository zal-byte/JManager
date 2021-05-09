package Client;

import android.os.Environment;
import android.widget.Toast;

import com.zadev.jmanage.MainActivity;

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
    public String GET(String url) {
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

    public String POST(String url, HashMap<String, String> param) {
        String res = "";
        try {
            URL site = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) site.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(270000);
            connection.setConnectTimeout(270000);
            connection.addRequestProperty("Content-Type","application/x-www-form-urlencoded, charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(getParamData(param));
            bw.flush();
            bw.close();
            os.close();

            int CODE = connection.getResponseCode();
            if (CODE == HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    res += line;
                }
                br.close();
            }else{
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getParamData(@NotNull HashMap<String, String> param) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean status = true;
        for (Map.Entry<String, String> map : param.entrySet()) {
            if (status)
                status = false;
            else
                sb.append("&");
            sb.append(URLEncoder.encode(map.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(map.getValue(), "UTF-8"));
        }
        return sb.toString();
    }
}

public class Client extends Handler {
    public String url = "";
    public String login;
    public String product;
    public String profile;
    public String post;
    public String imgProdSrc;
    public String imgProfSrc;
    public String path;

    public Client() {
        this.url = loadURL();
        if ((this.url == null) || !this.url.isEmpty()) {

            this.login = url + "/index.php";
            this.product = url + "/index.php";
            this.profile = url + "/index.php";
            this.post = url + "/index.php";
            this.imgProdSrc = url + "/lib/img/product/";
            this.imgProfSrc = url + "/lib/img/profilepengguna/";
        }
    }

    public String loadURL() {
        String final_res = "";
        path = Environment.getExternalStorageDirectory().getPath() + "/Jamuku/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        String filename = "config.txt";
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path + filename));
            byte data;
            while ((data = (byte) bis.read()) != -1) {
                final_res += (char) data;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return final_res;
    }

    @Override
    public String POST(String url, HashMap<String, String> param) {
        return super.POST(url, param);
    }

    @Override
    public String GET(String url) {
        return super.GET(url);
    }
}
