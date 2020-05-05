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

import jak.draughts.R;
import jak.draughts.Room;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private LobbyActivity activity;
    private List<Room> rooms;
    private Context context;

    private static final int ROOM_VACANT = 0;
    private static final int ROOM_FULL = 1;
    private static final int ROOM_PLAYING = 2;

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
        sb.append("Host: ").append(room.getUserHostId());
        sb.append("\n");
        sb.append("Game Mode: ").append(room.getGameMode());
        sb.append("\n");
        sb.append("Status: ").append(parseStatus(room.getStatus()));
        return sb.toString();
    }

    private String parseStatus(int status) {
        switch(status) {
            case ROOM_VACANT:
                return "VACANT";
            case ROOM_FULL:
                return "FULL";
            case ROOM_PLAYING:
                return "PLAYING";
            default:
                throw new IllegalArgumentException();
        }
    }

    private void chooseColor(@NonNull LobbyViewHolder holder, int position) {
        holder.textView.setBackground(chooseColor(position));
    }

    private ColorDrawable chooseColor(int position) {
        Room room = rooms.get(position);

        if (room == activity.getSelectedRoom()) {
            return new ColorDrawable(context.getColor(R.color.colorBoardSelected));
        } else if (room.getStatus() == ROOM_VACANT) {
            return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        } else if (room.getStatus() == ROOM_FULL) {
            return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
        } else if (room.getStatus() == ROOM_PLAYING){
            return new ColorDrawable(context.getColor(R.color.colorBoardCaptureSelect));
        } else {
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
