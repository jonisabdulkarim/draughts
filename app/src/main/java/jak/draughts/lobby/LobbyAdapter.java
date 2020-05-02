package jak.draughts.lobby;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jak.draughts.R;
import jak.draughts.Room;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private LobbyActivity activity;
    private List<Room> rooms;
    private Context context;
    private Room selectedRoom;

    public LobbyAdapter(LobbyActivity activity, List<Room> rooms) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.rooms = rooms;
        this.selectedRoom = activity.selectedRoom;
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
            return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        } else {
            return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
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

        LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.item_lobby_room);
            this.textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(textView, getAdapterPosition());
        }
    }

    private void onItemClick(View view, int position) {
        // TODO: update Room class
        if (isSelected(view)) {
            selectedRoom = null;
            view.setBackgroundColor(context.getColor(R.color.colorBoardBuff));
        } else if (selectedRoom != null) {

        } else {

        }
    }

    private boolean isSelected(View view) {
        Drawable color = view.getBackground();
        return color == context.getDrawable(R.color.colorBoardSelected);
    }
}
