package jak.draughts.game;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jak.draughts.Coordinates;
import jak.draughts.R;
import jak.draughts.TileColor;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private Context context;
    private Game game;
    private List<Integer> dataSet;
    private List<TileColor> backgroundSet;

    public GameAdapter(Context context, Game game) {
        this.context = context;
        this.game = game;
        this.game.setAdapter(this);

        this.dataSet = new ArrayList<>();
        this.backgroundSet = new ArrayList<>();

        this.dataSet.addAll(game.getDataSet());
        this.backgroundSet.addAll(game.getBackgroundSet());
    }

    @NonNull
    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_board_tile, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        chooseColor(holder, position);

        chooseDisc(holder, position);
    }

    private void chooseDisc(@NonNull MyViewHolder holder, int position) {
        switch (dataSet.get(position)) {
            case 0:
                holder.textView.setForeground(null);
                break;
            case 1:
                holder.textView.setForeground(context.getDrawable(R.drawable.red_man_disc));
                break;
            case 3:
                holder.textView.setForeground(context.getDrawable(R.drawable.white_man_disc));
                break;
        }
    }

    private void chooseColor(@NonNull MyViewHolder holder, int position) {
        holder.textView.setBackground(chooseColor(position));
    }

    private ColorDrawable chooseColor(int position) {
        switch (game.getBackgroundSet().get(position)) {
            case GREEN:
                return new ColorDrawable(context.getColor(R.color.colorBoardGreen));
            case WHITE:
                return new ColorDrawable(context.getColor(R.color.colorBoardBuff));
            case SELECTED:
                return new ColorDrawable(context.getColor(R.color.colorBoardSelected));
            default:
                throw new IllegalStateException("Illegal tile background");
        }
    }

    private boolean isGreen(int position) {
        return position / 8 % 2 == 0 ^ position % 2 == 0;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(textView, getAdapterPosition());
        }
    }

    private void onItemClick(View view, int position) {
        TextView textView = (TextView) view;

        // TODO: test data
        Log.i("clickMessage2", "Click in position:"
                + position + " tile no: " + textView.getText());

        game.resolveClick(new Coordinates(position));
        // update();
    }

    public void update() {
        this.dataSet.clear();
        this.dataSet.addAll(game.getDataSet());

        this.backgroundSet.clear();
        this.backgroundSet.addAll(game.getBackgroundSet());

        notifyDataSetChanged();
    }
}
