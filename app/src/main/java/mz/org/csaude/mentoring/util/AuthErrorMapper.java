package mz.org.csaude.mentoring.util;

import android.content.Context;

import mz.org.csaude.mentoring.R;

public final class AuthErrorMapper {

    private AuthErrorMapper() {}

    public static String map(Context context, String errorMsg) {

        if (errorMsg == null) {
            return context.getString(R.string.error_generic);
        }

        if (errorMsg.equalsIgnoreCase("Unauthorized")
                || errorMsg.contains("401")) {
            return context.getString(R.string.error_invalid_credentials);
        }

        if (errorMsg.contains("403")) {
            return context.getString(R.string.error_unauthorized);
        }

        if (errorMsg.toLowerCase().contains("timeout")
                || errorMsg.toLowerCase().contains("network")) {
            return context.getString(R.string.error_network);
        }

        return context.getString(R.string.error_generic);
    }
}
