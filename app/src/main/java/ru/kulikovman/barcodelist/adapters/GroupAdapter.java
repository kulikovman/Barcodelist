package ru.kulikovman.barcodelist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.kulikovman.barcodelist.R;
import ru.kulikovman.barcodelist.models.Good;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {
    private Realm mRealm;
    private Context mContext;
    private List<String> mGroups;

    public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GoodAdapter mAdapter;
        private TextView mGroupName;
        private RecyclerView mGroupRecyclerView;

        public GroupHolder(View itemView) {
            super(itemView);

            // Слушатель нажатий по элементу
            itemView.setOnClickListener(this);

            // Инициализируем вью элементы
            mGroupName = itemView.findViewById(R.id.item_group_name);
            mGroupRecyclerView = itemView.findViewById(R.id.item_group_recyclerview);
        }

        @Override
        public void onClick(View v) {
            if (mGroupRecyclerView.getVisibility() == View.GONE) {
                mGroupRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mGroupRecyclerView.setVisibility(View.GONE);
            }
        }

        public void bindGroup(String group) {
            // Устанавливаем название группы
            if (group.equals("")) {
                mGroupName.setText("Общая группа");
            } else {
                mGroupName.setText(group);
            }

            // Формируем список товаров группы
            RealmResults<Good> goods = mRealm.where(Good.class)
                    .equalTo(Good.GROUP, group)
                    .sort(Good.NAME, Sort.ASCENDING)
                    .findAll();

            // Подключаем адаптер
            mAdapter = new GoodAdapter(mContext, goods);
            mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mGroupRecyclerView.setAdapter(mAdapter);
            mGroupRecyclerView.setHasFixedSize(true);
        }
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        String group = mGroups.get(position);
        holder.bindGroup(group);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public GroupAdapter(Context context, List<String> groups, Realm realm) {
        mGroups = groups;
        mContext = context;
        mRealm = realm;
    }

    public void setGroups(List<String> groups) {
        mGroups = groups;
    }
}
