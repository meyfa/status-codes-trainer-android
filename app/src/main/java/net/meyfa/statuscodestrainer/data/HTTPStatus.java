package net.meyfa.statuscodestrainer.data;

import android.support.annotation.NonNull;

/**
 * Represents a HTTP response status, including a code and a message.
 */
public class HTTPStatus
{
    private final int code;
    private final String message;

    /**
     * @param code    The status code.
     * @param message The status message.
     */
    public HTTPStatus(int code, @NonNull String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * @return The status code.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return The status message.
     */
    @NonNull
    public String getMessage()
    {
        return message;
    }
}
