package sk.miroc.whitebikes.map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import sk.miroc.whitebikes.R;
import sk.miroc.whitebikes.data.models.Stand;
import sk.miroc.whitebikes.standdetail.StandActivity;

public class ClosestStandDialogFragment extends DialogFragment {
    private static final String EXTRA_STANDS = "stands";
    private ArrayList<Stand> stands;

    public ClosestStandDialogFragment() {
            // Empty constructor required
        }

        public static ClosestStandDialogFragment newInstance(ArrayList<Stand> stands) {
            ClosestStandDialogFragment frag = new ClosestStandDialogFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(EXTRA_STANDS, stands);
            frag.setArguments(args);
            return frag;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            this.stands = getArguments().getParcelableArrayList(EXTRA_STANDS);
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

            String standNames[] = new String[stands.size()];
            for (int i = 0; i < stands.size(); i++){
                standNames[i] = stands.get(i).getStandName();
            }

            b.setTitle(R.string.closest_stands)
                    .setItems(standNames, (dialog, selectedPos) -> {
                        Stand selectedStand = stands.get(selectedPos);
                        Intent intent = new Intent(getContext(), StandActivity.class);
                        intent.putExtra(StandActivity.EXTRA_STAND, selectedStand);
                        startActivity(intent);
                        dismiss();

                    });
            return b.create();
        }
    }
