package ddapp_project.ddapp;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String firstName;
    private String lastName;
    private String id;
    private String birthday;
    private float coutse_0;
    private float course_1;
    private float course_2;
    private float course_3;
    private float gpa;

    private ApiEndpointInterface apiService;
    private ProgressBar progressBar;
    Button btn;

    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    private String[] from;
    private int[] to;
    int index;
    boolean loading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);
        from = new String[] { DB.COLUMN_STUDENT_FIRST_NAME, DB.COLUMN_BIRTHDAY };
        to = new int[] { R.id.textFirstName, R.id.textBirthday };
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lvData = (ListView)findViewById(R.id.list);
        initRetrofit();
        doRequest();
        scAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.element, cursor, from, to);
        lvData.setAdapter(scAdapter);
        btn = (Button) findViewById(R.id.btn);

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.limit+=20;
                cursor = db.getAllData();
                scAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.element, cursor, from, to);
                lvData.setAdapter(scAdapter);
            }
        });*/

        lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount>= totalItemCount ){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    DB.limit+=20;
                    cursor = db.getAllData();
                    scAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.element, cursor, from, to);
                    scAdapter.notifyDataSetChanged();
                    lvData.setAdapter(scAdapter);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });

    }

    private void downloadWhithBase(){

    }

    private void doRequest() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Call<List<Student>> call = apiService.getStudents();
                call.enqueue(new Callback<List<Student>>() {
                    @Override
                    public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                        /**
                         * Вот твой список студентов :
                         * response.body()
                         * :)
                         */
                        Log.d(TAG, "onResponse: response.isSuccessful() " + response.isSuccessful());
                        Log.d(TAG, "onResponse: response.size() = " + response.body().size());
                        for(int i = 0; i < response.body().size(); i++) {
                            Log.d(TAG, "onResponse: getFirstName " + response.body().get(i).getFirstName());
                            firstName = response.body().get(i).getFirstName();
                            Log.d(TAG, "onResponse: getLastName " + response.body().get(i).getLastName());
                            lastName = response.body().get(i).getLastName();
                            Log.d(TAG, "onResponse: getBirthday " + response.body().get(i).getBirthday());
                            birthday = response.body().get(i).getBirthday();
                            Log.d(TAG, "onResponse: getID " + response.body().get(i).getId());
                            id = response.body().get(i).getId();
                            Courses[] curs = response.body().get(i).getCourses();
                            for(int j = 0; j < curs.length; j++) {
                                Log.d(TAG, "onResponse: getID " + curs[j].getName() + " " + curs[j].getMark());

                                if(j==0) {
                                    coutse_0 = Float.parseFloat(curs[j].getMark());
                                }else if (j==1) {
                                    course_1 = Float.parseFloat(curs[j].getMark());
                                }else if (j==2) {
                                    course_2 = Float.parseFloat(curs[j].getMark());
                                }else if (j==3) {
                                    course_3 = Float.parseFloat(curs[j].getMark());
                                    gpa = (coutse_0+course_1+course_2+course_3)/4;
                                }
                            }
                            db.addRec(firstName,lastName,id,birthday,(int)coutse_0,(int)course_1,(int)course_2,(int)course_3,gpa);
                            Log.d(TAG, "onResponse: getGPA " + gpa);
                            loading = true;
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Student>> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
        thread.start();
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ddapp-sfa-api-dev.azurewebsites.net/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(ApiEndpointInterface.class);
    }
    public void onDestroy(){
        super.onDestroy();
        cursor = db.delALL();
    }

}
