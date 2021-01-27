package com.pharos.notea2pp.ui.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.pharos.notea2pp.Prefs;
import com.pharos.notea2pp.R;

public class ProfileFragment extends Fragment {
    ImageView icon;
    LottieAnimationView animation1, animation2, animation3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        icon = view.findViewById(R.id.profile_icon);
        icon.setOnClickListener(v -> mGetContext.launch("image/*"));
        animation1 = view.findViewById(R.id.lottie1);
        animation2 = view.findViewById(R.id.lottie2);
        animation3 = view.findViewById(R.id.lottie3);
        animation1.setAnimation(R.raw.writing);
        animation2.setAnimation(R.raw.guitar);
        animation3.setAnimation(R.raw.gymon);

        setHasOptionsMenu(true);
        return view;
    }

    ActivityResultLauncher<String> mGetContext = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    icon.setImageURI(result);
                }
            });

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.settings_del_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.delete){
            new Prefs(requireContext()).deleteBoardStatus();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.boardFragment);
            return true;
        } else
        return super.onOptionsItemSelected(item);
    }
}