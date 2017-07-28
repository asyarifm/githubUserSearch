package usersearch.github.com.githubusersearch;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private SearchView searchBar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        searchBar = (SearchView) findViewById(R.id.searchBar);
        listView = (ListView) findViewById(R.id.listView);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                    OnlineService task = new OnlineService(MainActivity.this, MainActivity.this);
                    task.execute(query);
                return false;
            }
        });
    }

    @Override
    public void onTaskCompleted(ListAdapter result) {
        if (!result.isEmpty() && result != null) {
            listView.setAdapter(result);
        } else {
            Toast.makeText(this, "No user found.", Toast.LENGTH_LONG).show();
        }

    }
}
