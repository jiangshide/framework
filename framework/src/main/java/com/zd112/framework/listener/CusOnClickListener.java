package com.zd112.framework.listener;

import android.os.Bundle;
import android.view.View;

/**
 * Interface definition for a callback to be invoked when a view is clicked.
 */
public interface CusOnClickListener extends View.OnClickListener{

    void onClick(View v, Bundle bundle);
}
