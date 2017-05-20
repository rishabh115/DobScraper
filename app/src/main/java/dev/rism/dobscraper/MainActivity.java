package dev.rism.dobscraper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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
    ProgressBar progressBar;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        picker= (EditText) findViewById(R.id.datePicker);
        lv= (ListView) findViewById(R.id.listView);
        progressBar=(ProgressBar) findViewById(R.id.progress);
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
            ArrayList<String> list=new ArrayList<>();
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
                        String st;
                        JSONObject jsonObject1=jsonArray.getJSONObject(i).getJSONObject("person");
                        st=jsonObject1.getString("value");
                        list.add((st.substring(st.lastIndexOf('/')+1,st.length())).replace('_',' '));
                    }
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,list);
                lv.setAdapter(adapter);
            }
            catch (Exception e){}
            finally {
                progressBar.setVisibility(View.GONE);
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = (String) parent.getItemAtPosition(position);
                    name.replace(' ','_');
                    Intent intent=new Intent(MainActivity.this,WebActivity.class);
                    intent.putExtra("tag",name);
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
}
