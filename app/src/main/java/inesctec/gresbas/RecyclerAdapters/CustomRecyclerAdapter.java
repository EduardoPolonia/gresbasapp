package inesctec.gresbas.RecyclerAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.data.model.model_files.EarnedPoints;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<EarnedPoints> earnedPoints;

    public CustomRecyclerAdapter(Context context, List earnedPoints) {
        this.context = context;
        this.earnedPoints = earnedPoints;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(earnedPoints.get(position));

        EarnedPoints pu = earnedPoints.get(position);
        holder.pName.setText(pu.getName());
        holder.pPoints.setText(pu.getTotalPoints() + "pts.");
        String type = pu.getName();
        if (type.equals("Computer")) {
            holder.imgtype.setImageResource(R.drawable.icon_computer_light_points);
        }

        if (type.equals("Ligths")) {
            holder.imgtype.setImageResource(R.drawable.icon_light_points);
        }

        if (type.equals("Monitor")) {
            holder.imgtype.setImageResource(R.drawable.icon_monitor_points);
        }


    }

    @Override
    public int getItemCount() {
        return earnedPoints.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPoints;
        public ImageView imgtype;
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway/Raleway-Medium.ttf");

        public ViewHolder(final View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pName.setTypeface(typeface);
            pPoints = (TextView) itemView.findViewById(R.id.pPointstxt);
            pPoints.setTypeface(typeface);
            imgtype = (ImageView) itemView.findViewById(R.id.imgtype);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EarnedPoints cpu = (EarnedPoints) view.getTag();

                    Toast.makeText(view.getContext(), cpu.getName() + " has " + cpu.getTotalPoints() + "pts", Toast.LENGTH_SHORT).show();


                }
            });

        }
    }

}