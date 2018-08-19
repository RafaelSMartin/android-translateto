package com.ticktalk.translateto.history;

/**
 * Created by Rafael S. Martin
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
