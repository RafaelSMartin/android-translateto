package com.danielstone.materialaboutlibrary.items;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.danielstone.materialaboutlibrary.R;
import com.danielstone.materialaboutlibrary.holders.MaterialAboutItemViewHolder;
import com.danielstone.materialaboutlibrary.util.ViewTypeManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.View.GONE;

public class MaterialAboutActionSwitchItem extends MaterialAboutItem {

    public static final int GRAVITY_TOP = 0;
    public static final int GRAVITY_MIDDLE = 1;
    public static final int GRAVITY_BOTTOM = 2;
    private CharSequence text = null;
    private int textRes = 0;
    private CharSequence subText = null;
    private int subTextRes = 0;
    private Drawable icon = null;
    private int iconRes = 0;
    private boolean showIcon = true;
    private int iconGravity = GRAVITY_MIDDLE;
    private boolean checked = false;
    private MaterialAboutItemOnClickAction onClickAction = null;
    private MaterialAboutItemOnClickAction onLongClickAction = null;

    private MaterialAboutActionSwitchItem(Builder builder) {
        this.text = builder.text;
        this.textRes = builder.textRes;

        this.subText = builder.subText;
        this.subTextRes = builder.subTextRes;

        this.icon = builder.icon;
        this.iconRes = builder.iconRes;

        this.showIcon = builder.showIcon;

        this.iconGravity = builder.iconGravity;

        this.checked = builder.checked;

        this.onClickAction = builder.onClickAction;
        this.onLongClickAction = builder.onLongClickAction;
    }

    public MaterialAboutActionSwitchItem(CharSequence text, CharSequence subText, Drawable icon, MaterialAboutItemOnClickAction onClickAction) {
        this.text = text;
        this.subText = subText;
        this.icon = icon;
        this.onClickAction = onClickAction;
    }

    public MaterialAboutActionSwitchItem(CharSequence text, CharSequence subText, Drawable icon) {
        this.text = text;
        this.subText = subText;
        this.icon = icon;
    }

    public MaterialAboutActionSwitchItem(int textRes, int subTextRes, int iconRes, MaterialAboutItemOnClickAction onClickAction) {
        this.textRes = textRes;
        this.subTextRes = subTextRes;
        this.iconRes = iconRes;
        this.onClickAction = onClickAction;
    }

    public MaterialAboutActionSwitchItem(int textRes, int subTextRes, int iconRes) {
        this.textRes = textRes;
        this.subTextRes = subTextRes;
        this.iconRes = iconRes;
    }

    public static MaterialAboutItemViewHolder getViewHolder(View view) {
        return new MaterialAboutActionSwitchItemViewHolder(view);
    }

    public static void setupItem(MaterialAboutActionSwitchItemViewHolder holder, MaterialAboutActionSwitchItem item, Context context) {
        CharSequence text = item.getText();
        int textRes = item.getTextRes();

        holder.text.setVisibility(View.VISIBLE);
        if (text != null) {
            holder.text.setText(text);
        } else if (textRes != 0) {
            holder.text.setText(textRes);
        } else {
            holder.text.setVisibility(GONE);
        }

        CharSequence subText = item.getSubText();
        int subTextRes = item.getSubTextRes();

        holder.subText.setVisibility(View.VISIBLE);
        if (subText != null) {
            holder.subText.setText(subText);
        } else if (subTextRes != 0) {
            holder.subText.setText(subTextRes);
        } else {
            holder.subText.setVisibility(GONE);
        }

        if (item.shouldShowIcon()) {
            holder.icon.setVisibility(View.VISIBLE);
            Drawable drawable = item.getIcon();
            int drawableRes = item.getIconRes();
            if (drawable != null) {
                holder.icon.setImageDrawable(drawable);
            } else if (drawableRes != 0) {
                holder.icon.setImageResource(drawableRes);
            }
        } else {
            holder.icon.setVisibility(GONE);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.icon.getLayoutParams();
        switch (item.getIconGravity()) {
            case MaterialAboutActionSwitchItem.GRAVITY_TOP:
                params.gravity = Gravity.TOP;
                break;
            case MaterialAboutActionSwitchItem.GRAVITY_MIDDLE:
                params.gravity = Gravity.CENTER_VERTICAL;
                break;
            case MaterialAboutActionSwitchItem.GRAVITY_BOTTOM:
                params.gravity = Gravity.BOTTOM;
                break;
        }
        holder.icon.setLayoutParams(params);

        int pL = 0, pT = 0, pR = 0, pB = 0;
        if (Build.VERSION.SDK_INT < 21) {
            pL = holder.view.getPaddingLeft();
            pT = holder.view.getPaddingTop();
            pR = holder.view.getPaddingRight();
            pB = holder.view.getPaddingBottom();
        }

        if (item.getOnClickAction() != null || item.getOnLongClickAction() != null) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            holder.view.setBackgroundResource(outValue.resourceId);
	    }
        holder.setOnClickAction(item.getOnClickAction());
        holder.setOnLongClickAction(item.getOnLongClickAction());

        if (Build.VERSION.SDK_INT < 21) {
            holder.view.setPadding(pL, pT, pR, pB);
        }

        holder.switchView.setChecked(item.checked);
    }

    @Override
    public int getType() {
        return ViewTypeManager.ItemType.ACTION_SWITCH_ITEM;
    }

    public CharSequence getText() {
        return text;
    }

    public MaterialAboutActionSwitchItem setText(CharSequence text) {
        this.textRes = 0;
        this.text = text;
        return this;
    }

    public int getTextRes() {
        return textRes;
    }

    public MaterialAboutActionSwitchItem setTextRes(int textRes) {
        this.text = null;
        this.textRes = textRes;
        return this;
    }

    public CharSequence getSubText() {
        return subText;
    }

    public MaterialAboutActionSwitchItem setSubText(CharSequence subText) {
        this.subTextRes = 0;
        this.subText = subText;
        return this;
    }

    public int getSubTextRes() {
        return subTextRes;
    }

    public MaterialAboutActionSwitchItem setSubTextRes(int subTextRes) {
        this.subText = null;
        this.subTextRes = subTextRes;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public MaterialAboutActionSwitchItem setIcon(Drawable icon) {
        this.iconRes = 0;
        this.icon = icon;
        return this;
    }

    public int getIconRes() {
        return iconRes;
    }

    public MaterialAboutActionSwitchItem setIconRes(int iconRes) {
        this.icon = null;
        this.iconRes = iconRes;
        return this;
    }

    public boolean shouldShowIcon() {
        return showIcon;
    }

    public MaterialAboutActionSwitchItem setShouldShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }

    @IconGravity
    public int getIconGravity() {
        return iconGravity;
    }

    public MaterialAboutActionSwitchItem setIconGravity(int iconGravity) {
        this.iconGravity = iconGravity;
        return this;
    }

    public MaterialAboutItemOnClickAction getOnClickAction() {
        return onClickAction;
    }

    public MaterialAboutActionSwitchItem setOnClickAction(MaterialAboutItemOnClickAction onClickAction) {
        this.onClickAction = onClickAction;
        return this;
    }

    public MaterialAboutItemOnClickAction getOnLongClickAction() {
        return onLongClickAction;
    }

    public MaterialAboutActionSwitchItem setOnLongClickAction(MaterialAboutItemOnClickAction onLongClickAction) {
        this.onLongClickAction = onLongClickAction;
        return this;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRAVITY_TOP, GRAVITY_MIDDLE, GRAVITY_BOTTOM})
    public @interface IconGravity {
    }

    public static class MaterialAboutActionSwitchItemViewHolder extends MaterialAboutItemViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View view;
        public final ImageView icon;
        public final TextView text;
        public final TextView subText;
        public final Switch switchView;
        private MaterialAboutItemOnClickAction onClickAction;
        private MaterialAboutItemOnClickAction onLongClickAction;

        MaterialAboutActionSwitchItemViewHolder(View view) {
            super(view);
            this.view = view;
            icon = (ImageView) view.findViewById(R.id.mal_item_image);
            text = (TextView) view.findViewById(R.id.mal_item_text);
            subText = (TextView) view.findViewById(R.id.mal_action_item_subtext);
            switchView = (Switch)view.findViewById(R.id.switch_view);
        }

        public void setOnClickAction(MaterialAboutItemOnClickAction onClickAction) {
            this.onClickAction = onClickAction;
            view.setOnClickListener(onClickAction != null ? this : null);
            switchView.setClickable(false);
        }

        public void setOnLongClickAction(MaterialAboutItemOnClickAction onLongClickAction) {
            this.onLongClickAction = onLongClickAction;
            view.setOnLongClickListener(onLongClickAction != null ? this : null);
        }

        @Override
        public void onClick(View v) {
            if (onClickAction != null) {
                onClickAction.onClick();
                switchView.setChecked(!switchView.isChecked());
            }
        }

	    @Override
	    public boolean onLongClick(View v) {
		    if (onLongClickAction != null) {
			    onLongClickAction.onClick();
			    return true;
		    }
		    return false;
	    }
    }

    public static class Builder {

        MaterialAboutItemOnClickAction onClickAction = null;
        MaterialAboutItemOnClickAction onLongClickAction = null;
        private CharSequence text = null;
        @StringRes
        private int textRes = 0;
        private CharSequence subText = null;
        @StringRes
        private int subTextRes = 0;
        private Drawable icon = null;
        @DrawableRes
        private int iconRes = 0;
        private boolean showIcon = true;
        @IconGravity
        private int iconGravity = GRAVITY_MIDDLE;

        private boolean checked = false;

        public Builder checked(boolean checked){
            this.checked = checked;
            return this;
        }

        public Builder text(CharSequence text) {
            this.text = text;
            this.textRes = 0;
            return this;
        }

        public Builder text(@StringRes int text) {
            this.textRes = text;
            this.text = null;
            return this;
        }

        public Builder subText(CharSequence subText) {
            this.subText = subText;
            this.subTextRes = 0;
            return this;
        }

        public Builder subText(@StringRes int subTextRes) {
            this.subText = null;
            this.subTextRes = subTextRes;
            return this;
        }

        public Builder subTextHtml(String subTextHtml) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.subText = Html.fromHtml(subTextHtml, Html.FROM_HTML_MODE_LEGACY);
            } else {
                //noinspection deprecation
                this.subText = Html.fromHtml(subTextHtml);
            }
            this.subTextRes = 0;
            return this;
        }

        public Builder icon(Drawable icon) {
            this.icon = icon;
            this.iconRes = 0;
            return this;
        }

        public Builder icon(@DrawableRes int iconRes) {
            this.icon = null;
            this.iconRes = iconRes;
            return this;
        }

        public Builder showIcon(boolean showIcon) {
            this.showIcon = showIcon;
            return this;
        }

        public Builder setIconGravity(@IconGravity int iconGravity) {
            this.iconGravity = iconGravity;
            return this;
        }

        public Builder setOnClickAction(MaterialAboutItemOnClickAction onClickAction) {
            this.onClickAction = onClickAction;
            return this;
        }

        public Builder setOnLongClickAction(MaterialAboutItemOnClickAction onLongClickAction) {
            this.onLongClickAction = onLongClickAction;
            return this;
        }

        public MaterialAboutActionSwitchItem build() {
            return new MaterialAboutActionSwitchItem(this);
        }
    }
}
