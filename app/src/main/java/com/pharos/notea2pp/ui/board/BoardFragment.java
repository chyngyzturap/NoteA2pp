package com.pharos.notea2pp.ui.board;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pharos.notea2pp.OnItemClickListener;
import com.pharos.notea2pp.Prefs;
import com.pharos.notea2pp.R;

public class BoardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        BoardAdapter adapter = new BoardAdapter();
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                ((tab, position) -> tab.setText(""))).attach();
        adapter.setBoardFragmentListener(() -> {
            new Prefs(requireContext()).saveBoardStatus();
            close();
        });
        view.findViewById(R.id.imgBtn_skip).setOnClickListener(v -> {close();});
    }

    private void close() {
        Prefs prefs = new Prefs(requireContext());
        prefs.saveBoardStatus();
        NavController navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(R.id.phoneFragment);
    }
}