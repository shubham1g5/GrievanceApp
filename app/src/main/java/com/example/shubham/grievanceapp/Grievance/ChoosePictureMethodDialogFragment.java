package com.example.shubham.grievanceapp.Grievance;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.shubham.grievanceapp.R;

public class ChoosePictureMethodDialogFragment extends DialogFragment {

    private ChoosePictureMethodInterface choosePictureMethodInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ChoosePictureMethodInterface){
            choosePictureMethodInterface = (ChoosePictureMethodInterface) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement ChoosePictureMethodInterface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_pic_method)
                .setItems(R.array.choose_pic_methods,
                        (dialog, which) -> {
                            choosePictureMethodInterface.selectMethod(which);
                            dismiss();
                        });
        return builder.create();
    }
}
