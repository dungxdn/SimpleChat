package jp.bap.traning.simplechat.presenter.chattalks;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.bap.traning.simplechat.R;

public class PopUpBottomSheet extends BottomSheetDialogFragment {

    private static final String LAYOUT_ID = "LAYOUT_POPUP";
    private static final int layout = R.layout.popup_grid_bottom_sheet;

    public static PopUpBottomSheet getInstance() {
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID,layout);
        PopUpBottomSheet popUpBottomSheet = new PopUpBottomSheet();
        popUpBottomSheet.setArguments(bundle);
        return popUpBottomSheet;
    }

    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            String state = null;
            switch (newState) {
                case BottomSheetBehavior.STATE_COLLAPSED: {
                    state = "STATE_COLLAPSED";
                    break;
                }
                case BottomSheetBehavior.STATE_DRAGGING: {
                    state = "STATE_DRAGGING";
                    break;
                }
                case BottomSheetBehavior.STATE_EXPANDED: {
                    state = "STATE_EXPANDED";
                    break;
                }
                case BottomSheetBehavior.STATE_SETTLING: {
                    state = "STATE_SETTLING";
                    break;
                }
                case BottomSheetBehavior.STATE_HIDDEN: {
                    state = "STATE_HIDDEN";
                    dismiss();
                    break;
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(),getArguments().getInt(LAYOUT_ID),null);
        ButterKnife.bind(this,contentView);
        dialog.setContentView(contentView);

        BottomSheetBehavior<View> mViewBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        if(mViewBottomSheetBehavior != null) {
            mViewBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
            mViewBottomSheetBehavior.setPeekHeight(1000);
        }

    }

    @OnClick({R.id.popUpCopy,R.id.popUpShare,R.id.popUpDelete})
    public void onClickBottomSheet(View view) {
        int id = view.getId();
        switch (view.getId()) {
            case R.id.popUpCopy: {
                Toast.makeText(getContext(), "Day la copy", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            }
            case R.id.popUpShare: {
                Toast.makeText(getContext(), "Day la share", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            }
            case R.id.popUpDelete: {
                Toast.makeText(getContext(), "Day la xoa", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            }
        }
    }
}
