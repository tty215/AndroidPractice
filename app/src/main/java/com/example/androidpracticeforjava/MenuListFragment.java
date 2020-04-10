package com.example.androidpracticeforjava;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuListFragment extends Fragment {

    private Activity _parentActivity;
    private boolean _isLayoutLand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parentActivity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View menuThanksActivity = _parentActivity.findViewById(R.id.fragmentResult);
        _isLayoutLand = menuThanksActivity != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        ListView lvMenu = view.findViewById(R.id.lvMenu);
        List<Map<String, String>> menuList = new ArrayList<Map<String, String>>(Arrays.asList(
                new HashMap<String, String>(){
                    {
                        put("name", "唐揚げ定食");
                        put("subdata", "800円");
                    }
                },
                new HashMap<String, String>(){
                    {
                        put("name", "ハンバーグ定食");
                        put("subdata", "700円");
                    }
                },
                new HashMap<String, String>(){
                    {
                        put("name", "東京都");
                        put("subdata", "新宿区");
                    }
                },
                new HashMap<String, String>(){
                    {
                        put("name", "大阪府");
                        put("subdata", "東大阪市");
                    }
                },
                new HashMap<String, String>(){
                    {
                        put("name", "メディア再生");
                        put("subdata", "");
                    }
                }
        ));

        String[] from = {"name", "subdata"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleAdapter adapter = new SimpleAdapter(
                _parentActivity,
                menuList,
                android.R.layout.simple_expandable_list_item_2,
                from,
                to);

        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new ListItemClickListener());

        return view;
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Map<String, String> item = (Map<String, String>) adapterView.getItemAtPosition(i);
            String menuName = item.get("name");
            String menuSubdata = item.get("subdata");

            Bundle bundle = new Bundle();
            bundle.putString("menuName", menuName);
            bundle.putString("menuSubdata", menuSubdata);

            if (menuName.contains("定食")) {
                if (_isLayoutLand) {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    ThanksFragment thanksFragment = new ThanksFragment();
                    thanksFragment.setArguments(bundle);

                    transaction.replace(R.id.fragmentResult, thanksFragment);
                    transaction.commit();
                }
                else {
                    Intent intent = new Intent(_parentActivity, ThanksActivity.class);

                    // 次の画面にデータを送る
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            else if(menuName.contains("都") || menuName.contains("府")) {
                if (_isLayoutLand) {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    WeatherFragment weatherFragment = new WeatherFragment();
                    weatherFragment.setArguments(bundle);

                    transaction.replace(R.id.fragmentResult, weatherFragment);
                    transaction.commit();
                }
                else {
                    Intent intent = new Intent(_parentActivity, WeatherActivity.class);

                    // 次の画面にデータを送る
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            else if(menuName.contains("メディア再生")) {
                if (_isLayoutLand) {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    MediaFragment mediaFragment = new MediaFragment();

                    transaction.replace(R.id.fragmentResult, mediaFragment);
                    transaction.commit();
                }
                else {
                    Intent intent = new Intent(_parentActivity, MediaActivity.class);

                    startActivity(intent);
                }
            }
        }
    }
}
