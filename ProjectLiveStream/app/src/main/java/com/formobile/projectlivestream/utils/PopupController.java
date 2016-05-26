package com.formobile.projectlivestream.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.entities.PopupGenericEntity;

public class PopupController {

	public static void showGenericPopup(Activity activity, final PopupGenericEntity popupGenericEntity){
		final Dialog dialog = new Dialog(activity,
                R.style.Theme_Dialog_SoccerLiveStreaming);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_generic);

        // Set Title
        if(popupGenericEntity.getTitle() != null){
            ((TextView) dialog.findViewById(R.id.tvPopupTitle)).setText(popupGenericEntity.getTitle());
        } else{
            ((TextView) dialog.findViewById(R.id.tvPopupTitle)).setText(popupGenericEntity.getTitleRes());
        }

        
        // Set Description
        if(popupGenericEntity.getDescription() != null){
            ((TextView) dialog.findViewById(R.id.tvPopupDescription)).setText(popupGenericEntity.getDescription());
        } else{
            ((TextView) dialog.findViewById(R.id.tvPopupDescription)).setText(popupGenericEntity.getDescriptionRes());
        }

        
        // Set Confirmation
        if(popupGenericEntity.getBtnConfirmation() != null){
            ((Button) dialog.findViewById(R.id.btnPopupConfirmation)).setText(popupGenericEntity.getBtnConfirmation());
        } else{
            ((Button) dialog.findViewById(R.id.btnPopupConfirmation)).setText(popupGenericEntity.getBtnConfirmationRes());
        }

        ((Button) dialog.findViewById(R.id.btnPopupConfirmation)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(popupGenericEntity != null && popupGenericEntity.getPopupGenericInterface() != null){
					popupGenericEntity.getPopupGenericInterface().onConfirmationClicked(dialog);
				} else{
					dialog.dismiss();
				}
			}
		});
        
        // Set Cancel
        if(popupGenericEntity.getBtnCancel() != null){
            ((Button) dialog.findViewById(R.id.btnPopupCancel)).setText(popupGenericEntity.getBtnCancel());
        } else if(popupGenericEntity.getBtnCancelRes() > 0){
            ((Button) dialog.findViewById(R.id.btnPopupCancel)).setText(popupGenericEntity.getBtnCancelRes());
        } else{
            ((Button) dialog.findViewById(R.id.btnPopupCancel)).setVisibility(View.GONE);
        }

        ((Button) dialog.findViewById(R.id.btnPopupCancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(popupGenericEntity != null && popupGenericEntity.getPopupGenericInterface() != null){
					popupGenericEntity.getPopupGenericInterface().onCancelClicked(dialog);
				} else{
					dialog.dismiss();
				}
			}
		});
        
        dialog.show();
	}
	
}
