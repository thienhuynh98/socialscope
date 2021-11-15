import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class TweetSearch {

    public static ArrayList search(String searchString, String bearerToken) throws IOException, URISyntaxException
    {
        ArrayList <BigInteger> idList = new ArrayList<>();

        String searchResponse = "";

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("expansions","referenced_tweets.id"));
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity)
        {
            searchResponse = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = new JSONObject(searchResponse);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                String s = data.getJSONObject(i).toString();
                if(s.contains("type"))
                {
                    JSONArray rt = data.getJSONObject(i).getJSONArray("referenced_tweets");
                    idList.add(rt.getJSONObject(0).getBigInteger("id"));
                }
                else
                {
                    idList.add(data.getJSONObject(i).getBigInteger("id"));
                }
            }
        }
        return idList;
    }
}
