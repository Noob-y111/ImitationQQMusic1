package com.example.imitationqqmusic.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imitationqqmusic.R;
import com.example.imitationqqmusic.base.BaseFragment;
import com.example.imitationqqmusic.databinding.FragmentTestBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {

    private FragmentTestBinding binding;


    @NonNull
    @Override
    protected View setRootView() {
        binding = FragmentTestBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected int getToolBarId() {
        return R.id.test_toolbar;
    }

    @Override
    protected void initView() {

//        binding.testToolbar.setPadding(0,getStatusBarHeight(),0,0);
//        System.out.println("==============================getStatusBarHeight(): " + getStatusBarHeight());

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        Adapter adapter = new Adapter(Adapter.itemCallback);
        binding.recyclerview.setAdapter(adapter);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        adapter.submitList(list);

    }

    @Override
    protected void toMusicDetail(@NonNull Bundle bundle) {

    }

    @Override
    protected void onCreateOptionMenuOnToolBar(Toolbar toolbar) {
        super.onCreateOptionMenuOnToolBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.fragment).navigateUp();
            }
        });
        toolbar.inflateMenu(R.menu.default_white_more);
    }

    @Override
    protected void onToolBarOptionMenuSelected(MenuItem menuItem) {
        super.onToolBarOptionMenuSelected(menuItem);
        Toast.makeText(requireContext(), "More...", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    protected String getTitle() {
        return "测试";
    }

    public static class Adapter extends ListAdapter<Integer, Adapter.MyHolder>{

        public static DiffUtil.ItemCallback<Integer> itemCallback = new DiffUtil.ItemCallback<Integer>(){

            @Override
            public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
                return oldItem.intValue() == newItem.intValue();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
                return oldItem.equals(newItem);
            }
        };

        private OnItemClickListener listener;

        public interface OnItemClickListener{
            void onClick(int position);
        }

        public Adapter(@NonNull DiffUtil.ItemCallback<Integer> diffCallback) {
            super(diffCallback);
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final MyHolder holder = new MyHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.simple_item, parent, false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) return;
                    listener.onClick(holder.getAbsoluteAdapterPosition());
                }
            });
            return holder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.textView.setText("position:" + position);
        }

        static class MyHolder extends RecyclerView.ViewHolder{

            private TextView textView;

            MyHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_view);
            }
        }
    }
}
