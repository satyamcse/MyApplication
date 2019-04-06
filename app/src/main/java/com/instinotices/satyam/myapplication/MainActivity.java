package com.instinotices.satyam.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MyAdapter.SelectedItemCountChanged {
    BottomSheetDialog mBottomSheetDialog;
    Button continueButton;
    View sheetView;
    CoordinatorLayout mainActivtyRoot;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    RadioGroup radioGroup1;
    int stage = 0;
    LinearLayout additionalOptions1, additional_options2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivtyRoot = findViewById(R.id.main_actvity_root);
        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        initialiseBottomSheet();
    }

    void initialiseBottomSheet() {
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        BottomSheetBehavior.from(sheetView.findViewById(R.id.bottom_sheet)).setState(BottomSheetBehavior.STATE_EXPANDED);
        additionalOptions1 = sheetView.findViewById(R.id.additional_options_1);
        additional_options2 = sheetView.findViewById(R.id.additional_options_2);
        recyclerView = (RecyclerView) sheetView.findViewById(R.id.my_recycler_view);
        radioGroup1 = sheetView.findViewById(R.id.radio_gp_1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TransitionManager.beginDelayedTransition((ViewGroup) MainActivity.this.findViewById(R.id.main_actvity_root));
            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String[] ary = {"PHYSICS", "CHEMISTRY", "MATHEMATICS", "ENGLISH", "HINDI", "BIOLOGY", "COMPUTER"};
        ArrayList<String> dta = new ArrayList<>(Arrays.asList(ary));
        mAdapter = new MyAdapter(dta, this);
        recyclerView.setAdapter(mAdapter);
        continueButton = sheetView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.main_actvity_root));
                if (stage == 1) {
                    additionalOptions1.setVisibility(View.VISIBLE);
                    stage = 2;
                } else if (stage == 2) {
                    additional_options2.setVisibility(View.VISIBLE);
                    stage = 3;
                    BottomSheetBehavior.from(sheetView.findViewById(R.id.bottom_sheet)).setState(BottomSheetBehavior.STATE_EXPANDED);
                    final ScrollView scrollView = sheetView.findViewById(R.id.bottom_sheet);
                    sheetView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    continueButton.setEnabled(false);
                } else {
                    mBottomSheetDialog.hide();
                    Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                }

            }

        });
        //continueButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSheet(View view) {
        mBottomSheetDialog.show();
        if (stage != 3) continueButton.setEnabled(true);
        stage = 1;
    }

    @Override
    public void onSelectedItemCountChanged(int selectedItems) {
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.main_actvity_root));
        if (selectedItems <= 5 && selectedItems >= 3) {
            continueButton.setEnabled(true);
        } else {
            continueButton.setEnabled(false);
        }
    }

}
