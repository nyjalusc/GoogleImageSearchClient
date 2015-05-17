package com.example.naugustine.gridimagesearch.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.naugustine.gridimagesearch.R;
import com.example.naugustine.gridimagesearch.models.AdvancedFilters;

public class AdvancedFiltersDialog extends DialogFragment {
    private View view;
    private Spinner spSize;
    private Spinner spColor;
    private Spinner spType;
    private Spinner spSafetyLevel;
    private EditText etSite;
    private AdvancedFilters advancedFilters;

    public AdvancedFiltersDialog() {
        // Empty constructor required for DialogFragment
    }

    // Used to pass the filter settings model from activity to fragment
    public static AdvancedFiltersDialog newInstance(AdvancedFilters advancedFilters) {
        AdvancedFiltersDialog frag = new AdvancedFiltersDialog();
        Bundle args = new Bundle();
        args.putSerializable("advancedFilterSettings", advancedFilters);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        advancedFilters = (AdvancedFilters) getArguments().getSerializable("advancedFilterSettings");
        // Check styles.xml for the note on problem
        // Using AlertDialog instead of AlertDialog.builder because dismiss() does not work with .builder class
        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog).create();
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.advanced_filters, null);

        // Setup Click listeners for save and cancel button
        // SAVE
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAdvancedFilterSettings();
                builder.dismiss();
            }
        });

        // CANCEL
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        // Populate the values of view
        setViews();
        // Attach the view to the alertdialog builder
        builder.setView(view);
        return builder;
    }

    // Extract values from the view and save the model
    private void saveAdvancedFilterSettings() {
        String size = (String) spSize.getSelectedItem();
        advancedFilters.setSize(size);
        String color = (String) spColor.getSelectedItem();
        advancedFilters.setColor(color);
        String type = (String) spType.getSelectedItem();
        advancedFilters.setType(type);
        String safetyLevel = (String) spSafetyLevel.getSelectedItem();
        advancedFilters.setSafetyLevel(safetyLevel);
        String siteFilter = etSite.getText().toString();
        advancedFilters.setSite(siteFilter);
        advancedFilters.save();
    }

    // Render the views with correct values
    private void setViews() {
        // Set spinner for size
        spSize = (Spinner) view.findViewById(R.id.spSize);
        setSpinnerView(spSize, R.array.imgsize, advancedFilters.getSize());

        // Set spinner for color
        spColor = (Spinner) view.findViewById(R.id.spColor);
        setSpinnerView(spColor, R.array.imgcolor, advancedFilters.getColor());

        // Set spinner for type
        spType = (Spinner) view.findViewById(R.id.spType);
        setSpinnerView(spType, R.array.imgtype, advancedFilters.getType());

        // Set spinner for safety level
        spSafetyLevel = (Spinner) view.findViewById(R.id.spSafetyLevel);
        setSpinnerView(spSafetyLevel, R.array.safety_level, advancedFilters.getSafetyLevel());

        // Set the editText for site
        etSite = (EditText) view.findViewById(R.id.etSiteName);
        etSite.setText(advancedFilters.getSite());
    }

    // Populates value in dropdown spinner and sets the spinner to custom filter settings
    private void setSpinnerView(Spinner spinner, int textArrayResId, String text) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), textArrayResId, R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set the spinner to custom settings OR to default values if there are no custom filter settings
        int spinnerPosition = adapter.getPosition(text);
        spinner.setSelection(spinnerPosition);
    }
}
