import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONObject;

public class TestClient extends TweetDetail
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        String bearerToken = System.getenv("BEARER_TOKEN");
        if (null != bearerToken)
        {
            ArrayList idList = search("Donald Trump", bearerToken);
            for(int i = 0; i < idList.size(); i++)
            {
                String tweetDetail = getTweets(idList.get(i).toString(), bearerToken);
                JSONObject jsonObject = new JSONObject(tweetDetail);
                JSONArray data = jsonObject.getJSONArray("data");
                JSONObject re = data.getJSONObject(0);
                System.out.println(re);
//                System.out.println(re.getJSONObject("public_metrics").getInt("like_count"));
            }
        }
        else
        {
            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }
}
