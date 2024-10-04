package com.pasc.business.ewallet.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BaseDialogFragment extends AppCompatDialogFragment {

    public static final String ARG_ON_DISMISS_LISTENER = "onDismissListener";
    public static final String ARG_ON_CONFIRM_LISTENER = "onConfirmListener";
    public static final String ARG_ON_CLOSE_LISTENER = "onCloseListener";
    public static final String ARG_ON_SINGLE_CHOICE_LISTENER = "onSingleChoiceListener";
    public static final String ARG_ON_CONFIRM_CHOICE_STATE_LISTENER = "onConfirmChoiceStateListener";
    public static final String ARG_ON_MULTI_CHOICE_LISTENER = "onMultiChoiceListener";

    public static final int WHAT_ON_DISMISS_LISTENER = 1;
    public static final int WHAT_ON_CONFIRM_LISTENER = 2;
    public static final int WHAT_ON_CLOSE_LISTENER = 3;
    public static final int WHAT_ON_SINGLE_CHOICE_LISTENER = 4;
    public static final int WHAT_ON_CONFIRM_CHOICE_STATE_LISTENER = 5;
    public static final int WHAT_ON_MULTI_CHOICE_LISTENER = 6;

    @SuppressWarnings("unchecked")
    private static class MessageHandler extends Handler {

        final WeakReference<BaseDialogFragment> dialogRef;

        private MessageHandler(BaseDialogFragment dialogFragment) {
            dialogRef = new WeakReference<>(dialogFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseDialogFragment dialogFragment = dialogRef.get();
            if (dialogFragment != null) {
                Object obj = msg.obj;
                switch (msg.what) {
                    case WHAT_ON_DISMISS_LISTENER: {
                        if (obj != null && obj instanceof OnDismissListener) {
                            ((OnDismissListener) obj).onDismiss(dialogFragment);
                        }
                    }
                    break;
                    case WHAT_ON_CONFIRM_LISTENER: {
                        if (obj != null && obj instanceof OnConfirmListener) {
                            ((OnConfirmListener) obj).onConfirm(dialogFragment);
                        }
                    }
                    break;
                    case WHAT_ON_CLOSE_LISTENER: {
                        if (obj != null && obj instanceof OnCloseListener) {
                            ((OnCloseListener) obj).onClose(dialogFragment);
                        }
                    }
                    break;
                    case WHAT_ON_SINGLE_CHOICE_LISTENER: {
                        if (obj != null && obj instanceof OnSingleChoiceListener) {
                            ((OnSingleChoiceListener) obj).onSingleChoice(dialogFragment, msg.arg1);
                        }
                    }
                    break;

                    case WHAT_ON_CONFIRM_CHOICE_STATE_LISTENER: {
                        if (obj != null && obj instanceof OnConfirmChoiceStateListener) {
                            boolean isCheckState = false;
                            if (msg.arg1 == 1) {
                                isCheckState = true;
                            }
                            ((OnConfirmChoiceStateListener) obj).onConfirm(dialogFragment, isCheckState);
                        }
                    }
                    break;
                    case WHAT_ON_MULTI_CHOICE_LISTENER: {
                        if (obj != null && obj instanceof OnMultiChoiceListener) {
                            List<Integer> list = new ArrayList<>();
                            list.add(1);
                            ((OnMultiChoiceListener) obj).onMultiChoice(dialogFragment, list);
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }
    }

    private final MessageHandler messageHandler = new MessageHandler(this);

    /**
     * 发送在Arguments里的消息.
     *
     * @param key 指定的消息.
     * @return 发送是否成功，true代表发送成功，false代表发送失败.
     */
    protected boolean sendMessage(String key) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable parcelable = arguments.getParcelable(key);
            if (parcelable != null && parcelable instanceof Message) {
                Message obtain = Message.obtain(((Message) parcelable));
                obtain.sendToTarget();
                return true;
            }
        }
        return false;
    }

    /**
     * 发送在Arguments里的消息.
     *
     * @param position 单选位置.
     * @return 发送是否成功，true代表发送成功，false代表发送失败.
     */
    protected boolean sendMessageSingleChoice(int position) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable parcelable = arguments.getParcelable(ARG_ON_SINGLE_CHOICE_LISTENER);
            if (parcelable != null && parcelable instanceof Message) {
                Message obtain = Message.obtain(((Message) parcelable));
                obtain.arg1 = position;
                obtain.sendToTarget();
                return true;
            }
        }
        return false;
    }

    /**
     * 发送在Arguments里的消息.
     *
     * @param position 单选位置.
     * @return 发送是否成功，true代表发送成功，false代表发送失败.
     */
    protected boolean sendMessageConfirmChoiceState(int position) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable parcelable = arguments.getParcelable(ARG_ON_CONFIRM_CHOICE_STATE_LISTENER);
            if (parcelable != null && parcelable instanceof Message) {
                Message obtain = Message.obtain(((Message) parcelable));
                obtain.arg1 = position;
                obtain.sendToTarget();
                return true;
            }
        }
        return false;
    }

    protected boolean sendMultiChoiceState(List<Integer> positionList) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable parcelable = arguments.getParcelable(ARG_ON_MULTI_CHOICE_LISTENER);
            if (parcelable != null && parcelable instanceof Message) {
                Message obtain = Message.obtain(((Message) parcelable));
                obtain.arg1 = positionList.get(0);
                obtain.sendToTarget();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getArgSerializable(String key, T defaultValue) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Serializable serializable = arguments.getSerializable(key);
            if (serializable != null) {
                return (T) serializable;
            }
        }
        return defaultValue;
    }

    protected CharSequence getArgCharSequence(String key, CharSequence defaultValue) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            CharSequence charSequence = arguments.getCharSequence(key);
            if (!TextUtils.isEmpty(charSequence)) {
                return charSequence;
            }
        }
        return defaultValue;
    }

    protected int getArgInt(String key, int defaultValue) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public Message obtainMessage(int what, Object obj) {
        return messageHandler.obtainMessage(what, obj);
    }

    public void show(AppCompatActivity activity, String tag) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable onDismissListener = arguments.getParcelable(ARG_ON_DISMISS_LISTENER);
            if (onDismissListener != null) {
                Message msg = (Message) onDismissListener;
                Message.obtain(msg).sendToTarget();
            }
        }

        super.onDismiss(dialog);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // 与Activity分离后去掉所有的消息
        messageHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 显示对话框，默认的tag为类的简单名称。
     *
     * @param activity 对应的activity。
     */
    public void show(FragmentActivity activity) {
        if (activity != null) {
            show(activity.getSupportFragmentManager(), getClass().getSimpleName());
        }
    }


    /**
     * 显示对话框，默认的tag为类的简单名称。
     *
     * @param fragment 对应的fragment。
     */
    public void show(Fragment fragment) {
        if (fragment != null) {
            show(fragment.getChildFragmentManager(), getClass().getSimpleName());
        }
    }

    /**
     * 显示对话框，如果执行了onSaveInstanceState后就不显示，直接忽略操作。
     *
     * @param manager fragment管理器。
     * @param tag     标签。
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager != null) {
            boolean stateSaved = manager.isStateSaved();
            if (!stateSaved) {
                // 如果未执行onSaveInstanceState则显示对话框
                try {
                    manager.beginTransaction().remove(this).commit();
                    super.show(manager, tag);

                }catch (Exception e) {
                    //同一实例使用不同的tag会异常,这里捕获一下
                    e.printStackTrace();
                }
            }
        }
    }
}
