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
import inesctec.gresbas.data.model.model_files.Restaurant;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantRecyclerAdapter(Context context, List menu) {
        this.context = context;
        this.restaurantList = menu;
    }

    @Override
    public RestaurantRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_restaurant_list, parent, false);
        RestaurantRecyclerAdapter.ViewHolder viewHolder = new RestaurantRecyclerAdapter.ViewHolder(v);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RestaurantRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(restaurantList.get(position));

        Restaurant pu = restaurantList.get(position);

        holder.pName.setText(pu.getMindescription());

        String type = pu.getMaxdescription();


        if (type.equals("Meat")) {
            holder.image.setImageResource(R.drawable.meat);
        }

        if (type.equals("Fish")) {
            holder.image.setImageResource(R.drawable.fish);
        }

        if (type.equals("Vegetarian")) {
            holder.image.setImageResource(R.drawable.veg);
        }


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPoints;
        public ImageView image;
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway/Raleway-Medium.ttf");

        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pName.setTypeface(typeface);
            pPoints = (TextView) itemView.findViewById(R.id.pPointstxt);
            image = (ImageView) itemView.findViewById(R.id.userImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Restaurant cpu = (Restaurant) view.getTag();

                    Toast.makeText(view.getContext(), "Menu type: " + cpu.getMaxdescription(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
