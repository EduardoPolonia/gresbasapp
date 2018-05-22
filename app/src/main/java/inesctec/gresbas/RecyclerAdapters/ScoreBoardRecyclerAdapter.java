package inesctec.gresbas.RecyclerAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import inesctec.gresbas.R;
import inesctec.gresbas.data.model.model_files.ScoreBoard;

import static android.content.Context.MODE_PRIVATE;


public class ScoreBoardRecyclerAdapter extends RecyclerView.Adapter<ScoreBoardRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<ScoreBoard> scoreBoards;
    private ScoreBoardRecyclerAdapter thisAdapter = this;
    Dialog myDialog;

    String username;
    int couter = 1;
    int i;
    int size;
    int above;
    int below;
    int g =1;

    public ScoreBoardRecyclerAdapter(Context context, List scoreBoards) {
        this.context = context;
        this.scoreBoards = scoreBoards;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        int b;
        if (g==1) {
            for (b = 0; b < size; b++) {
                ScoreBoard pu = scoreBoards.get(b);
                String user = pu.getUsername();
                if (user.equals(username)) {
                    i = b+1;
                }
            }
            above = i - 2;
            below = i + 2;
        }
        g=0;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(scoreBoards.get(position));
        ScoreBoard pu = scoreBoards.get(position);
        holder.pName.setText(pu.getUsername());
        holder.pPoints.setText(pu.getTotalPoints() + "pts.");
        holder.place.setText((couter)+ "º");
        String user = pu.getUsername();
        if (user.equals(username))
        {
            holder.item_user.setBackgroundColor(Color.parseColor("#d4f7e9"));
        }


       if (couter < above || couter > below)
        {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        couter = couter +1;
    }

    @Override
    public int getItemCount() {
        size = scoreBoards.size();
        return scoreBoards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPoints;
        Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway/Raleway-Medium.ttf");
        private LinearLayout item_user;
        public TextView place;

        public ViewHolder(View itemView) {
            super(itemView);

            item_user = (LinearLayout) itemView.findViewById(R.id.user_item);


            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pName.setTypeface(typeface);
            pPoints = (TextView) itemView.findViewById(R.id.pPointstxt);
            pPoints.setTypeface(typeface);
            place = (TextView) itemView.findViewById(R.id.place);
            place.setTypeface(typeface);

            SharedPreferences pref = itemView.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
             username = pref.getString("username", null);



        }
    }

}