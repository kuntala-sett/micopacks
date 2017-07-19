package dev.ukanth.iconmgr;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

/**
 * Created by ukanth on 17/7/17.
 */

public class IconPack {
    public String packageName;
    public String name;
    public String type;
    Resources iconPackres = null;
    HashSet<String> totalDraw = new HashSet<>();

    public int getCount() {
        return totalDraw.size();
    }


    public IconPack(String packageName, Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            XmlPullParser xpp = null;

            iconPackres = pm.getResourcesForApplication(packageName);
            int appfilter = iconPackres.getIdentifier("appfilter", "xml", packageName);
            if (appfilter > 0) {
                xpp = iconPackres.getXml(appfilter);
            } else {
                try {
                    InputStream appfilterstream = iconPackres.getAssets().open("appfilter.xml");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    xpp = factory.newPullParser();
                    xpp.setInput(appfilterstream, "utf-8");
                } catch (IOException e1) {
                }
            }
            if (xpp != null) {
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("item")) {
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                if (xpp.getAttributeName(i).equals("drawable")) {
                                    totalDraw.add(xpp.getAttributeValue(i));
                                }
                            }
                        }
                    }
                    eventType = xpp.next();
                }
            }
        } catch (PackageManager.NameNotFoundException | XmlPullParserException | IOException e) {
        }
    }

}