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
import inesctec.gresbas.data.model.model_files.Messages;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Messages> messagesList;

    public MessagesRecyclerAdapter(Context context, List menu) {
        this.context = context;
        this.messagesList = menu;
    }

    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);
        MessagesRecyclerAdapter.ViewHolder viewHolder = new MessagesRecyclerAdapter.ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(messagesList.get(position));

        Messages pu = messagesList.get(position);

        holder.pName.setText(pu.getTitleMessage());
        holder.pPoints.setText(pu.getDateMessage());
        String type = pu.getMessageType();


        if (type.equals("Alert")) {
            holder.image.setImageResource(R.drawable.ic_ep_score_outline_48dp);
        }

        if (type.equals("Exclamation")) {
            holder.image.setImageResource(R.drawable.ic_audiotrack_dark);
        }

        if (type.equals("Information")) {
            holder.image.setImageResource(R.drawable.user_icon);
        }

    }


    @Override
    public int getItemCount() {
        return messagesList.size();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Messages cpu = (Messages) view.getTag();

                    Toast.makeText(view.getContext(), "Message: " + cpu.getDescriptionMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }
    }
}
