package gavengers.wag;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    public static String sendREST(String sendUrl, String jsonValue) throws IllegalStateException {

        String inputLine = null;
        StringBuffer outResult = new StringBuffer();

        try{
            Log.d("sendREST", "sentREST start");
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            OutputStream os = conn.getOutputStream();
            os.write(jsonValue.getBytes("UTF-8"));
            os.flush();

            // 리턴된 결과 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            conn.disconnect();
            Log.d("sendREST", "sentREST End");
        }catch(Exception e){
            Log.w("sendREST", e.getMessage(), e);
            e.printStackTrace();
        }

        return outResult.toString();
    }
}
