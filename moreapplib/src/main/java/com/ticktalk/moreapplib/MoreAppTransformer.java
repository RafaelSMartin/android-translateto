package com.ticktalk.moreapplib;

import android.support.annotation.FloatRange;
import android.view.View;

import com.yarolegovich.discretescrollview.transform.DiscreteScrollItemTransformer;
import com.yarolegovich.discretescrollview.transform.Pivot;

/**
 * Created by indogroup on 9/6/17.
 */

public class MoreAppTransformer implements DiscreteScrollItemTransformer {

    private Pivot pivotX;
    private Pivot pivotY;
    private float minScale;
    private float maxMinDiff;
    private float minAlpha;
    private float maxMinAlpha;

    public MoreAppTransformer() {
        pivotX = Pivot.X.CENTER.create();
        pivotY = Pivot.Y.CENTER.create();
        minScale = 0.8f;
        maxMinDiff = 0.2f;
        minAlpha= 0.35f;
        maxMinAlpha = 0.65f;
    }

    @Override
    public void transformItem(View item, float position) {
        pivotX.setOn(item);
        pivotY.setOn(item);
        float closenessToCenter = 1f - Math.abs(position);
        float scale = minScale + maxMinDiff * closenessToCenter;
        item.setScaleX(scale);
        item.setScaleY(scale);

        float alpha = minAlpha + maxMinAlpha * closenessToCenter;
        item.setAlpha(alpha);
    }

    public static class Builder {

        private MoreAppTransformer transformer;
        private float maxScale;

        public Builder() {
            transformer = new MoreAppTransformer();
            maxScale = 1f;
        }

        public MoreAppTransformer.Builder setMinScale(@FloatRange(from = 0.01) float scale) {
            transformer.minScale = scale;
            return this;
        }

        public MoreAppTransformer.Builder setMaxScale(@FloatRange(from = 0.01) float scale) {
            maxScale = scale;
            return this;
        }

        public MoreAppTransformer.Builder setPivotX(Pivot.X pivotX) {
            return setPivotX(pivotX.create());
        }

        public MoreAppTransformer.Builder setPivotX(Pivot pivot) {
            assertAxis(pivot, Pivot.AXIS_X);
            transformer.pivotX = pivot;
            return this;
        }

        public MoreAppTransformer.Builder setPivotY(Pivot.Y pivotY) {
            return setPivotY(pivotY.create());
        }

        public MoreAppTransformer.Builder setPivotY(Pivot pivot) {
            assertAxis(pivot, Pivot.AXIS_Y);
            transformer.pivotY = pivot;
            return this;
        }

        public MoreAppTransformer build() {
            transformer.maxMinDiff = maxScale - transformer.minScale;
            return transformer;
        }

        private void assertAxis(Pivot pivot, @Pivot.Axis int axis) {
            if (pivot.getAxis() != axis) {
                throw new IllegalArgumentException("You passed a Pivot for wrong axis.");
            }
        }
    }
}
