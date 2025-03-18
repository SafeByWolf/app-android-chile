package com.safebywolf.safebywolf.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MyClusterManagerRender extends DefaultClusterRenderer<ClausterMarker> {
    private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private final int markerWidth;
    private final int marckerHeight;

    public MyClusterManagerRender(Context context, GoogleMap map, ClusterManager<ClausterMarker> clusterManager) {
        super(context, map, clusterManager);
        iconGenerator=new IconGenerator(context.getApplicationContext());
        imageView=new ImageView(context.getApplicationContext());
        markerWidth=(int) 10;
        marckerHeight=(int) 10;
        imageView.setPadding(4,4,4,4);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClausterMarker item, MarkerOptions markerOptions) {
        imageView.setImageBitmap(item.getIconPicture());
        Bitmap icon=iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClausterMarker> cluster) {
        return false;
    }
}
