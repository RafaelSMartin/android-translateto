package com.ticktalk.translateto.history;

/**
 * Created by Indogroup02 on 23/01/2018.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
