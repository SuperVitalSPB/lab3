package ru.supervital.lab3;

import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DSF_Map extends DummySectionFragment implements OnMyLocationListener, OnBalloonListener {

	TextView dummyTextView;
	Button btn;
	ListView lvMain;
	View mFormView;
	View mStatusView;
	Boolean aResult = false;

    MapController mMapController;
    OverlayManager mOverlayManager;
    MapView mapView;
    Overlay mOverlay;
    
    GeoPoint geoMyWork;
    
    BalloonItem balloonMyWork;        
    BalloonItem balloonMyHouse;         
    BalloonItem balloonMyLove; 
	
	
	public DSF_Map() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dummy_map, container, false);
		
		lvMain = (ListView) rootView.findViewById(R.id.lvMain);
		
		btn = (Button) rootView.findViewById(R.id.btnShowAll);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//mMapController.getOverlayManager().getMyLocation().findMe();
				setZoomSpan(mOverlay.getOverlayItems().size());
			}
		});
		
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.showZoomButtons(true);
        mapView.showFindMeButton(true);
        mapView.showJamsButton(false);

        
        
        mMapController = mapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(true);
        
        // A simple implementation of map objects
        showObject();
		
		return rootView;
	}
	
	public void ShowStartPos(){
		setZoomSpan(mOverlay.getOverlayItems().size());
	}
	
    public void showObject(){
        // Load required resources
        Resources res = getResources();
        
        // Create a layer of objects for the map
        mOverlay = new Overlay(mMapController);
        
        geoMyWork = new GeoPoint(59.949077, 30.358358);
        // Create an object for the layer
        final OverlayItem oiMyWork = new OverlayItem(geoMyWork, res.getDrawable(R.drawable.ps));
        // Create a balloon model for the object
        balloonMyWork = new BalloonItem(this.getActivity(), oiMyWork.getGeoPoint());
        balloonMyWork.setText(getString(R.string.MyWork));
        balloonMyWork.setOnBalloonListener(this); 
        // Add the balloon model to the object
        oiMyWork.setBalloonItem(balloonMyWork);
        // Add the object to the layer
        mOverlay.addOverlayItem(oiMyWork);

        final OverlayItem MyHouse = new OverlayItem(new GeoPoint(59.942998, 30.491318), res.getDrawable(R.drawable.shop));
        balloonMyHouse = new BalloonItem(this.getActivity(), MyHouse.getGeoPoint());
        balloonMyHouse.setText(getString(R.string.MyHouse));
        balloonMyHouse.setOnBalloonListener(this);
        MyHouse.setBalloonItem(balloonMyHouse);
        mOverlay.addOverlayItem(MyHouse);

        final OverlayItem MyLove = new OverlayItem(new GeoPoint(59.952768, 30.301531), res.getDrawable(R.drawable.heart));
        balloonMyLove = new BalloonItem(this.getActivity(), MyLove.getGeoPoint());
        balloonMyLove.setText(getString(R.string.MyLove));
        balloonMyLove.setOnBalloonListener(this);
        MyLove.setBalloonItem(balloonMyLove);
        mOverlay.addOverlayItem(MyLove);

        // Add the layer to the map
        mOverlayManager.addOverlay(mOverlay);
        
        
    }
    
    @Override
    public void onMyLocationChange(MyLocationItem myLocationItem) {
    	// myLocationItem.getDrawable().setVisible(true, true);
    	GeoPoint geo = myLocationItem.getGeoPoint();
    	
    	
    	//lvMain.
/*    	
        final TextView textView = new TextView(this);
        textView.setText("Type " + myLocationItem.getType()+" GeoPoint ["+myLocationItem.getGeoPoint().getLat()+","+myLocationItem.getGeoPoint().getLon()+"]");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mView.addView(textView);
            }
        });
*/    
    }
    
    private void setZoomSpan(int count){
        List<OverlayItem> list = mOverlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < count; i++){
            GeoPoint geoPoint = list.get(i).getGeoPoint();
            
            double lat = geoPoint.getLat();
            double lon = geoPoint.getLon();

            maxLat = Math.max(lat, maxLat);
            minLat = Math.min(lat, minLat);
            maxLon = Math.max(lon, maxLon);
            minLon = Math.min(lon, minLon);
        }
        mMapController.setZoomToSpan(maxLat - minLat, maxLon - minLon);
        mMapController.setPositionAnimationTo(new GeoPoint((maxLat + minLat)/2, (maxLon + minLon)/2));
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view)  {
        OverlayItem item = balloonItem.getOverlayItem();
        
        if (balloonItem == balloonMyWork) {
        	Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.billing.ru"));
        	startActivity(browseIntent);
        } else if (balloonItem == balloonMyHouse) {
        	
            Intent intent = new Intent().setClass(this.getActivity(), ImageActivity.class);
            startActivity(intent);
        	
        } if (balloonItem == balloonMyLove) {
        	
        } else return;

        
        
//        Toast.makeText(this.getActivity(), balloonItem.getText(), Toast.LENGTH_LONG).show();
        
/*
        Intent intent = new Intent().setClass(this, InfoActivity.class);
        if (balloonItem.getText() != null){
            intent.putExtra(InfoActivity.ID_INFO, balloonItem.getText());
        }else{
        	intent.putExtra(InfoActivity.ID_INFO, "яндекс");
        }
        startActivity(intent);
*/
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {
        // TODO Auto-generated method stub

    }


}
