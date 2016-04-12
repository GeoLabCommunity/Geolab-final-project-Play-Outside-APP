package geolab.playoutside.fragment_categories;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private String category;

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
          category = getArguments().getString("category");

        for (int i = 0; i < buttonCount; i++) {
            final ImageButton button = new ImageButton(getActivity().getApplicationContext());
            final float scale = getResources().getDisplayMetrics().density;
            int dpWidthInPx  = (int) (150 * scale);
            int margin  = (int) (6 * scale);

            button.setLayoutParams(new ViewGroup.LayoutParams(dpWidthInPx,dpWidthInPx));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(margin, 0, margin, 0);
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
                    Toast toast = Toast.makeText(getContext(),subCategoryTag, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    ((Add_Event_Activity)getActivity()).setSubCategoryData(subCategoryTag,category);

                }
            });
        }

        hideAllButtons(buttonContainer);


        return view;

    }
    public static SubCategoryIcon newInstance(int buttonCounter, int[] iconArray, String[] tagArray, String category) {

        SubCategoryIcon f = new SubCategoryIcon();
        Bundle b = new Bundle();
        b.putInt("buttonCounter", buttonCounter);
        b.putIntArray("iconarray", iconArray);
        b.putStringArray("tagarray", tagArray);
        b.putString("category", category);



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
