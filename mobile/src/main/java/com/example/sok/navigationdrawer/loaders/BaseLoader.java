package com.example.sok.navigationdrawer.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.sok.navigationdrawer.api.response.RequestResult;
import com.example.sok.navigationdrawer.api.response.Response;

import java.io.IOException;
import java.sql.SQLException;

public abstract class BaseLoader extends AsyncTaskLoader<Response> {
    private static final String TAG = BaseLoader.class.getSimpleName();

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Response loadInBackground() {
        try {
            Response response = apiCall();
            if (response.getRequestResult() == RequestResult.SUCCESS) {
                try {
                    response.save(getContext());
                } catch (SQLException e) {
                    Log.d(TAG, "SQLException Can not save data to DB", e);
                }
                onSuccess();
            } else {
                onError();
            }
            return response;
        } catch (IOException e) {
            onError();
            return new Response();
        }
    }

    protected void onSuccess() {
    }

    protected void onError() {
    }

    protected abstract Response apiCall() throws IOException;
}

