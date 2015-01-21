package com.audacity.colorpickerdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by zr.faisal on 1/21/15.
 */
public class ColorPickerDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener, TextView.OnEditorActionListener {

    private TableLayout tlDialog;

    private SeekBar sbRed;
    private SeekBar sbGreen;
    private SeekBar sbBlue;
    private SeekBar sbAlpha;

    private EditText etRed;
    private EditText etGreen;
    private EditText etBlue;
    private EditText etAlpha;

    private int color;

    public interface ColorPickerListener {
        public void onDoneClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ColorPickerListener mListener;

    /*
    public static ColorPickerDialog newInstance(DialogInterface.OnClickListener listener) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        return colorPickerDialog;
    }
    */


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Color");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_picker, null);
        builder.setView(view);

        tlDialog = (TableLayout) view.findViewById(R.id.tableLayoutDialog);

        sbRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        sbRed.setOnSeekBarChangeListener(this);
        sbRed.setProgressDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));

        sbGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
        sbGreen.setOnSeekBarChangeListener(this);
        sbGreen.setProgressDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));

        sbBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        sbBlue.setOnSeekBarChangeListener(this);
        sbBlue.setProgressDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));

        sbAlpha = (SeekBar) view.findViewById(R.id.seekBarAlpha);
        sbAlpha.setOnSeekBarChangeListener(this);

        color = Color.argb(sbAlpha.getProgress(), sbRed.getProgress(), sbGreen.getProgress(), sbBlue.getProgress());

        etRed = (EditText) view.findViewById(R.id.editTextRed);
        etRed.setOnEditorActionListener(this);

        etGreen = (EditText) view.findViewById(R.id.editTextGreen);
        etGreen.setOnEditorActionListener(this);

        etBlue = (EditText) view.findViewById(R.id.editTextBlue);
        etBlue.setOnEditorActionListener(this);

        etAlpha = (EditText) view.findViewById(R.id.editTextAlpha);
        etAlpha.setOnEditorActionListener(this);

        // Positive Button
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do something
                mListener.onDoneClick(ColorPickerDialog.this);
            }
        });

        /*
        // Negative Button
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do something else
            }
        });
        */

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id) {
            case R.id.seekBarRed:
                seekBar.setProgressDrawable(new ColorDrawable(Color.rgb(progress, 0, 0)));
                etRed.setText(progress + "");
                break;
            case R.id.seekBarGreen:
                seekBar.setProgressDrawable(new ColorDrawable(Color.rgb(0, progress, 0)));
                etGreen.setText(progress + "");
                break;
            case R.id.seekBarBlue:
                seekBar.setProgressDrawable(new ColorDrawable(Color.rgb(0, 0, progress)));
                etBlue.setText(progress + "");
                break;
            case R.id.seekBarAlpha:
                etAlpha.setText(progress + "");
                break;
            default:
                break;
        }

        color = Color.argb(sbAlpha.getProgress(), sbRed.getProgress(), sbGreen.getProgress(), sbBlue.getProgress());
        tlDialog.setBackgroundColor(color);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            try {
                int id = textView.getId();
                switch (id) {
                    case R.id.editTextRed:
                        sbRed.setProgress(Integer.parseInt(textView.getText().toString()));
                        break;
                    case R.id.editTextGreen:
                        sbGreen.setProgress(Integer.parseInt(textView.getText().toString()));
                        break;
                    case R.id.editTextBlue:
                        sbBlue.setProgress(Integer.parseInt(textView.getText().toString()));
                        break;
                    case R.id.editTextAlpha:
                        sbAlpha.setProgress(Integer.parseInt(textView.getText().toString()));
                        break;
                    default:
                        break;
                }
                hideSoftKeyboard(textView);

                return true;

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void hideSoftKeyboard(TextView textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    public int getColor() {
        color = Color.argb(sbAlpha.getProgress(), sbRed.getProgress(), sbGreen.getProgress(), sbBlue.getProgress());
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    // Listener Interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ColorPickerListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
