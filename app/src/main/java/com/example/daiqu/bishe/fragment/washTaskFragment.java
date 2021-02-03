package com.example.daiqu.bishe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daiqu.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link washTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class washTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> list;
    private Spinner spinner;
    private Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public washTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment washTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static washTaskFragment newInstance(String param1, String param2) {
        washTaskFragment fragment = new washTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wash_task, container, false);
        initWidget(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] items = getResources().getStringArray(R.array.spinner_data);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this.getActivity().getApplicationContext(),R.layout.spinner_item_layout,items);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //spinner.setAdapter(spinnerAdapter);
        //spinner.setSelection(0);
    }

    void initWidget(View view) {
        spinner = view.findViewById(R.id.time_spinner_checkText);
    }

}