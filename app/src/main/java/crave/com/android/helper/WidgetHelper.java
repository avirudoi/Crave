package crave.com.android.helper;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class WidgetHelper {

	/*
	 * Ref:
	 * http://stackoverflow.com/questions/4096851/remove-underline-from-links
	 * -in-textview-android
	 */
	public static void makeTextViewLink(Context context, TextView textView, int resId) {
		makeTextViewLink(context, textView, resId, false);
	}

	public static void makeTextViewLink(Context context, TextView textView, int resId, boolean removeUnderline) {
		String str = context.getResources().getString(resId);
		makeTextViewLink(textView, str, removeUnderline);
	}

	public static void makeTextViewLink(TextView textView, String text) {
		makeTextViewLink(textView, text, false);
	}

	public static void makeTextViewLink(TextView textView, String text, boolean removeUnderline) {
		if (textView != null) {
			if (TextUtils.isEmpty(text) == false) {
				textView.setText(getHtml(text, removeUnderline));
				textView.setMovementMethod(LinkMovementMethod.getInstance());
			}
		}
	}
	
	public static void setSafeText(TextView tv, String text) {
		if(tv != null && text != null) {
			tv.setText(text);
		}
	}

	public static void setSafeText(Activity act, TextView tv, int stringId) {
		if(act != null) {
			String text = act.getString(stringId);
			if(tv != null && text != null) {
				tv.setText(text);
			}
		}
	}

	public static void setSafeText(Activity act, int resId, String text) {
		if(act != null) {
			TextView tv = (TextView)act.findViewById(resId);
			if(tv != null && text != null) {
				tv.setText(text);
			}
		}
	}

	/**
	 * A utility method that will set text for a Text View if the input text is non-empty
	 * else  will hide the Text View.
	 * @param view
	 * @param resID
	 * @param text
	 */
	public static void setVisibleText(View view, int resID, String text) {
		TextView tv = (TextView)view.findViewById(resID);
		if(tv != null) {
			tv.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
			if(TextUtils.isEmpty(text) == false) {
				tv.setText(text);
			}
		}
	}
	
    /**
     * Returns the integer value defined in R.java file for the input String.
     * <p> This method must be used sparingly, as it is not very efficient.
     * @param context
     * @param resId : String defined in one of the resource files such as Strings.xml resource name e.g: action_item_help
     * @return
     */
    public static int getResourceIdentifierFromStr(Context ctxt, String resId){
        return TextUtils.isEmpty(resId) ? 0 : ctxt.getResources().getIdentifier(resId, "drawable", ctxt.getPackageName());
    }    
	
	public static void setSafeText(Activity act, int resId, int stringId) {
		if(act != null) {
			TextView tv = (TextView)act.findViewById(resId);
			if(stringId > 0) {
				String text = act.getString(stringId);
				if(tv != null && text != null) {
					tv.setText(text);
				}
			}
		}
	}
	
	private static Spannable getHtml(String html, boolean removeUnderline) {
		Spannable s = (Spannable) Html.fromHtml(html);
		if (removeUnderline) {
			for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
				s.setSpan(new UnderlineSpan() {
					public void updateDrawState(TextPaint tp) {
						tp.setUnderlineText(false);
					}
				}, s.getSpanStart(u), s.getSpanEnd(u), 0);
			}
		}
		return s;
	}
}