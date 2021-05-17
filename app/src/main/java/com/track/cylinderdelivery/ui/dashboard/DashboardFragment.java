package com.track.cylinderdelivery.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.track.cylinderdelivery.R;

public class DashboardFragment extends Fragment {

    //private DashboardViewModel dashboardViewModel;
    Context context;

    public DashboardFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //RecyclerView recyclerView=root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      //  recyclerView.setLayoutManager(layoutManager);
        context=getActivity();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}