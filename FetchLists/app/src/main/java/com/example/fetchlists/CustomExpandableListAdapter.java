package com.example.fetchlists;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<Integer> listIDs; // listIds
    private Map<Integer, List<Item>> listItems; // Items for each listId

    public CustomExpandableListAdapter(Context context, List<Integer> listIDs, Map<Integer, List<Item>> listItems) {
        this.context = context;
        this.listIDs = listIDs;
        this.listItems = listItems;
    }
    @Override
    public int getGroupCount() {
        return listIDs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItems.get(this.listIDs.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listIDs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItems.get(this.listIDs.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Integer listId = (Integer) getGroup(listPosition);
        String listTitle = String.valueOf(listId);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_by_lists, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.list_id);
        listTitleTextView.setText("  List " + listTitle);
        if (isExpanded) {
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
        } else {
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
        }
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            Object child = getChild(listPosition, expandedListPosition);

            Item item = (Item) child;
            String expandedListText = item.getName();

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_items, null);
            }

            TextView expandedListTextView = (TextView) convertView.findViewById(R.id.item_name);
            expandedListTextView.setText(expandedListText);
            return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }
}
