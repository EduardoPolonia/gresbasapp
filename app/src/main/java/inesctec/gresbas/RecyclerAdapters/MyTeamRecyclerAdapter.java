package inesctec.gresbas.RecyclerAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.data.model.model_files.MyTeam;


public class MyTeamRecyclerAdapter extends RecyclerView.Adapter<MyTeamRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<MyTeam> myTeamList;

    public MyTeamRecyclerAdapter(Context context, List menu) {
        this.context = context;
        this.myTeamList = menu;
    }

    @Override
    public MyTeamRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myteam_list, parent, false);
        MyTeamRecyclerAdapter.ViewHolder viewHolder = new MyTeamRecyclerAdapter.ViewHolder(v);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(MyTeamRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(myTeamList.get(position));

        MyTeam pu = myTeamList.get(position);

        holder.pName.setText(pu.getUserTeam());
        holder.pPoints.setText(pu.getPointsTeam());

      /*  byte[] bt = pu.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bt,0,bt.length);
        holder.image.setImageBitmap(bitmap);*/


    }


    @Override
    public int getItemCount() {

        return myTeamList.size();
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
            pPoints.setTypeface(typeface);
            image = (ImageView) itemView.findViewById(R.id.userImg);

        }
    }
}



