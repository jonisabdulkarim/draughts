package jak.draughts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jak.draughts.activities.LobbyActivity;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private LobbyActivity activity;
    private List<Room> rooms;
    private Context context;

    public LobbyAdapter(LobbyActivity activity, List<Room> rooms) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_lobby_room, parent, false);

        return new LobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        holder.textView.setText(chooseText(position));

        chooseColor(holder, position);
    }

    private String chooseText(int position) {
        StringBuilder sb = new StringBuilder();
        Room room = rooms.get(position);
        sb.append("Host: ").append(room.getUserHost().getName());
        sb.append("\n");
        sb.append("Status: ");
        if (room.getUserJoin() != null) {
            sb.append("full");
        } else {
            sb.append("vacant");
        }
        return sb.toString();
    }

    private void chooseColor(@NonNull LobbyViewHolder holder, int position) {
        holder.textView.setBackground(chooseColor(position));
    }

    private ColorDrawable chooseColor(int position) {
        if (rooms.get(position).getUserJoin() == null) {
            return new ColorDrawable(context.getColor(R.color.colorPrimary));
        } else {
            return new ColorDrawable(context.getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void update() {
        notifyDataSetChanged();
    }

    class LobbyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.item_lobby_room);
            this.textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
