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
        holder.textView.setText(dataSet[position]);
        holder.textView.setBackground(chooseColor(position));
    }

    private ColorDrawable chooseColor(int position) {
        ColorDrawable color;

        if (position / 8 % 2 == 0 && position % 2 == 0) {
            color = new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        } else if (position / 8 % 2 == 0) {
            color = new ColorDrawable(context.getColor(R.color.colorBoardGreen));
        } else if (position % 2 == 0) {
            color = new ColorDrawable(context.getColor(R.color.colorBoardGreen));
        } else {
            color = new ColorDrawable(context.getColor(R.color.colorBoardBuff));
        }

        return color;
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
