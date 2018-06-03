package ru.kulikovman.barcodelist.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.kulikovman.barcodelist.EditGoodActivity;
import ru.kulikovman.barcodelist.R;
import ru.kulikovman.barcodelist.models.Good;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.GoodHolder> {
    private Context mContext;
    private List<Good> mGoods;

    public GoodAdapter(Context context, List<Good> goods) {
        setHasStableIds(true);
        mContext = context;
        mGoods = goods;
    }

    public class GoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Good mGood;
        private TextView mGoodName;

        public GoodHolder(View itemView) {
            super(itemView);

            // Слушатель нажатий по элементу
            itemView.setOnClickListener(this);

            // Инициализируем вью элементы
            mGoodName = itemView.findViewById(R.id.item_good_name);
        }

        @Override
        public void onClick(View v) {
            // Открываем товар при нажатии
            Intent editGoodActivity = new Intent(mContext, EditGoodActivity.class);
            editGoodActivity.putExtra("barcode", mGood.getBarcode());
            mContext.startActivity(editGoodActivity);
        }

        public void bindGood(Good good) {
            mGood = good;
            String textForItem = good.getBarcode() + " - " + good.getName();
            mGoodName.setText(textForItem);
        }
    }

    @NonNull
    @Override
    public GoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good, parent, false);
        return new GoodAdapter.GoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodHolder holder, int position) {
        Good good = mGoods.get(position);
        holder.bindGood(good);
    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }
}
