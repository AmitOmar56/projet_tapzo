package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Soft1 on 07-Sep-17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeToRefresh;
    private ItemAdapter itemAdapter;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.booking_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        
      /*  ViewGroup item = getViewGroupChild(convertView, parent);
        GridView label = (GridView) item.findViewById(ipvc.estg.placebook.R.id.gridview);
        label.setAdapter(new GridAdapter(parent.getContext(), groupPosition + 1));

        // initialize the following variables (i've done it based on your layout
        // note: rowHeightDp is based on my grid_cell.xml, that is the height i've
        //    assigned to the items in the grid.
        final int spacingDp = 10;
        final int colWidthDp = 50;
        final int rowHeightDp = 20;

        // convert the dp values to pixels
        final float COL_WIDTH = _context.getResources().getDisplayMetrics().density * colWidthDp;
        final float ROW_HEIGHT = _context.getResources().getDisplayMetrics().density * rowHeightDp;
        final float SPACING = _context.getResources().getDisplayMetrics().density * spacingDp;

        // calculate the column and row counts based on your display
        final int colCount = (int) Math.floor((parent.getWidth() - (2 * SPACING)) / (COL_WIDTH + SPACING));
        final int rowCount = (int) Math.ceil((intValues.size() + 0d) / colCount);

        // calculate the height for the current grid
        final int GRID_HEIGHT = Math.round(rowCount * (ROW_HEIGHT + SPACING));

        // set the height of the current grid
        label.getLayoutParams().height = GRID_HEIGHT;

        return item;
*/

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.booking_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        return convertView;

//        final String childText = (String) getChild(groupPosition, childPosition);
//
//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.booking_list_item1, null);
//        }
//
//        recyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view_viral_news);
//
//        setPagerAdapter(childText);
//
//
//
//       /* TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.lblListItem);
//
//        txtListChild.setText(childText);*/
//
//
//        return convertView;


    }

    public void setPagerAdapter(String childText) {
        GridLayoutManager llm = new GridLayoutManager(_context, 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        itemAdapter = new ItemAdapter(_context,childText);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


    public class ItemAdapter extends RecyclerView.Adapter {

        private  String childText;
        private Context context;


        public ItemAdapter(Context context, String childText) {
            this.context = context;
            this.childText=childText;


        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_grid_view, parent, false);
            ContactViewHolder contactViewHolder = new ContactViewHolder(itemView);
            return contactViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ContactViewHolder) {
                final ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
                contactViewHolder.setIsRecyclable(false);


//            contactViewHolder.ivIcon.setImageResource(modelArrayList.get(position).getImageId());
//                contactViewHolder.tvTitle.setText(modelArrayList.get(position).getDiscription());


            }

        }


        @Override
        public int getItemCount() {
            return 20;
        }


        public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView textView;

            public ContactViewHolder(View itemView) {
                super(itemView);
                getId(itemView);
                handleListener();


            }

            private void getId(View itemView) {
                textView = (TextView) itemView.findViewById(R.id.txt);

            }

            private void handleListener() {


            }


            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.txt:
                        break;
                }


            }
        }


    }

}
