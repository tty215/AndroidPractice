package com.example.androidpracticeforjava;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WeatherFragment extends Fragment {

    private Activity _parentActivity;
    private boolean _isLayoutLand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _parentActivity = getActivity();

        FragmentManager manager = getFragmentManager();
        MenuListFragment menuListFragment = (MenuListFragment)manager.findFragmentById(R.id.fragmentMenuList);

        _isLayoutLand = menuListFragment != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        Bundle bundle;

        if (_isLayoutLand) {
            bundle = getArguments();
        }
        else {
            Intent intent = _parentActivity.getIntent();
            bundle = intent.getExtras();
        }

        String menuName = bundle.getString("menuName");
        String menuSubdata = bundle.getString("menuSubdata");

        TextView tvCityName = view.findViewById(R.id.tvCityName);
        TextView tvWeather = view.findViewById(R.id.tvWeather);
        TextView tvWeatherInfo = view.findViewById(R.id.tvWeatherInfo);

        tvCityName.setText(menuName + " " + menuSubdata + "の天気は");

        // cityIdの取得
        String cityId = "";
        if (menuName.contains("東京")) {
            cityId = "130010";
        }
        else if (menuName.contains("大阪")) {
            cityId = "270000";
        }

        WeatherInfoReceiver receiver = new WeatherInfoReceiver(tvWeather, tvWeatherInfo);
        receiver.execute(cityId);

        // Inflate the layout for this fragment
        return view;
    }

    private class WeatherInfoReceiver extends AsyncTask<String, String, String> {

        private TextView _tvWeather;
        private TextView _tvWeatherInfo;

        public WeatherInfoReceiver(TextView tvWeather, TextView tvWeatherInfo) {
            _tvWeather = tvWeather;
            _tvWeatherInfo = tvWeatherInfo;
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String urlStr = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + id;
            String result = "";

            // API呼び出し
            HttpURLConnection con = null;
            InputStream is = null;

            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();

                result = is2String(is);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String weather = "";
            String info = "";

            // json解析
            try {
                JSONObject rootJSON = new JSONObject(result);
                JSONObject descJSON = rootJSON.getJSONObject("description");
                info = descJSON.getString("text");

                JSONArray forecasts = rootJSON.getJSONArray("forecasts");
                JSONObject forecastNow = forecasts.getJSONObject(0);
                weather = forecastNow.getString("telop");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            _tvWeather.setText(weather);
            _tvWeatherInfo.setText(info);
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();

            char[] b = new char[1024];
            int line;

            while(0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }

            return sb.toString();
        }
    }
}