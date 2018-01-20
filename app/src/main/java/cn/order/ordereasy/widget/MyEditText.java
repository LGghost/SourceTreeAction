package cn.order.ordereasy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

public class MyEditText extends android.support.v7.widget.AppCompatEditText {

    public MyEditText(Context context) {
        super(context);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private OnFinishComposingListener mFinishComposingListener;

    public void setOnFinishComposingListener(OnFinishComposingListener listener) {
        this.mFinishComposingListener = listener;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), false);
    }

    public class MyInputConnection extends InputConnectionWrapper {
        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean finishComposingText() {
            boolean finishComposing = super.finishComposingText();
            if (mFinishComposingListener != null) {
                mFinishComposingListener.finishComposing();
            }
            return finishComposing;
        }
    }

    public interface OnFinishComposingListener {
        public void finishComposing();
    }
}