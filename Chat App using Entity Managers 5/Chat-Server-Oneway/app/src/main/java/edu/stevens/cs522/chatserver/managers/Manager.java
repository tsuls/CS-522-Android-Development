package edu.stevens.cs522.chatserver.managers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import edu.stevens.cs522.chatserver.async.AsyncContentResolver;
import edu.stevens.cs522.chatserver.async.IEntityCreator;
import edu.stevens.cs522.chatserver.async.IQueryListener;
import edu.stevens.cs522.chatserver.async.ISimpleQueryListener;
import edu.stevens.cs522.chatserver.async.QueryBuilder;
import edu.stevens.cs522.chatserver.async.SimpleQueryBuilder;


/**
 * Created by dduggan.
 */

public abstract class Manager<T> {

    protected final Context context;

    private final IEntityCreator<T> creator;

    protected final int loaderID;

    protected final String tag;

    protected Manager(Context context,
                      IEntityCreator<T> creator,
                      int loaderID) {
        this.context = context;
        this.creator = creator;
        this.loaderID = loaderID;
        this.tag = this.getClass().getCanonicalName();
    }

    private ContentResolver syncResolver;

    private AsyncContentResolver asyncResolver;

    protected ContentResolver getSyncResolver() {
        if (syncResolver == null)
            syncResolver = context.getContentResolver();
        return syncResolver;
    }

    protected AsyncContentResolver getAsyncResolver() {
        if (asyncResolver == null)
            asyncResolver = new AsyncContentResolver(context.getContentResolver());
        return asyncResolver;
    }

    // TODO Provide operations for executing queries (see lectures)

    protected void executeSimpleQuery(Uri uri,
                                      ISimpleQueryListener<T> listener) {
        // TODO
        SimpleQueryBuilder.executeQuery(tag, (Activity) context, uri, creator, listener);
    }

    protected void executeSimpleQuery(Uri uri,
                                      String[] projection,
                                      String selection,
                                      String[] selectionArgs,
                                      ISimpleQueryListener<T> listener) {
        // TODO
       SimpleQueryBuilder.executeQuery(tag, (Activity) context, uri, projection,  selection, selectionArgs, creator, listener);
    }

    protected void executeQuery(Uri uri,
                                IQueryListener<T> listener) {
        // TODO
        QueryBuilder.executeQuery(tag, (Activity) context, uri, loaderID, creator, listener);
    }

    protected void executeQuery(Uri uri,
                                String[] projection,
                                String selection,
                                String[] selectionArgs,
                                String order,
                                IQueryListener<T> listener) {
        // TODO
        QueryBuilder.executeQuery(tag, (Activity) context, uri, projection, selection, selectionArgs, order, loaderID, creator, listener);
    }

    protected void reexecuteQuery(Uri uri,
                                  IQueryListener<T> listener) {
        // TODO
        QueryBuilder.reexecuteQuery(tag, (Activity) context, uri, loaderID, creator, listener);
    }

    protected void reexecuteQuery(Uri uri,
                                  String[] projection,
                                  String selection,
                                  String[] selectionArgs,
                                  String order,
                                  IQueryListener<T> listener) {
        // TODO
        QueryBuilder.reexecuteQuery(tag, (Activity) context, uri, projection, selection, selectionArgs, order, loaderID, creator, listener);
    }

}