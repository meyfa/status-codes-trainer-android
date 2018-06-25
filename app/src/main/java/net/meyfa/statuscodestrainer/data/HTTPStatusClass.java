package net.meyfa.statuscodestrainer.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a class of HTTP response statuses (for example: the 4xx "Client Error" class).
 */
public class HTTPStatusClass
{
    private final int digit;
    private final String name;
    private final List<HTTPStatus> statuses;

    /**
     * @param digit    The leading digit of all status codes in this class (e.g. 4).
     * @param name     The name of this class (e.g. "Client Error").
     * @param statuses A list of all statuses in this class (e.g. 400, 401, 402, ...).
     */
    public HTTPStatusClass(int digit, @NonNull String name, @NonNull List<HTTPStatus> statuses)
    {
        this.digit = digit;
        this.name = name;
        this.statuses = Collections.unmodifiableList(new ArrayList<>(statuses));
    }

    /**
     * @return The leading digit of all status codes in this class.
     */
    public int getDigit()
    {
        return digit;
    }

    /**
     * @return The name of this class.
     */
    @NonNull
    public String getName()
    {
        return name;
    }

    /**
     * @return A read-only list of all statuses in this class.
     */
    @NonNull
    public List<HTTPStatus> getStatuses()
    {
        return statuses;
    }
}
