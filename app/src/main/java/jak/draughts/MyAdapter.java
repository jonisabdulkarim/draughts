package jak.draughts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<Integer> dataSet;
    private Board board;
    private int greenTileNumber; // playable tiles

    public MyAdapter(Context context, Board board) {
        this.context = context;
        this.board = board;
        this.dataSet = board.convertBoard();
        greenTileNumber = 1;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_board_tile, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        boolean isGreen = isGreen(position);

        if (isGreen) {
            holder.textView.setText(String.valueOf(greenTileNumber++));
        }
        holder.textView.setBackground(chooseColor(isGreen));

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
            case 2:
                holder.textView.setForeground(context.getDrawable(R.drawable.white_man_disc));
                break;
        }
    }

    private ColorDrawable chooseColor(boolean isGreen) {
        ColorDrawable color;

        if (isGreen) {
            color = new ColorDrawable(context.getColor(R.color.colorBoardGreen));
        } else {
            color = new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        }

        return color;
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

        board.resolveClick(position);
    }

    public void setDataSet(List<Integer> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
        // TODO: consider board method not being update but deleted
        // TODO: must use clear/addAll as above
    }
}
