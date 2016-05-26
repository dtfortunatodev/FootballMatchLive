package com.formobile.projectlivestream.entities;

import com.formobile.projectlivestream.interfaces.PopupGenericInterface;

public class PopupGenericEntity {

	private int titleRes;
	private int descriptionRes;
	private int btnConfirmationRes;
	private int btnCancelRes;

    private String title;
    private String description;
    private String btnConfirmation;
    private String btnCancel;

	private PopupGenericInterface popupGenericInterface;
	
	public PopupGenericEntity(int titleRes, int descriptionRes, int btnConfirmationRes, int btnCancelRes, PopupGenericInterface popupGenericInterface){
		this.titleRes = titleRes;
		this.descriptionRes = descriptionRes;
		this.btnConfirmationRes = btnConfirmationRes;
		this.btnCancelRes = btnCancelRes;
		this.popupGenericInterface = popupGenericInterface;
	}

    public PopupGenericEntity(String title, String description, String btnConfirmation, String btnCancel, PopupGenericInterface popupGenericInterface){
        this.title = title;
        this.description = description;
        this.btnConfirmation = btnConfirmation;
        this.btnCancel = btnCancel;
        this.btnCancelRes = -1;
        this.popupGenericInterface = popupGenericInterface;
    }

	public int getTitleRes() {
		return titleRes;
	}

	public void setTitleRes(int titleRes) {
		this.titleRes = titleRes;
	}

	public int getDescriptionRes() {
		return descriptionRes;
	}

	public void setDescriptionRes(int descriptionRes) {
		this.descriptionRes = descriptionRes;
	}

	public int getBtnConfirmationRes() {
		return btnConfirmationRes;
	}

	public void setBtnConfirmationRes(int btnConfirmationRes) {
		this.btnConfirmationRes = btnConfirmationRes;
	}

	public int getBtnCancelRes() {
		return btnCancelRes;
	}

	public void setBtnCancelRes(int btnCancelRes) {
		this.btnCancelRes = btnCancelRes;
	}

	public PopupGenericInterface getPopupGenericInterface() {
		return popupGenericInterface;
	}

	public void setPopupGenericInterface(PopupGenericInterface popupGenericInterface) {
		this.popupGenericInterface = popupGenericInterface;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBtnConfirmation() {
        return btnConfirmation;
    }

    public void setBtnConfirmation(String btnConfirmation) {
        this.btnConfirmation = btnConfirmation;
    }

    public String getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(String btnCancel) {
        this.btnCancel = btnCancel;
    }
}
