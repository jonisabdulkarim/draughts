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
        Room room = rooms.get(position);

        if (room == activity.getSelectedRoom()) {
            return new ColorDrawable(context.getColor(R.color.colorBoardSelected));
        } else if (room.getUserJoin() == null) {
            return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        } else {
            return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    /**
     * Informs the adapter that either the dataSet
     * has changed or that a room has been selected.
     * The recyclerView will update accordingly.
     */
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

    /**
     * Called by the ViewHolder object that was clicked.
     * Selects the corresponding room and updates the
     * view accordingly. If the given room is already
     * selected, it will be deselected.
     *
     * @param view that was clicked
     * @param position index of the corresponding room
     */
    private void onItemClick(View view, int position) {
        // TODO: update Room class
        Room room = rooms.get(position);

        if (room == activity.getSelectedRoom()) {
            // deselect if already selected
            activity.setSelectedRoom(null);
        } else {
            // select room if others/none are selected
            activity.setSelectedRoom(room);
        }

        update();
    }
}
