package com.formobile.projectlivestream.interfaces;

import android.app.Activity;
import android.view.ViewGroup;

public interface ViewCreationInterface {

	ViewGroup createView(Activity context, int childPosition);
}
