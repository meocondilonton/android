package vn.philong.game.pikachunew;


import vn.philong.game.pikachunew.utility.LogUtils;
import vn.philong.game.pikachunew.utility.ResourceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmanit.lib.imagemanager.ImageManager;

public class CrossSaleManager {
	private static class CrossSale{
		public String title;
		public String imgIcon;
		public String mess;
		public String packageName;
		public String directLink;
	}
	public static void initUI(TextView tvTitle,TextView tvContent,final ImageView imgIcon){
		if(crossSale != null){
			tvTitle.setText(crossSale.title);
			tvContent.setText(Html.fromHtml(crossSale.mess));
			ImageManager imgManager = ImageManager.getInstance();
			imgManager.push(crossSale.imgIcon, imgIcon);
		}
	}
	private static CrossSale crossSale = null;
	public static boolean isCrossSale(){
		return crossSale != null;
	}
	public static void submit(Activity target){
		if(crossSale != null){
			String uri = crossSale.directLink;
			if(TextUtils.isEmpty(uri)){
				uri = "market://details?id="+crossSale.packageName;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(uri));
			LogUtils.e(CrossSaleManager.class, "uri:"+uri);
			target.startActivity(intent);
		}
	}
	public static void checkGameForPackageName(String title,String imageIcon,String mess,final String packageName,final String directLink,final int xu) {
		try {
			if(ResourceManager.isCrossSale(packageName)){
				PackageInfo pi = KaApp.getAppContext().getPackageManager().getPackageInfo(packageName, 0);
				ResourceManager.activeCrossSale(packageName, xu);
			}
			crossSale = null;
		} catch (Exception e) {
			crossSale = new CrossSale();
			LogUtils.e(CrossSaleManager.class, "get image icon:"+imageIcon);
			crossSale.directLink = directLink;
			crossSale.imgIcon = imageIcon;
			crossSale.mess = mess;
			crossSale.packageName = packageName;
			crossSale.directLink = directLink;
			crossSale.title = title;
			
		}
	}
}
