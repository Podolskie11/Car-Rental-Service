package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvModel;
        public TextView tvAStatus;
        public TextView tvRemarks;


        public ViewHolder(View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvAStatus = itemView.findViewById(R.id.tvAStatus);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);

            itemView.setOnLongClickListener(this); //register long click action to this viewholder instance
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition(); //key point, record the position here
            return false;
        }
    } // close ViewHolder class

    //////////////////////////////////////////////////////////////////////
    // adapter class definitions

    private List<Car> carListData;   // list of book objects
    private Context mContext;       // activity context
    private int currentPos;         // currently selected item (long press)

    public CarAdapter(Context context, List<Car> listData) {
        carListData = listData;
        mContext = context;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout using the single item layout
        View view = inflater.inflate(R.layout.car_list_item, parent, false);
        // Return a new holder instance
        CarAdapter.ViewHolder viewHolder = new CarAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CarAdapter.ViewHolder holder, int position) {
        // bind data to the view holder instance
        Car m = carListData.get(position);
        holder.tvModel.setText(m.getModel());
        holder.tvAStatus.setText(m.getAvailabilityStatus());
        holder.tvRemarks.setText(m.getRemarks());
    }

    @Override
    public int getItemCount() {
        return carListData.size();
    }


    /**
     * return book object for currently selected book (index already set by long press in viewholder)
     * @return
     */
    public Car getSelectedItem() {
        // return the book record if the current selected position/index is valid
        if(currentPos>=0 && carListData !=null && currentPos<carListData.size()) {
            return carListData.get(currentPos);
        }
        return null;
    }
}