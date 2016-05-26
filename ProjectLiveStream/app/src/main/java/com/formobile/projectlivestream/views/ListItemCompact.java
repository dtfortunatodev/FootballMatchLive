package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;

public class ListItemCompact extends BaseItemView implements
        ViewCreationInterface {
	private String title;
	private ViewGroup viewGroup;
	private ViewGroup viewGroupItemsContainer;
	private int childPosition;
	
	public ListItemCompact(String title){
		this.title = title;
		this.childPosition = 0;
	}
	
	@Override
	public ViewGroup createView(Activity context, int childPosition) {
		if(viewGroup == null){
			viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.base_list_item_compact, null);
			
			// Set Title
			((TextView) viewGroup.findViewById(R.id.tvBaseListCompactTitle)).setText(title);
			
			// Get Items Container
			viewGroupItemsContainer = (ViewGroup) viewGroup.findViewById(R.id.layoutBaseListCompactContainer);
		}
		
		return generateView(viewGroup, childPosition);
	}

	public void addItem(Activity context, ViewCreationInterface viewCreationInterface){
		if(viewCreationInterface != null){
			viewGroupItemsContainer.addView(viewCreationInterface.createView(context, childPosition));
			childPosition ++;
		}
	}
	
}
