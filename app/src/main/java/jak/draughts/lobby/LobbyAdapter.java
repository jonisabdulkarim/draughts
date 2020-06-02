package jak.draughts.lobby;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import jak.draughts.R;
import jak.draughts.Room;
import jak.draughts.User;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    private LobbyActivity activity;
    private List<Room> rooms;
    private Context context;

    private static final int ROOM_VACANT = 0;
    private static final int ROOM_FULL = 1;
    private static final int ROOM_READY = 2;
    private static final int ROOM_PLAYING = 3;

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
        Room room = rooms.get(position);
        chooseText(holder, room);

        chooseColor(holder, room);
    }

    private void chooseText(final @NonNull LobbyViewHolder holder, final Room room) {
        // FIXME: get rid of DB code in adapter
        FirebaseFirestore.getInstance().collection("users")
                .document(room.getUserHostId()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        String hostName = user.getName();
                        holder.textView.setText(writeText(room, hostName));
                    }
                });
    }

    private String writeText(Room room, String hostName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Room Id: ").append(room.getRoomId());
        sb.append("\n");
        sb.append("Host: ").append(hostName);
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
            case ROOM_READY:
                return "READY";
            case ROOM_PLAYING:
                return "PLAYING";
            default:
                throw new IllegalArgumentException();
        }
    }

    private void chooseColor(@NonNull LobbyViewHolder holder, Room room) {
        holder.textView.setBackground(chooseColor(room));
    }

    private ColorDrawable chooseColor(Room room) {
        if (room == activity.getSelectedRoom()) {
            return new ColorDrawable(context.getColor(R.color.colorBoardSelected));
        }

        switch(room.getStatus()) {
            case ROOM_VACANT:
                return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
            case ROOM_FULL:
                return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
            case ROOM_READY:
                return new ColorDrawable(context.getColor(R.color.colorAccent));
            case ROOM_PLAYING:
                return new ColorDrawable(context.getColor(R.color.colorBoardCaptureSelect));
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
