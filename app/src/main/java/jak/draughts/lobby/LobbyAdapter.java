package jak.draughts.lobby;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private LobbyActivity activity;
    private List<Room> rooms;
    private Map<String, User> users;
    private Context context;

    public LobbyAdapter(LobbyActivity activity, List<Room> rooms, Map<String, User> users) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.rooms = rooms;
        this.users = users;
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
        Room room = rooms.get(position);
        chooseText(holder, room);

        chooseColor(holder, room);
    }

    private void chooseText(final @NonNull LobbyViewHolder holder, final Room room) {
        User host = users.get(room.getUserHostId());
        User join = users.get(room.getUserJoinId());
        holder.textView.setText(writeText(room, host, join));
    }

    private String writeText(Room room, User host, User join) {
        StringBuilder sb = new StringBuilder();
        sb.append("Room Id: ").append(room.getRoomId()).append("\n");
        if (host != null) {
            sb.append("Host: ").append(host.getName()).append("\n");
        }
        if (join != null) {
            sb.append("Join: ").append(join.getName()).append("\n");
        }
        sb.append("Game Mode: ").append(room.getGameMode()).append("\n");
        sb.append("Status: ").append(parseStatus(room.getStatus())).append("\n");
        return sb.toString();
    }

    private String parseStatus(int status) {
        switch(status) {
            case Room.VACANT:
                return "VACANT";
            case Room.FULL:
                return "FULL";
            case Room.READY:
                return "READY";
            case Room.PLAYING:
                return "PLAYING";
            case Room.RESULT:
                return "RESULT";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the colour of the textView according to the room's status.
     *
     * @param holder
     * @param room
     */
    private void chooseColor(@NonNull LobbyViewHolder holder, Room room) {
        holder.textView.setBackground(chooseColor(room));
    }

    /**
     * Returns the colour of the room tile, depending on the status
     * of the given room.
     *
     * @param room given room
     * @return ColorDrawable
     */
    private ColorDrawable chooseColor(Room room) {
        if (room == activity.getSelectedRoom()) {
            return new ColorDrawable(context.getColor(R.color.colorBoardSelected));
        }

        switch(room.getStatus()) {
            case Room.VACANT:
                return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
            case Room.FULL:
            case Room.READY:
            case Room.PLAYING:
            case Room.RESULT:
                return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
            default:
                throw new IllegalStateException();
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
