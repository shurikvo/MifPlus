package ru.shurikvo.mifplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SettingDialog extends DialogFragment {

    private final String[] catNamesArray = {"SL0 -> SL1"};
    private final boolean[] checkedItemsArray = {false};

    public String transfer = "N";

    public interface MyDialogFragmentListener { public void onReturnValue(String foo); }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(transfer.substring(0,1).equals("Y"))
            checkedItemsArray[0] = true;
        else
            checkedItemsArray[0] = false;

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Настройки")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(R.layout.dialog)
                .setMultiChoiceItems(catNamesArray, checkedItemsArray,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which, boolean isChecked) {
                                checkedItemsArray[which] = isChecked;
                            }
                        })
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(checkedItemsArray[0])
                                    transfer = "Y";
                                else
                                    transfer = "N";
                                MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                                activity.onReturnValue("transfer:"+transfer);
                            }
                        })
                .setNegativeButton("Отмена", null)
                .create();
    }
}
