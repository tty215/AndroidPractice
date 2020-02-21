package com.example.androidpracticeforjava;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_thanks, container, false);

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

        TextView tvMenuName = view.findViewById(R.id.tvMenuName);
        TextView tvMenuSubdata = view.findViewById(R.id.tvMenuSubdata);
        TextView purchaseCount = view.findViewById(R.id.purchaseCount);

        tvMenuName.setText(menuName);
        tvMenuSubdata.setText(menuSubdata);

        int count = this.getIncrementCount(menuName);
        purchaseCount.setText(String.valueOf(count));

        return view;
    }

    private int getIncrementCount(String menuName) {
        int count = 0;

        DatabaseHelper helper = new DatabaseHelper(_parentActivity);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // データ取得処理
            String sqlSelect = "SELECT * FROM cocktailmemo WHERE name = '" + menuName + "'";
            Cursor cursor = db.rawQuery(sqlSelect, null);

            while (cursor.moveToNext()) {
                int idx = cursor.getColumnIndex("count");
                count = cursor.getInt(idx);
            }

            count++;

            // なければ初期値として入れる
            if (count == 1) {
                String sqlInsert = "INSERT INTO cocktailmemo (name, count) VALUES (?, 1)";
                SQLiteStatement stmt = db.compileStatement(sqlInsert);
                stmt.bindString(1, menuName);

                stmt.executeInsert();
            }
            else {
                // データ格納処理
                String sqlUpdate = "UPDATE cocktailmemo SET count = " + count + " WHERE name = '" + menuName + "'";
                SQLiteStatement stmt = db.compileStatement(sqlUpdate);

                stmt.executeUpdateDelete();
            }
        }
        finally {
            db.close();
        }

        return count;
    }
}
