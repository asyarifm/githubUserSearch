package usersearch.github.com.githubusersearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlineService extends AsyncTask<String, Integer, String> {

    //declare ontaskcomplete interface
    private OnTaskCompleted taskCompleted;

    //init variable
    private HttpURLConnection urlConnection = null;
    private int timeout = 30000;
    private ProgressDialog pd;
    private Activity act;
    private ListAdapter listAdapter;

    public OnlineService(MainActivity act, OnTaskCompleted c) {
        this.taskCompleted = c;
        this.act = act;
    }

    /**
     * asyntask process.
     */
    @Override
    protected String doInBackground(String... arg0) {
        String result = "";

        //get username from input parameter
        String username = (String) arg0[0];

        //link to fetch user data
        String url = "https://api.github.com/search/users?q=" + username;
        Log.d("GITHUBSEARCH", "URL: " + url);

        //request user data
        result = reqUserData(url);

        if (result.isEmpty() || result == null || result.contentEquals("null") || result.equalsIgnoreCase("1")) {
            return "1";
        }

        ArrayList<List> userList = null;
        try {
            //convert result to JSONObj
            JSONObject obj = new JSONObject(result);

            if(!obj.toString().isEmpty()){
                //parse JSONObj
                JSONParser parser = new JSONParser();
                parser.renderUser(obj);
                //get JSON parsing result
                userList = parser.getUserList();
            } else {
                return "2";
            }
        } catch (Throwable t) {
            //Toast.makeText(this, "No data retrieved from GITHUB, please check your input and your internet connection", Toast.LENGTH_LONG).show();
            Log.e("GITHUBSEARCH", "Could not parse malformed JSON: \"" + result + "\"");
        }
        listAdapter = new ListAdapter(act, userList);

        return "0";
    }

    protected void onPreExecute(){
        pd = new ProgressDialog(act);
        pd.setMessage("Loading, please wait.");
        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                urlConnection.disconnect();
                return true;
            }
        });
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    /**
     * retrieve asynctask result.
     */
    @Override
    protected void onPostExecute(String result) {
        if (pd.isShowing()) {
            pd.dismiss();
        }

        if (result.equalsIgnoreCase("0")) {
            //forward the result to onTaskCompleted interface
            taskCompleted.onTaskCompleted(listAdapter);
        } else if (result.equalsIgnoreCase("1")) {
            Toast.makeText(act, "Please check your internet connection", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(act, "No data found", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Fetch JSON data from GITHUB api
     */
    public String reqUserData(String link) {
        String result = "";
        Log.d("GITHUBSEARCH", "RequestUserData");

        URL url = null;
        try {
            url = new URL(link);
            //init connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(timeout);
            urlConnection.connect();

            //fetching data
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine())!=null) {
                builder.append(line + "\n");
            }
            //finish fetching data
            is.close();

            //set the retrieved data as a result
            result = builder.toString();
            urlConnection.disconnect();

        } catch (Exception e) {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            result = "1";
            Log.e("GITHUBSEARCH", e.getMessage());
        }

        return result;
    }
}
