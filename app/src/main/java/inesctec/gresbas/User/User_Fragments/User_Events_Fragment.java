
package inesctec.gresbas.User.User_Fragments;

        import android.content.Intent;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebView;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.TextView;

        import com.twitter.sdk.android.core.DefaultLogger;
        import com.twitter.sdk.android.core.Twitter;
        import com.twitter.sdk.android.core.TwitterAuthConfig;
        import com.twitter.sdk.android.core.TwitterConfig;
        import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

        import inesctec.gresbas.R;
        import inesctec.gresbas.Utils.Utils;


public class User_Events_Fragment extends Fragment {

    private View view;
    private BottomNavigationView bottomNavigationView;

    public User_Events_Fragment() {
        // Required empty public constructor
    }



    public static final String TAG = "TimeLineActivity";

    private static final String baseURl = "http://twitter.com";

    private static final String widgetInfo = "<a class=\"twitter-timeline\" href=\"https://twitter.com/INESCTEC?ref_src=twsrc%5Etfw\">Tweets by INESCTEC</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_events, container, false);

        load_background_color();

        WebView webView = (WebView) view.findViewById(R.id.timeline_webview);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(baseURl, widgetInfo, "text/html", "UTF-8", null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    private void load_background_color() {
        WebView webView = (WebView) view.findViewById(R.id.timeline_webview);
        //webView.setBackgroundColor(getResources().getColor(R.color.twitter_dark));
        webView.setBackgroundColor(0); // transparent
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
