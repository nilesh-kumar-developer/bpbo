package us.nilesh.bpbo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.nilesh.bpbo.R;
import us.nilesh.bpbo.interfaces.EnquiryInterface;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<String> allName, allOrgan;
    private final LayoutInflater mInflater;
    private final EnquiryInterface enquiryInterface;
    private static final String TAG="eventadapter";

    public HomeAdapter(Context context, List<String> Name, List<String> Organ, EnquiryInterface enquiryInterface){
        this.mInflater = LayoutInflater.from(context);
        this.enquiryInterface = enquiryInterface;
        this.allName = Name;
        this.allOrgan = Organ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.donor.setText(allName.get(position));
        holder.organ.setText(allOrgan.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onBindViewHolder: "+ allOrgan.get(position).toString());
                enquiryInterface.onClickEnquiry(String.valueOf(allOrgan.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return allName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView donor, organ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            donor = itemView.findViewById(R.id.donorNameTxt);
            organ =itemView.findViewById(R.id.organNameTxt);
        }
    }
}
