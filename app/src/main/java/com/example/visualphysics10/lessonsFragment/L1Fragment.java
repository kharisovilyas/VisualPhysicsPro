package com.example.visualphysics10.lessonsFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.visualphysics10.MainActivity;
import com.example.visualphysics10.R;
import com.example.visualphysics10.database.App;
import com.example.visualphysics10.database.AppDataBase;
import com.example.visualphysics10.database.LessonData;
import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.databinding.L1FragmentBinding;
import com.example.visualphysics10.inform.input.FullScreenDialog;
import com.example.visualphysics10.inform.output.FullScreenInfo;
import com.example.visualphysics10.inform.test.FragmentTest;
import com.example.visualphysics10.lessonInformFragment.L1FragInform;
import com.example.visualphysics10.objects.PhysicsModel;
import com.example.visualphysics10.physics.PhysicView;
import com.example.visualphysics10.ui.MainFlag;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.Objects;

public class L1Fragment extends Fragment {
    private L1FragmentBinding binding;
    private PhysicView gameView;
    private boolean flagInput = false;
    private boolean startToast = true;
    private boolean startVisual = true;
    public static boolean isMoving = false;
    private EditText input_speed;
    private EditText input_acc;
    private FloatingActionButton info;
    private FloatingActionButton play;
    AppDataBase db = App.getInstance().getDatabase();

    //TODO:
    //        ПЕРЕДЕЛАТЬ ЭТО МЕСТО, пока написан костыль...
    LessonData lessonData = FullScreenDialog.getInstance();
    //TODO:
    private boolean endInput = true;
    private int countListener = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = L1FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addToolbar();
        gameView = binding.physicsView;
        MainActivity.isFragment = true;
        PhysicsModel.L1 = true;
        MainFlag.setThreadStop(false);
        gameView.addModelGV(0);
        FloatingActionButton play = binding.play;
        FloatingActionButton restart = binding.restart;
        FloatingActionButton startInput = binding.startInput;
        FloatingActionButton startTest = binding.startTest;
        info = binding.info;
        play.setOnClickListener(v -> {
            play.setImageResource(R.drawable.pause_circle);
            flagInput = false;
            isMoving = true;
            db.dataDao().getAllLiveData();
            info.setVisibility(View.VISIBLE);
            PhysicsData.setAcc(lessonData.acc);
            PhysicsData.setDistance(lessonData.distance);
            gameView.updateMoving(lessonData.speed, 0, 0);
        });
        restart.setOnClickListener(v -> {
            startVisual = true;
            createDialog();
        });
        startInput.setOnClickListener(v -> {
            //toggleBottomSheetInput();
            createdFullScreenDialog();
        });

        startTest.setOnClickListener(v->{
            startTesting();
        });

        info.setOnClickListener(v -> {
            gameView.stopThread();
            createdFullScreenInfo();
        });
    }


    private void startTesting() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                .replace(R.id.container, new FragmentTest())
                .addToBackStack(null)
                .commit();
    }

    private void createdFullScreenInfo() {
        DialogFragment dialogInfoFragment = FullScreenInfo.newInstance();
        dialogInfoFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "info");
    }

    private void createdFullScreenDialog() {
        DialogFragment dialogFragment = FullScreenDialog.newInstance();
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "input");
    }

    @SuppressLint("ResourceType")
    private void createDialog() {
        new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle("Перезапуск")
                .setMessage("Сохранить введенные данные?")
                .setCancelable(false)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        restartAll();
                    }
                })
                .setNegativeButton("Записать новые", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartAll();
                        db.dataDao().delete(lessonData);
                    }
                })
                .show();
    }

    private void restartAll() {

    }


    private void addToolbar() {
        Toolbar toolbar = binding.toolbar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setTitle(R.string.titleL1);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toggleBottomSheetInput() {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.l1_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        dialog.setContentView(view);
        dialog.show();
        input_speed = view.findViewById(R.id.input_speed);
        input_acc = view.findViewById(R.id.input_acc);
        FloatingActionButton saveInput = view.findViewById(R.id.save);
        saveInput.setOnClickListener(v -> {
            try {
                saveData();
                startToast = false;
                startVisual = false;
                flagInput = true;
                info.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
    }


    private void saveData() {
        lessonData.speed = Double.parseDouble(input_speed.getText().toString());
        lessonData.acc = Double.parseDouble(input_acc.getText().toString());
        rightInput(lessonData.speed, lessonData.acc);
        db.dataDao().insert(lessonData);
    }

    @SuppressLint("SetTextI18n")
    private void rightInput(double speed, double acc) {
        //TODO: заменить на проверку пустой строки в LoginFragment
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void toggleBottomSheetOutput() {
        View view = getLayoutInflater().inflate(R.layout.l1_bottom_sheet_output, null);
        BottomSheetDialog dialog = new BottomSheetDialog(
                Objects.requireNonNull(getContext()), R.style.BottomSheetDialogTheme
        );
        TextView output_speed = view.findViewById(R.id.output_speed);
        output_speed.setText((int) lessonData.speed + " [м/с] - начальная скорость");
        TextView output_speedEnd = view.findViewById(R.id.output_speedEnd);
        output_speedEnd.setText(new DecimalFormat("#0.00").format(PhysicsData.getSpeedEnd()) + " [м/с] - конечная скорость");
        TextView output_distance = view.findViewById(R.id.output_distance);
        output_distance.setText("1000 [м] - расстояние");
        TextView output_acc = view.findViewById(R.id.output_acc);
        output_acc.setText((int) lessonData.acc + " [м/с^2] - ускорение");
        FloatingActionButton restartInput = view.findViewById(R.id.restart);
        restartInput.setOnClickListener(v -> {
            PhysicsModel.onRestartClick = true;
            startVisual = true;
            flagInput = true;
            restartAll();
            dialog.dismiss();
        });
        Button toNextFrag = view.findViewById(R.id.toNextFrag);
        toNextFrag.setOnClickListener(v -> {
            gameView.stopThread();
            MainFlag.setThreadStop(true);
            dialog.dismiss();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim)
                    .replace(R.id.container, new L1FragInform())
                    .addToBackStack(null)
                    .commit();
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
