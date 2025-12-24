package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.SpinnerAdapter;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ThemedSpinnerAdapter.class */
public interface ThemedSpinnerAdapter extends SpinnerAdapter {

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ThemedSpinnerAdapter$Helper.class */
    public static final class Helper {
        private final Context mContext;
        private LayoutInflater mDropDownInflater;
        private final LayoutInflater mInflater;

        public Helper(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        public LayoutInflater getDropDownViewInflater() {
            LayoutInflater layoutInflater = this.mDropDownInflater;
            if (layoutInflater == null) {
                layoutInflater = this.mInflater;
            }
            return layoutInflater;
        }

        public Resources.Theme getDropDownViewTheme() {
            LayoutInflater layoutInflater = this.mDropDownInflater;
            return layoutInflater == null ? null : layoutInflater.getContext().getTheme();
        }

        public void setDropDownViewTheme(Resources.Theme theme) {
            if (theme == null) {
                this.mDropDownInflater = null;
            } else if (theme == this.mContext.getTheme()) {
                this.mDropDownInflater = this.mInflater;
            } else {
                this.mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(this.mContext, theme));
            }
        }
    }

    Resources.Theme getDropDownViewTheme();

    void setDropDownViewTheme(Resources.Theme theme);
}
