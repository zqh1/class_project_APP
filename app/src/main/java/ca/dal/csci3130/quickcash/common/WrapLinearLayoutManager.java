package ca.dal.csci3130.quickcash.common;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapLinearLayoutManager extends LinearLayoutManager {

    /**
     * Manager for recyclers, used in every recycler of the app
     *
     * @param context:       Current screen initializing the linear wrap manager
     * @param orientation:   Orientation the layout will use
     * @param reverseLayout: Boolean is the order is reversed (upside down)
     */
    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * Method that link each recycler with their state
     *
     * @param recycler: Recycler containing the child
     * @param state:    State used by current child
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("WrapLinearLayoutManager", "Index Out Of Bound Exception in RecyclerView");
        }
    }
}
