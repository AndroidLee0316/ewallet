package com.pasc.business.ewallet.widget.dialog.bottomchoice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.adapter.base.BaseViewHolder;
import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.BaseDialogFragment;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnSingleChoiceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部选择对话框.
 * Created by chenruihan410 on 2018/10/11.
 */
public class BottomChoiceDialogFragment extends BaseDialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CLOSE_TEXT = "closeText";
    private static final String ARG_ITEMS = "items";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.EwalletBottomChoiceDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ewallet_widget_dialog_bottom_choice, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.ewallet_pay_yue_record_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decor.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ewallet_widget_bg_list_divider));
        recyclerView.addItemDecoration(decor);

        ArrayList<CharSequence> items = getArgSerializable(ARG_ITEMS, new ArrayList<CharSequence>());

        ChoiceAdapter iconActionAdapter = new ChoiceAdapter(items);
        iconActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sendMessageSingleChoice(position);
            }
        });

        iconActionAdapter.bindToRecyclerView(recyclerView);

        // 标题
        TextView titleTextView = view.findViewById(R.id.title);
        CharSequence title = getArgCharSequence(ARG_TITLE, "");
        if("".equals(title)){
            titleTextView.setVisibility(View.GONE);
        }
        titleTextView.setText(title);

        // 关闭按钮
        TextView closeButton = view.findViewById(R.id.close);
        CharSequence closeText = getArgCharSequence(ARG_CLOSE_TEXT, "取消");
        closeButton.setText(closeText);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sendMessage(ARG_ON_CLOSE_LISTENER)) {
                    dismiss();
                }
            }
        });
    }

    class ChoiceAdapter extends BaseQuickAdapter<CharSequence, BaseViewHolder> {

        public ChoiceAdapter(@Nullable List<CharSequence> data) {
            super(R.layout.ewallet_widget_item_bottom_choice, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CharSequence item) {

            String textString = item.toString();
            if(textString.contains("\n")){
                SpannableStringBuilder spanStyle = new SpannableStringBuilder(item);
                spanStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ewallet_dialog_explain_text)),
                        textString.indexOf("\n"), item.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
                spanStyle.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(13)), textString.indexOf("\n"), item.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.text, spanStyle);

            }else {
                helper.setText(R.id.text, item);
            }


        }
    }


    public static class Builder {

        ArrayList<CharSequence> items;
        CharSequence title; // 标题
        CharSequence closeText; // 关闭文本
        OnCloseListener<BottomChoiceDialogFragment> onCloseListener; // 关闭监听器
        OnSingleChoiceListener<BottomChoiceDialogFragment> onSingleChoiceListener; // 单选监听器

        public Builder setItems(ArrayList<CharSequence> items) {
            this.items = items;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setCloseText(CharSequence closeText) {
            this.closeText = closeText;
            return this;
        }

        public Builder setOnCloseListener(OnCloseListener<BottomChoiceDialogFragment> onCloseListener) {
            this.onCloseListener = onCloseListener;
            return this;
        }

        public Builder setOnSingleChoiceListener(OnSingleChoiceListener<BottomChoiceDialogFragment> onSingleChoiceListener) {
            this.onSingleChoiceListener = onSingleChoiceListener;
            return this;
        }

        public BottomChoiceDialogFragment build() {
            BottomChoiceDialogFragment dialogFragment = new BottomChoiceDialogFragment();

            Bundle args = new Bundle();
            if (items != null) {
                args.putSerializable(ARG_ITEMS, items);
            }
            if (!TextUtils.isEmpty(title)) {
                args.putCharSequence(ARG_TITLE, title);
            }
            if (!TextUtils.isEmpty(closeText)) {
                args.putCharSequence(ARG_CLOSE_TEXT, closeText);
            }
            if (onSingleChoiceListener != null) {
                args.putParcelable(ARG_ON_SINGLE_CHOICE_LISTENER, dialogFragment.obtainMessage(WHAT_ON_SINGLE_CHOICE_LISTENER, onSingleChoiceListener));
            }
            if (onCloseListener != null) {
                args.putParcelable(ARG_ON_CLOSE_LISTENER, dialogFragment.obtainMessage(WHAT_ON_CLOSE_LISTENER, onCloseListener));
            }

            dialogFragment.setArguments(args);
            dialogFragment.setCancelable(true);
            return dialogFragment;
        }
    }
}
