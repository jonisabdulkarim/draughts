package jak.draughts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private String[] dataSet;
    private static int greenTileNumber = 1;

    public MyAdapter(Context context, String[] dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View textView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_board_tile, parent, false);

        return new MyViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        boolean isGreen = isGreen(position);

        if (isGreen)
            holder.textView.setText(greenTileNumber++ + "");

        holder.textView.setBackground(chooseColor(isGreen));
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
        if (position / 8 % 2 != 0 && position % 2 == 0) {
            return true;
        } else return position / 8 % 2 == 0 && position % 2 != 0;
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
        }
    }

}
