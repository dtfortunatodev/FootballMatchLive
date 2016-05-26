package com.formobile.projectlivestream.views;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.formobile.projectlivestream.utils.AnimationConsts;

public class BaseItemView {

	public ViewGroup generateView(ViewGroup viewGroup, int childPosition){
		if(viewGroup != null){
			AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(AnimationConsts.ANIMATION_FADE_IN_DURATION);
			anim.setStartOffset(AnimationConsts.ANIMATION_FADE_IN_DELAY * childPosition);
			viewGroup.startAnimation(anim);
		}
		
		return viewGroup;
	}

}
