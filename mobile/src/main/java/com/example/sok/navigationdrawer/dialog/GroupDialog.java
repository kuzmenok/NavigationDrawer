package com.example.sok.navigationdrawer.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sok.navigationdrawer.R;
import com.example.sok.navigationdrawer.data.Group;
import com.example.sok.navigationdrawer.listener.SimpleTextWatcher;

public class GroupDialog extends DialogFragment {
    private GroupDialogCallback mCallback;
    private EditText etName;
    private TextInputLayout tlName;

    public interface GroupDialogCallback {
        void onGroupCreated(Group newGroup);
    }

    private final MaterialDialog.ButtonCallback mButtonCallback = new MaterialDialog.ButtonCallback() {
        @Override
        public void onPositive(MaterialDialog dialog) {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                tlName.setError(getString(R.string.error_empty_name));
            } else {
                mCallback.onGroupCreated(new Group(name));
                dialog.dismiss();
            }
        }
        @Override
        public void onNegative(MaterialDialog materialDialog) {
            materialDialog.dismiss();
        }
    };

    public void setCallback(GroupDialogCallback callback) {
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.new_group)
                .callback(mButtonCallback)
                .autoDismiss(false)
                .customView(R.layout.group_dialog, false)
                .positiveText(R.string.create)
                .negativeText(android.R.string.cancel)
                .build();

        View customView = dialog.getCustomView();
        etName = (EditText) customView.findViewById(R.id.et_name);
        tlName = (TextInputLayout) customView.findViewById(R.id.tl_name);
        etName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    tlName.setError(null);
                }
            }
        });
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dialog.getActionButton(DialogAction.POSITIVE).performClick();
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }
}
