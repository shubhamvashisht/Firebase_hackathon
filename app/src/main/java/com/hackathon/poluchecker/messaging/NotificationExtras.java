package com.hackathon.poluchecker.messaging;

/**
 * Created by shivesh on 23/2/17.
 */


import android.app.Notification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Method;

public final class NotificationExtras {

    // Method reference to Notification.Builder#makeContentView
    private static final Method MAKE_CONTENT_VIEW_METHOD;

    static {
        Method m = null;
        try {
            m = Notification.Builder.class.getDeclaredMethod("makeContentView");
            m.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MAKE_CONTENT_VIEW_METHOD = m;
    }

    private NotificationExtras() {
        // no instance
    }

	/* Public */

    public static Notification buildWithBackgroundResource(Context context, Notification.Builder builder, @DrawableRes int res) {
        if (MAKE_CONTENT_VIEW_METHOD == null) return buildNotification(builder);
        RemoteViews remoteViews = obtainRemoteViews(builder);
        Notification notification = buildNotification(builder);

        // Find the root of the content view and apply the background to it
        if (remoteViews != null) {
            View v = LayoutInflater.from(context).inflate(remoteViews.getLayoutId(), null);
            remoteViews.setInt(v.getId(), "setBackgroundResource", res);
        }

        return notification;
    }

    public static Notification buildWithBackgroundColor(Context context, Notification.Builder builder, @ColorInt int color) {
        if (MAKE_CONTENT_VIEW_METHOD == null) return buildNotification(builder);
        RemoteViews remoteViews = obtainRemoteViews(builder);
        Notification notification = buildNotification(builder);

        // Find the root of the content view and apply the color to it
        if (remoteViews != null) {
            View v = LayoutInflater.from(context).inflate(remoteViews.getLayoutId(), null);
            remoteViews.setInt(v.getId(), "setBackgroundColor", color);

            // Calculate a contrasting text color to ensure readability, and apply it to all TextViews within the notification layout
            boolean useDarkText = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114 > 186);
            int textColor = useDarkText ? 0xff000000 : 0xffffffff;
            applyTextColorToRemoteViews(remoteViews, v, textColor);
        }

        return notification;
    }

	/* Private */

    private static RemoteViews obtainRemoteViews(Notification.Builder builder) {
        try {
            // Explicitly force creation of the content view and re-assign it to the notification
            RemoteViews remoteViews = (RemoteViews) MAKE_CONTENT_VIEW_METHOD.invoke(builder);
            builder.setContent(remoteViews);
            return remoteViews;

        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Notification buildNotification(Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    private static void applyTextColorToRemoteViews(RemoteViews remoteViews, View view, int color) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0, count = vg.getChildCount(); i < count; i++) {
                applyTextColorToRemoteViews(remoteViews, vg.getChildAt(i), color);
            }
        } else if (view instanceof TextView) {
            remoteViews.setTextColor(view.getId(), color);
        }
    }
}
