package gavengers.wag;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private com.naver.maps.map.MapFragment mapFragment;
    private MapView mapView;
    private static NaverMap naverMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setMap();
        View v = inflater.inflate(R.layout.map_layout, container, false);
        // 3.x API version
        NaverMapSdk.getInstance(this.getContext()).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("eh5bexu1t1"));

        mapView = v.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return inflater.inflate(R.layout.map_layout, container, false);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
    }

    private void setMap(){
        FragmentManager fragmentManager = getChildFragmentManager();
        mapFragment = (com.naver.maps.map.MapFragment) fragmentManager.findFragmentById(R.id.map_view);

        if(mapFragment == null) {
            NaverMapOptions options = new NaverMapOptions()
                    .camera(new CameraPosition(new LatLng(37.45199894842855, 127.13179114165393), 14)) // 기본 위치
                    .logoGravity(Gravity.START | Gravity.TOP)
                    .logoMargin(8, 8, 8, 8) // 로고 여백
                    .locationButtonEnabled(false) // 현재 위치 비활성화
                    .compassEnabled(false) // 나침반
                    .zoomControlEnabled(true); // 줌

            mapFragment = com.naver.maps.map.MapFragment.newInstance(options);
            fragmentManager.beginTransaction().add(R.id.map_view, mapFragment).commit();

            // 비동기 코드이기 때문에 코드 실행에 유의
            mapFragment.getMapAsync(naverMap -> {
                naverMap.setExtent(new LatLngBounds(new LatLng(37.44792028734633, 127.12628356183701), new LatLng(37.4570968690434, 127.13723061921826)));
                naverMap.setMinZoom(14.0);
                naverMap.setMaxZoom(0.0);
                drawMarker(naverMap);
                onMapReady(naverMap);
                drawPath(naverMap);
            });
        }
    }

    public void drawMarker(NaverMap naverMap){
        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.45044136062332, 127.12986847331536));

        marker.setWidth(40);
        marker.setHeight(60);
        Log.d("marker",marker.getPosition().toString());
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker));
        marker.setMap(naverMap);
    }

    /**
     * 지도에 Path를 표시한다
     * @author Taehyun Park
     * @param naverMap NaverMap Object
     * 파라미터 추가예정
     */
    public void drawPath(NaverMap naverMap){
        String [] str_coordinate = getResources().getStringArray(R.array.gachon_globalcampus_coordinate); // 학교 좌표가 저장된 좌표 배열 불러오기
        ArrayList<LatLng> coordinates = new ArrayList<>();  // Location 좌표 저장할 ArrayList

        /*
        for (int i : pathArr){
            String [] arr = str_coordinate[i].split(",");
            coordinates.add(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));
        }*/

        // TEST CODE
        String [] arr = str_coordinate[0].split(",");
        coordinates.add(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));

        arr = str_coordinate[1].split(",");
        coordinates.add(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));

        arr = str_coordinate[2].split(",");
        coordinates.add(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));

        PathOverlay path = new PathOverlay(); // path 객체 생성
        path.setCoords(coordinates); // Location 좌표 setting
        path.setColor(Color.GREEN); // Path 색상

        path.setMap(naverMap);      // Map에 표시
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
