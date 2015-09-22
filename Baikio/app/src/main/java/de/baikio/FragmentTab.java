package de.baikio;

import android.os.Bundle;
        import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.TextView;

import java.io.Console;

public class FragmentTab extends Fragment {

    private String tag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(this.getTag() + " Content");
        tag = this.getTag();

        if (tag == "tab1")
        {
            Log.d("INFO", "hahah");

        }
        return v;
    }
}