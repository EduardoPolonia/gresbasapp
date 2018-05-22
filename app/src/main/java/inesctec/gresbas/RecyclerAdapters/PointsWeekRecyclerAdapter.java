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
import inesctec.gresbas.data.model.model_files.PointsWeek;

public class PointsWeekRecyclerAdapter extends RecyclerView.Adapter<PointsWeekRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<PointsWeek> pointsWeek;

    int numero = 0;

    public PointsWeekRecyclerAdapter(Context context, List pointsWeek) {
        this.context = context;
        this.pointsWeek = pointsWeek;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        if (numero == 0) {
            viewHolder.titulo.setText("My Points (No Data)");
        } else {
            viewHolder.titulo.setText("My Points");
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(pointsWeek.get(position));

        PointsWeek pu = pointsWeek.get(position);
        holder.pName.setText(pu.getNameWeek());
        holder.pPoints.setText(pu.getTotalPointsWeek() + "pts.");
        String type = pu.getNameWeek();
        if (type.equals("Computer")) {
            holder.imgtype.setImageResource(R.drawable.icon_computer_light_points);
        }

        if (type.equals("Ligths")) {
            holder.imgtype.setImageResource(R.drawable.icon_light_points);
        }

        if (type.equals("Monitor")) {
            holder.imgtype.setImageResource(R.drawable.icon_monitor_points);
        }

        if (numero == 0) {
            holder.titulo.setText("My Points (No Data)");
        } else {
            holder.titulo.setText("My Points");
        }


    }

    @Override
    public int getItemCount() {
        numero = pointsWeek.size();

        return pointsWeek.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPoints;
        public ImageView imgtype;
        public TextView titulo;
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway/Raleway-Medium.ttf");

        public ViewHolder(final View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pName.setTypeface(typeface);
            pPoints = (TextView) itemView.findViewById(R.id.pPointstxt);
            pPoints.setTypeface(typeface);
            imgtype = (ImageView) itemView.findViewById(R.id.imgtype);
            titulo = (TextView) itemView.findViewById(R.id.titlepop);

            if (numero == 0) {
                titulo.setText("My Points (No Data)");
            } else {
                titulo.setText("My Points");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PointsWeek cpu = (PointsWeek) view.getTag();

                    Toast.makeText(view.getContext(), cpu.getNameWeek() + " has " + cpu.getTotalPointsWeek() + "pts", Toast.LENGTH_SHORT).show();


                }
            });


        }
    }

}
