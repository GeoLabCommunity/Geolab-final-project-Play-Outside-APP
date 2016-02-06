package geolab.playoutside.fragment_categories;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import geolab.playoutside.Add_Event_Activity;
import geolab.playoutside.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryIcon extends Fragment {

    private LinearLayout buttonContainer;
    private int buttonCount;
    private int[] iconArray;
    private String [] tagArray;
    private static String subCategoryTag = null;

    public SubCategoryIcon() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ball_category, container, false);

          buttonContainer = (LinearLayout) view.findViewById(R.id.button_container);
          buttonCount=getArguments().getInt("buttonCounter");
          iconArray = getArguments().getIntArray("iconarray");
          tagArray = getArguments().getStringArray("tagarray");

        for (int i = 0; i < buttonCount; i++) {
            final ImageButton button = new ImageButton(getActivity().getApplicationContext());
            button.setLayoutParams(new ViewGroup.LayoutParams(150,150));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30, 0, 30, 0);
            button.setLayoutParams(lp);
            //System.out.println(iconArray[i]);

            button.setBackgroundResource(iconArray[i]);

            button.setTag(tagArray[i]);

            buttonContainer.addView(button);

            final int finalI = i;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAllButtons(buttonContainer);
                    button.getBackground().clearColorFilter();

                    subCategoryTag = (String) button.getTag();
                    ((Add_Event_Activity)getActivity()).setSubCategoryData(subCategoryTag);

                }
            });
        }

        hideAllButtons(buttonContainer);


        return view;

    }
    public static SubCategoryIcon newInstance(int buttonCounter, int[] iconArray, String[] tagArray) {

        SubCategoryIcon f = new SubCategoryIcon();
        Bundle b = new Bundle();
        b.putInt("buttonCounter", buttonCounter);
        b.putIntArray("iconarray", iconArray);
        b.putStringArray("tagarray", tagArray);



        f.setArguments(b);

        return f;
    }


    private void hideAllButtons(LinearLayout container){
        for (int i = 0; i < container.getChildCount(); i++) {
             ImageButton button= (ImageButton) container.getChildAt(i);
             button.getBackground().setColorFilter(getResources().getColor(R.color.grey_transparent), PorterDuff.Mode.SRC_ATOP);
             container.removeViewAt(i);
             container.addView(button,i);
        }
    }


}
