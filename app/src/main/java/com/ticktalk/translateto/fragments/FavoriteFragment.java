package com.ticktalk.translateto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ticktalk.translateto.database.DatabaseManager;
import com.ticktalk.translateto.database.FromResult;
import com.ticktalk.translateto.database.ToResult;
import com.ticktalk.translateto.adapters.FavoriteAdapter;
import com.ticktalk.translateto.history.ItemHistory;
import com.ticktalk.translateto.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael S. Martin
 */

public class FavoriteFragment extends Fragment {

    public static final String TAG ="FAVORITE_FRAGMENT";

    private List items;
    private View view;

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppCompatActivity activity;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        List<FromResult> fromResults = DatabaseManager.getInstance().getFavoriteResult();
        items = new ArrayList();
        FromResult fromResult = new FromResult();
        ToResult toResult = new ToResult();


        for(int i = 0; i < fromResults.size(); ++i){
                items.add(new ItemHistory(
                        fromResult.getLanguageCode(), fromResult.getText(),
                        toResult.getLanguageCode(), toResult.getText(),
                        R.drawable.favorite,
                        R.drawable.flag_circle_auto, R.drawable.flag_circle_auto,
                        fromResult.getFavoriteAnimation()));
        }




        // Obtener el Recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);

        adapter = new FavoriteAdapter(activity, items);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(view -> {
            int pos= recyclerView.getChildAdapterPosition(view);
            Toast.makeText(activity, "Selección: " + pos , Toast.LENGTH_LONG).show();

        });

        return view;

    }
}
