package com.example.android.samplethesis.ui.reminder;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TimePicker;
import com.example.android.samplethesis.AlarmReceiver;
import com.example.android.samplethesis.LocalData;
import com.example.android.samplethesis.NotificationScheduler;
import com.example.android.samplethesis.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static android.content.Context.CLIPBOARD_SERVICE;


public class ReminderFragment extends Fragment {

    private ReminderViewModel reminderViewModel;

    String TAG = "RemindMe";
    LocalData localData;

    SwitchCompat reminderSwitch;
    TextView tvTime;

    LinearLayout ll_set_time, ll_terms;

    int hour, min;

    ClipboardManager myClipboard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reminderViewModel =
                ViewModelProviders.of(this).get(ReminderViewModel.class);
        View root = inflater.inflate(R.layout.activity_reminder, container, false);


            localData = new LocalData(getActivity().getApplicationContext());

            myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

            ll_set_time = (LinearLayout) root.findViewById(R.id.ll_set_time);
            ll_terms = (LinearLayout) root.findViewById(R.id.ll_terms);

            tvTime = (TextView) root.findViewById(R.id.tv_reminder_time_desc);

            reminderSwitch = (SwitchCompat) root.findViewById(R.id.timerSwitch);

            hour = localData.get_hour();
            min = localData.get_min();

            tvTime.setText(getFormatedTime(hour, min));
            reminderSwitch.setChecked(localData.getReminderStatus());

            if (!localData.getReminderStatus())
                ll_set_time.setAlpha(0.4f);

            reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    localData.setReminderStatus(isChecked);
                    if (isChecked) {
                        Log.d(TAG, "onCheckedChanged: true");
                        NotificationScheduler.setReminder( getContext(), AlarmReceiver.class, localData.get_hour(), localData.get_min());
                        ll_set_time.setAlpha(1f);
                    } else {
                        Log.d(TAG, "onCheckedChanged: false");
                        NotificationScheduler.cancelReminder(getContext(), AlarmReceiver.class);
                        ll_set_time.setAlpha(0.4f);
                    }

                }
            });

            ll_set_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (localData.getReminderStatus())
                        showTimePickerDialog(localData.get_hour(), localData.get_min());
                }
            });

            ll_terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



        return root;}


        private void showTimePickerDialog(int h, int m) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.timepicker_header, null);

            TimePickerDialog builder = new TimePickerDialog(getActivity() , R.style.DialogTheme,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int min) {
                            Log.d(TAG, "onTimeSet: hour " + hour);
                            Log.d(TAG, "onTimeSet: min " + min);
                            localData.set_hour(hour);
                            localData.set_min(min);
                            tvTime.setText(getFormatedTime(hour, min));
                            NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, localData.get_hour(), localData.get_min());


                        }
                    }, h, m, false);

            builder.setCustomTitle(view);
            builder.show();

        }

        public String getFormatedTime(int h, int m) {
            final String OLD_FORMAT = "HH:mm";
            final String NEW_FORMAT = "hh:mm a";

            String oldDateString = h + ":" + m;
            String newDateString = "";

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
                Date d = sdf.parse(oldDateString);
                sdf.applyPattern(NEW_FORMAT);
                newDateString = sdf.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return newDateString;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public Locale getCurrentLocale() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return getResources().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                return getResources().getConfiguration().locale;
            }
        }


}