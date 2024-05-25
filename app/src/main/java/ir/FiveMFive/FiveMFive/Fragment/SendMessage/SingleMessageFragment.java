package ir.FiveMFive.FiveMFive.Fragment.SendMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.FiveMFive.FiveMFive.R;

public class SingleMessageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_message, container, false);

        return v;
    }
}