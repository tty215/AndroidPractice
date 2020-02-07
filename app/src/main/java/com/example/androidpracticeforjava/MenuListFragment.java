package com.example.androidpracticeforjava;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parentActivity = getActivity();
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
                        put("price", "800円");
                    }
                },
                new HashMap<String, String>(){
                    {
                        put("name", "ハンバーグ定食");
                        put("price", "700円");
                    }
                }
        ));

        String[] from = {"name", "price"};
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
            String menuPrice = item.get("price");

            Intent intent = new Intent(_parentActivity, ThanksActivity.class);

            // 次の画面にデータを送る
            intent.putExtra("menuName", menuName);
            intent.putExtra("menuPrice", menuPrice);

            startActivity(intent);
        }
    }
}
