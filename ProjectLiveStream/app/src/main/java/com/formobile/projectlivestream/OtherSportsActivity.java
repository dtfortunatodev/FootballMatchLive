package com.formobile.projectlivestream;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.formobile.projectlivestream.configs.ConnectionHelper;
import com.formobile.projectlivestream.configs.OtherSportJSONEntity;
import com.formobile.projectlivestream.entities.OtherSportEntity;
import com.formobile.projectlivestream.entities.PopupGenericEntity;
import com.formobile.projectlivestream.interfaces.PopupGenericInterface;
import com.formobile.projectlivestream.utils.PopupController;
import com.formobile.projectlivestream.utils.ProgressController;
import com.formobile.projectlivestream.views.ListOtherSportItemView;

import java.io.IOException;
import java.util.List;

public class OtherSportsActivity extends BaseSoccerLiveActivity {

	private int childCounts = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadOtherSports();
		findViewById(R.id.ivHeaderOtherSportsToggle).setVisibility(View.GONE);
		findViewById(R.id.ivHeaderRefreshBtn).setVisibility(View.GONE);
		
		// Set Title
		((TextView) findViewById(R.id.tvHeaderTitle)).setText("Other Sports");
		
	}

    private void loadOtherSports(){
        new AsyncTask<Void, Void, List<OtherSportJSONEntity>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                ProgressController.startDialog(OtherSportsActivity.this);
            }

            @Override
            protected List<OtherSportJSONEntity> doInBackground(Void... voids) {

                try {
                    return ConnectionHelper.getListOtherSport(getBaseContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<OtherSportJSONEntity> otherSportJSONEntities) {

                if(otherSportJSONEntities != null){

                    for(OtherSportJSONEntity otherSportJSONEntity : otherSportJSONEntities){
                        addSportItem(otherSportJSONEntity);
                    }

                } else{
                    // Show Error Popup
                    PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_error_connection_title, R.string.popup_error_connection_description, R.string.popup_error_connection_btn_confirmation, R.string.popup_error_connection_btn_cancel, new PopupGenericInterface() {

                        @Override
                        public void onConfirmationClicked(Dialog dialog) {
                            dialog.dismiss();
                            loadOtherSports();
                        }

                        @Override
                        public void onCancelClicked(Dialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    PopupController.showGenericPopup(OtherSportsActivity.this, popupGenericEntity);
                }

                ProgressController.stopDialog();

                super.onPostExecute(otherSportJSONEntities);
            }
        }.execute();
    }


//	private void generateSportsItems(){
//		OtherSportEntity otherSportEntity;
//
//		// Soccer Item
//		otherSportEntity = new OtherSportEntity("Football (Soccer)", "org.formobsports.livefootball", R.drawable.ic_soccer);
//		addSportItem(otherSportEntity);
//
//		// Basketball Item
//		otherSportEntity = new OtherSportEntity("Basketball (NBA)", "org.formobsports.livebasket", R.drawable.ic_basketball);
//		addSportItem(otherSportEntity);
//
//		// Tennis Item
//		otherSportEntity = new OtherSportEntity("Tennis", "org.formobsports.livetennis", R.drawable.ic_tennis);
//		addSportItem(otherSportEntity);
//
//		// American Football Item
//		otherSportEntity = new OtherSportEntity("American Football", "org.formobsports.livenfl", R.drawable.ic_nfl);
//		addSportItem(otherSportEntity);
//
//		// Ice Hockey Item
//		otherSportEntity = new OtherSportEntity("Ice Hockey", "org.formobsports.liveicehockey", R.drawable.ic_icehockey);
//		addSportItem(otherSportEntity);
//
//		// Baseball Item
//		otherSportEntity = new OtherSportEntity("Baseball", "org.formobsports.livebaseball", R.drawable.ic_baseball);
//		addSportItem(otherSportEntity);
//
//		// Racing Item
//		otherSportEntity = new OtherSportEntity("Racing", "org.formobsports.liveracing", R.drawable.ic_racing);
//		addSportItem(otherSportEntity);
//
//		// Boxing Item
//		otherSportEntity = new OtherSportEntity("Boxing", "org.formobsports.liveboxing", R.drawable.ic_boxing);
//		addSportItem(otherSportEntity);
//
//		// Cricket Item
//		otherSportEntity = new OtherSportEntity("Cricket", "org.formobsports.livecricket", R.drawable.ic_cricket);
//		addSportItem(otherSportEntity);
//
//		// Cycling Item
//		otherSportEntity = new OtherSportEntity("Cycling", "org.formobsports.livecycling", R.drawable.ic_cycling);
//		addSportItem(otherSportEntity);
//
//		// Field Hockey Item
//		otherSportEntity = new OtherSportEntity("Field Hockey", "org.formobsports.livefieldhockey", R.drawable.ic_fieldhockey);
//		addSportItem(otherSportEntity);
//
//		// Futsal Item
//		otherSportEntity = new OtherSportEntity("Futsal", "org.formobsports.livefutsal", R.drawable.ic_futsal);
//		addSportItem(otherSportEntity);
//
//		// Golf Item
//		otherSportEntity = new OtherSportEntity("Golf", "org.formobsports.livegolf", R.drawable.ic_golf);
//		addSportItem(otherSportEntity);
//
//		// Handball Item
//		otherSportEntity = new OtherSportEntity("Handball", "org.formobsports.livehandball", R.drawable.ic_handball);
//		addSportItem(otherSportEntity);
//
//		// Rugby Item
//		otherSportEntity = new OtherSportEntity("Rugby", "org.formobsports.liverugby", R.drawable.ic_rugby);
//		addSportItem(otherSportEntity);
//
//		// Volleyball Item
//		otherSportEntity = new OtherSportEntity("Volleyball", "org.formobsports.livevolleyball", R.drawable.ic_volleyball);
//		addSportItem(otherSportEntity);
//
//	}
	
	private void addSportItem(OtherSportJSONEntity otherSportEntity){
		if(otherSportEntity != null && !otherSportEntity.getNamepackage().equalsIgnoreCase(getApplicationContext().getPackageName())){
			addViewItem(new ListOtherSportItemView(otherSportEntity).createView(this, childCounts));
			childCounts ++;
		}
	}
	
}
