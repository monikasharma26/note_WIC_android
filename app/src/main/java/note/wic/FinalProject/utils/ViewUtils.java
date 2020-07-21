package note.wic.FinalProject.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import note.wic.FinalProject.App;


public class ViewUtils {

	public static void tintMenu(Menu menu, @IdRes int menuId, @ColorRes int colorRes){
		Drawable drawable = menu.findItem(menuId).getIcon();
		drawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTint(drawable, ContextCompat.getColor(App.CONTEXT, colorRes));
		menu.findItem(menuId).setIcon(drawable);
	}

	public static Drawable tintDrawable(@DrawableRes int drawableRes, @ColorRes int colorRes){
		final Drawable drawable = App.CONTEXT.getResources().getDrawable(drawableRes);
		drawable.setColorFilter(App.CONTEXT.getResources().getColor(colorRes), PorterDuff.Mode.SRC_ATOP);
		return drawable;
	}
}
