package inesctec.gresbas.RecyclerAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.data.model.model_files.Impact;


public class ImpactRecyclerAdapter extends RecyclerView.Adapter<ImpactRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Impact> impactList;

    public ImpactRecyclerAdapter(Context context, List impact) {
        this.context = context;
        this.impactList = impact;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(impactList.get(position));

        Impact pu = impactList.get(position);

        holder.pName.setText(pu.getDate());
        holder.pPoints.setText(pu.getValue());

    }

    @Override
    public int getItemCount() {
        return impactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPoints;
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway/Raleway-Medium.ttf");

        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pName.setTypeface(typeface);
            pPoints = (TextView) itemView.findViewById(R.id.pPointstxt);
            pPoints.setTypeface(typeface);

        }
    }

}