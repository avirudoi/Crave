package crave.com.android.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import crave.com.android.R;

/**
 * A custom text view class that supports custom fonts through a custom styleable attribute. 
 * Ref: http://www.techrepublic.com/article/pro-tip-extend-androids-textview-to-use-custom-fonts/
 * @author Avi Rudoi
 *
 */
public class CustomTextView extends TextView {

	public CustomTextView(Context context) {
		super(context);
		init(null);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public void setFont(String fontFile) {
		try {
			Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontFile);
			setTypeface(myTypeface);
		}
		catch(Exception e) {

		}
	}
	
	private void init(AttributeSet attrs) {
		if (attrs!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
			 String fontName = a.getString(R.styleable.CustomTextView_textViewFontName);
			 if (TextUtils.isEmpty(fontName)) {

			 }
			 else {
				 setFont(fontName);
			 }
			 a.recycle();
		}
	}
}
