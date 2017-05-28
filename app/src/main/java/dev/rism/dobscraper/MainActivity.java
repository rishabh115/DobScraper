package dev.rism.dobscraper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   Button button;
    EditText picker;
    ListView lv;
    Calendar myCalendar;
    ProgressBar progressBar,pagerProgress;
    DatePickerDialog.OnDateSetListener date;
    PresentationViewPager sliderLayout;
    CirclePageIndicator indicator;

    String [] types=new String[]{"Events","Births","Deaths"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        picker= (EditText) findViewById(R.id.datePicker);
        lv= (ListView) findViewById(R.id.listView);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        pagerProgress= (ProgressBar) findViewById(R.id.progress_pager);
        sliderLayout= (PresentationViewPager) findViewById(R.id.slider);
         indicator = (CirclePageIndicator) findViewById(R.id.tabDots);

        slideshow();
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setError(null);
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (picker.getText().toString().matches(""))
              {
                  picker.setError("Select the date");
                  return;
              }
              else
              {
                //  picker.setText(Config.q1+picker.getText().toString().trim()+Config.q3);
            new Background().execute(Config.q1+picker.getText().toString().trim()+Config.q3);
              }
          }
      });
    }
    class Background extends AsyncTask<String, Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String data="";
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                data=readStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return data;
        }
        private String readStream(InputStream in)
        {
            String s;
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            StringBuilder builder=new StringBuilder();
            try {
                while ((s=bufferedReader.readLine())!=null)
                {
                    builder.append(s);
                }}
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
            return builder.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<ListModel> list=new ArrayList<>();
            try
            {
                int i,length;
                JSONObject jsonObject=new JSONObject(s).getJSONObject("results");
                JSONArray jsonArray=jsonObject.getJSONArray("bindings");
                length=jsonArray.length();
                Toast.makeText(getApplicationContext(),length+" results found",Toast.LENGTH_SHORT).show();
                if(length>0)
                {
                    for (i=0;i<length;i++)
                    {
                        String st,st2;
                        JSONObject jsonObject1=jsonArray.getJSONObject(i).getJSONObject("person");
                        st=jsonObject1.getString("value");
                        JSONObject jsonObject2=jsonArray.getJSONObject(i).getJSONObject("thumbnail");
                        st2=jsonObject2.getString("value");
                        st2=st2.replace("http:","https:");
                        Log.d("URL",st2);
                        st=(st.substring(st.lastIndexOf('/')+1,st.length())).replace('_',' ');
                        ListModel ob=new ListModel();
                        ob.setName(st);
                        ob.setImgurl(st2);
                        list.add(ob);
                    }
                }
                ListAdapter adapter=new ListAdapter(MainActivity.this,list);
                lv.setAdapter(adapter);
            }
            catch (Exception e){}
            finally {
                progressBar.setVisibility(View.GONE);
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListModel ob = (ListModel) parent.getItemAtPosition(position);
                    String name=ob.getName();
                    name.replace(' ','_');
                    Intent intent=new Intent(MainActivity.this,WebActivity.class);
                    intent.putExtra("tag",name);
                    Log.d("tag",name);
                    startActivity(intent);
                }
            });
        }
    }
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        picker.setText(sdf.format(myCalendar.getTime()));
    }
    public void slideshow()
    {
        final ArrayList<SliderModel> list=new ArrayList<>();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Config.TIH, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataObject=response.getJSONObject("data");
                    for (String s:types)
                    {
                        JSONArray typeArray=dataObject.getJSONArray(s);
                        if (list.size()>0)
                        {
                            list.clear();
                        }
                        Log.d("Size of list",typeArray.length()+"");
                        for (int i=0;i<typeArray.length();i++)
                        {
                            JSONObject eventObject=typeArray.getJSONObject(i);
                            String year=eventObject.getString("year");
                            String text=eventObject.getString("text");
                            Log.d("Values",year+","+text);
                            SliderModel sliderModel=new SliderModel();
                            sliderModel.setLink(null);
                            sliderModel.setText(text);
                            sliderModel.setType(s);
                            sliderModel.setYear(year);
                            list.add(sliderModel);
                        }
                    }

                    sliderLayout.setDurationScroll(500);
                    ViewPagerAdapter adapter=new ViewPagerAdapter(MainActivity.this,list);
                    sliderLayout.setAdapter(adapter);
                    indicator.setViewPager(sliderLayout);
                    pagerProgress.setVisibility(View.INVISIBLE);
                    Thread thread=new Thread(){
                        @Override
                        public void run() {
                            int i=0;
                            int size=list.size();
                            while (i<size)
                            {
                                final int j=i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (sliderLayout.isActivated()||sliderLayout.isEnabled())
                                        {

                                                sliderLayout.setCurrentItem(j);

                                    }}});

                                try {
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    i++;
                                    if (i==size){i=0;}
                                }

                            }

                        }
                    };
                    thread.start();
                    //
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        App.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
