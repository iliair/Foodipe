package com.khawrizmi.iliaalizadeh.foodipe;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ings_frag extends Fragment {


    public ings_frag() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    loopjAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Fetchloopj> array;

    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tool,menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            if (item.getItemId() == R.id.add_item) {
                Intent i = new Intent(getActivity(), adding.class);
                startActivity(i);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view=inflater.inflate(R.layout.fragment_ings_frag,container,false);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ings_swipe);
        recyclerView = (RecyclerView) view.findViewById(R.id.ings_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new loopjAdapter(getActivity().getBaseContext(), array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        loadData(0);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadData(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<>();
                loadData(0);
                scrollListener.resetState();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = new Intent(getActivity(),data_activity.class);
                i.putExtra("title",array.get(position).title.toString());
                i.putExtra("image",array.get(position).img.toString());
                i.putExtra("data",array.get(position).data.toString());
                startActivity(i);
            }
        }));





        // Inflate the layout for this fragment
        return view;

}
    public void loadData(int page) {
        NetUtils.get("?data=phone&limit=10&page=" + (page+1), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray phones = response;

                try {
                    for (int i = 0; i < phones.length(); i++) {
                        array.add(new Fetchloopj(phones.getJSONObject(i)));
                    }
                    adapter.update(array);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

        });
    }

}

