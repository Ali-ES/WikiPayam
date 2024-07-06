package ir.FiveMFive.FiveMFive.ViewCreators;

import static ir.FiveMFive.FiveMFive.Utility.UM.checkEditNulls;
import static ir.FiveMFive.FiveMFive.Utility.UM.hideKeyboard;
import static ir.FiveMFive.FiveMFive.Utility.UM.setEditTextLayoutFocus;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.FiveMFive.FiveMFive.Interface.ListModifyListener;
import ir.FiveMFive.FiveMFive.Java.Group;
import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.RecyclerViewAdapter;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;

public class GroupDialogBuilder implements ListModifyListener {
    private static final String TAG = "GroupDialog";
    private Fragment fragment;
    private Context c;
    private View v;
    private View mainLayout;
    private RecyclerView groupRecycle;
    private RecyclerViewAdapter adapter;
    private List<Group> groups;
    private List<Group> selectedGroups;
    private ChangeListener changeListener;

    public interface ChangeListener {
        void onRemove();
        void onAdd();
    }

    public GroupDialogBuilder(Fragment fragment, List<Group> groups, List<Group> selectedGroups, View v) {
        this.fragment = fragment;
        this.c = fragment.requireContext();
        this.groups = groups;
        this.selectedGroups = selectedGroups;
        this.v = v;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout = inflater.inflate(R.layout.dialog_groups, null);
        groupRecycle = mainLayout.findViewById(R.id.group_recycler);

        setUpRecyclerView();
        handleAddButton();
    }
    private void setUpRecyclerView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(c);
                groupRecycle.setLayoutManager(manager);

                adapter = new RecyclerViewAdapter(fragment, RecyclerViewAdapter.LayoutType.GROUP, (ArrayList<Group>) selectedGroups);
                adapter.setListModifyListener(GroupDialogBuilder.this);
                groupRecycle.setAdapter(adapter);
            }
        }, 100);

    }

    public void showDialog() {
        new AlertDialog.Builder(c)
                .setView(mainLayout)
                .show();
    }

    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }

    private void handleAddButton() {
        ConstraintLayout groupLayout = mainLayout.findViewById(R.id.group_layout);
        TextView groupText = groupLayout.findViewById(R.id.group_tv);
        AutoCompleteTextView groupCompleteText = groupLayout.findViewById(R.id.group_ctv);
        setEditTextLayoutFocus(c, groupLayout, groupText, groupCompleteText);

        Button add = mainLayout.findViewById(R.id.add_bt);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkEditNulls(groupCompleteText)) {
                    String groupTitle = groupCompleteText.getText().toString();
                    boolean isGroupDuplicate = false;
                    for(Group group : selectedGroups) {
                        if (group.getName().equals(groupTitle)) {
                            isGroupDuplicate = true;
                            String warnMessage = c.getString(R.string.warn_duplicate_group);
                            SnackbarBuilder.showSnack(c, v, warnMessage, SnackbarBuilder.SnackType.WARNING);
                            groupCompleteText.setText("");
                            break;
                        }
                    }
                    Group selectedGroup = null;
                    if(!isGroupDuplicate) {
                        for(Group group : groups) {
                            if(group.getName().equals(groupTitle)) {
                                selectedGroup = group;
                                break;
                            }
                        }
                    }
                    if(selectedGroup != null) {
                        selectedGroups.add(selectedGroup);
                        int newPosition = selectedGroups.size() - 1;
                        adapter.notifyItemChanged(newPosition);
                        groupRecycle.smoothScrollToPosition(newPosition);
                        if(changeListener != null) {
                            changeListener.onAdd();
                        }
                        groupCompleteText.setText("");

                        String successMessage = c.getString(R.string.success_adding_group);
                        SnackbarBuilder.showSnack(c, v, successMessage, SnackbarBuilder.SnackType.SUCCESS);
                    } else if(!isGroupDuplicate) {
                        String errorMessage = c.getString(R.string.error_group_not_exist);
                        SnackbarBuilder.showSnack(c, v, errorMessage, SnackbarBuilder.SnackType.ERROR);
                    }
                } else {
                    hideKeyboard(c, groupCompleteText);
                    String warnMessage = c.getString(R.string.warn_empty_group);
                    SnackbarBuilder.showSnack(c, v, warnMessage, SnackbarBuilder.SnackType.WARNING);
                }
            }
        });

        groupCompleteText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    add.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onListRemove(int position) {
        selectedGroups.remove(position);
        if(changeListener != null) {
            changeListener.onRemove();
        }
    }
}
