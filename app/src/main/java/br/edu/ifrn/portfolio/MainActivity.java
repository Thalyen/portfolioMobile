package br.edu.ifrn.portfolio;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import br.edu.ifrn.portfolio.modelo.Area;

public class MainActivity extends AppCompatActivity {

    private List<Area> areas;
    private Area areaE;
    private Spinner exatasV;
    private String subarea;
    private TableLayout tableLayout;
    private final int ITEM_HOME = Menu.FIRST;
    private final int ITEM_laboratorios = Menu.FIRST + 1;
    private final int ITEM_SOBRE = Menu.FIRST + 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ITEM_HOME, Menu.NONE, R.string.home);
        menu.add(Menu.NONE, ITEM_laboratorios, Menu.NONE, R.string.lab);
        menu.add(Menu.NONE, ITEM_SOBRE, Menu.NONE, R.string.sobre);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ITEM_HOME :
                break;
            case ITEM_laboratorios :

            case ITEM_SOBRE :
                Intent sobre = new Intent(this, SobreActivity.class);
                startActivity(sobre); break;
            case R.id.action_close :
                finish(); break;
        }
        return true;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.tableArea);
        exatasV = (Spinner) findViewById(R.id.areasExastas);

        setOpcoesSubarea(exatasV);
    }


    public void getAreas(View view) {
        String url = "http://parcerias.ifrn.edu.br/portfolio/dados/areas.json";
        new comunicacao().execute("GET", url);
    }

    public class comunicacao extends AsyncTask<String, Void, String> {

        private String acao = null;
        private URL url = null;
        private String method;
        private HttpURLConnection urlConnection = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                method = params[0];
                url = new URL(params[1]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod(method);
                urlConnection.connect();
                int response = urlConnection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    Reader reader = new InputStreamReader(urlConnection.getInputStream());
                    Gson gson = new Gson();
                    areaE = gson.fromJson(reader, Area.class);
                } else {
                    trataErro(response);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_area, null);
            TextView nome = (TextView) row.findViewById(R.id.trAreaNome);
            /*for (Area a : areas) {
                nome.setText(a.getNome());
                break;
            }*/
            nome.setText(areaE.getNome());
            tableLayout.addView(row);
        }

        // Depois vale a pena expandir esse método!
        private void trataErro(int response) {
        }
    }

    protected void setOpcoesSubarea(final Spinner exatasV) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subexatas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exatasV.setAdapter(adapter);

        exatasV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Toast.makeText(parent.getContext(),
                        subarea = parent.getItemAtPosition(i).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
