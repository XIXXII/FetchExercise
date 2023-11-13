package com.example.fetchlists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ExpandableListView expandableListView;
    ExpandableListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        retrieveAndDisplay();

    }

    private void retrieveAndDisplay(){
        String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Item> itemList;
                    try {
                        itemList = jsonParse(response);
                        sortByIdAndName(itemList);
                        expandableListView = findViewById(R.id.expandableListView);
                        establishData(itemList);
                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                                return false;
                            }

                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private List<Item> jsonParse(JSONArray response) throws JSONException {
        List<Item> itemList = new ArrayList<>();
        Gson gson = new Gson();
        int total_item = response.length();
        for(int i=0; i<total_item; i++){
            JSONObject item =  response.getJSONObject(i);
            if (!item.isNull("name") && !item.getString("name").isEmpty()) {
                Item valid_item = gson.fromJson(item.toString(), Item.class);
                itemList.add(valid_item);
            }
        }
        return itemList;
    }

    private void sortByIdAndName(List<Item> itemList){
        Collections.sort(itemList, new Comparator<Item>() {
            //compare the item by listID first and then by name
            @Override
            public int compare(Item t1, Item t2) {
                int result = Integer.compare(t1.getListId(), t2.getListId());
                if(result == 0){
                    int item1_int = Integer.parseInt(t1.getName().split("\\s+")[1]);
                    int item2_int = Integer.parseInt(t2.getName().split("\\s+")[1]);
                    result = Integer.compare(item1_int, item2_int);
                }
                return result;
            }

        });
    }

    private void establishData(List<Item> itemList){
        HashMap<Integer, List<Item>> expandableListDetail = new HashMap<>();

        for (Item item : itemList) {
            int listId = item.getListId();
            if(!expandableListDetail.containsKey(listId)){
                expandableListDetail.put(listId, new ArrayList<>());
            }
            expandableListDetail.get(listId).add(item);
        }
        adapter = new CustomExpandableListAdapter(this, new ArrayList<>(expandableListDetail.keySet()), expandableListDetail);
        expandableListView.setAdapter(adapter);
    }
}