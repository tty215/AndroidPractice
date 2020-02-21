package com.example.androidpracticeforjava;


import android.app.Activity;
import android.content.Intent;
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
        String menuPrice = bundle.getString("menuPrice");

        TextView tvMenuName = view.findViewById(R.id.tvMenuName);
        TextView tvMenuPrice = view.findViewById(R.id.tvMenuPrice);

        tvMenuName.setText(menuName);
        tvMenuPrice.setText(menuPrice);

        return view;
    }

}
