package jp.bap.traning.simplechat.presenter.chattalks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.ui.ChatTalksActivity_;

public class PopUpBottomSheet extends BottomSheetDialogFragment {

    private static final String LAYOUT_ID = "LAYOUT_POPUP";
    private static final int layout = R.layout.popup_grid_bottom_sheet;
    private static long idMessage= -1;
    private static int roomId = -1;
    public static PopUpBottomSheet getInstance(long id, int room) {
        idMessage = id;
        roomId = room;
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
        switch (view.getId()) {
            case R.id.popUpCopy: {
                try{
                    Message message = new MessagePresenter().getAMessage(idMessage);
                    if(message==null) {
                    } else {
                        Toast.makeText(getActivity(),"Đã sao chép văn bản",Toast.LENGTH_SHORT).show();
                        copyTextMessage(message.getContent());
                    }
                    dismiss();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case R.id.popUpShare: {
                try{
                    Toast.makeText(getActivity(),"Shared the text",Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
                break;
            }
            case R.id.popUpDelete: {
                try{
                    confirmDialog(idMessage);
                    dismiss();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
                break;
            }
        }
    }

    private void copyTextMessage(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simpletext", text);
        clipboard.setPrimaryClip(clip);
    }

    private void confirmDialog(long idMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa tin nhắn?")
                .setMessage("Bạn không thể hoàn tác sau khi xóa bản sao của những tin nhắn này")
                .setPositiveButton("Yes", (dialog, id) -> {
                    try{
                        Toast.makeText(builder.getContext(),"Đã xóa",Toast.LENGTH_SHORT).show();
                        new MessagePresenter().deleteMessage(idMessage);
                        ChatTalksActivity_.intent(builder.getContext()).roomId(roomId).start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .show();

    }




}
