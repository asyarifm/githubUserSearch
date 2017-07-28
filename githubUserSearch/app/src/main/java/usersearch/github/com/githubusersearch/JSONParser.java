package usersearch.github.com.githubusersearch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to extract information from JSON
 */
public class JSONParser {
    private ArrayList<List> userList;

    public JSONParser() {
        userList = new ArrayList<List>();
    }

    public void renderUser(JSONObject json) {
        try {
            if (json.getInt("total_count") > 0) {
                //put user information as a JSONArray
                JSONArray arrJson= json.getJSONArray("items");
                String username = "";
                String avatarUrl = "";

                //get user information for each user
                for (int i=0; i < arrJson.length(); i++) {
                    //getusername
                    username = arrJson.getJSONObject(i).getString("login");
                    //getAvatarURL
                    avatarUrl = arrJson.getJSONObject(i).getString("avatar_url").replace("\\","");
                    userList.add(i, Arrays.asList(username, avatarUrl));
                }
            }

        } catch (Exception e) {
            Log.e("GITHUBSEARCH", "One or more fields not found in the JSON data");
        }
    }

    public ArrayList<List> getUserList() {
        return userList;
    }
}
