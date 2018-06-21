package net.meyfa.statuscodestrainer.data;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a collection of multiple status classes, and as such, of many different statuses.
 */
public class HTTPStatuses
{
    private final List<HTTPStatusClass> classes;

    /**
     * @param classes A list of all classes in this collection.
     */
    public HTTPStatuses(@NonNull Collection<HTTPStatusClass> classes)
    {
        this.classes = Collections.unmodifiableList(new ArrayList<>(classes));
    }

    /**
     * @return A read-only list of all classes in this collection.
     */
    @NonNull
    public List<HTTPStatusClass> getClasses()
    {
        return classes;
    }

    @Nullable
    public static HTTPStatuses load(@NonNull Resources res, @XmlRes int resourceId)
    {
        // get the XML resource
        XmlResourceParser parser = res.getXml(resourceId);
        Loader loader = new Loader(parser);

        // load
        HTTPStatuses result = null;
        try {
            result = loader.load();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Helper class for loading a statuses collection from an XML parser.
     */
    private static class Loader
    {
        private final XmlPullParser parser;

        private final List<HTTPStatusClass> resultClasses = new ArrayList<>();

        private int currentClassDigit;
        private String currentClassName;
        private List<HTTPStatus> currentClassStatuses;

        /**
         * @param parser The XML parser to load from.
         */
        public Loader(XmlPullParser parser)
        {
            this.parser = parser;
        }

        /**
         * Loads all statuses from the parser. This is a destructive action.
         *
         * @return The load result.
         * @throws XmlPullParserException If an XML error occurs.
         * @throws IOException If an I/O error occurs.
         */
        @NonNull
        public HTTPStatuses load() throws XmlPullParserException, IOException
        {
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                // only handle tag start events
                if (event != XmlPullParser.START_TAG) {
                    event = parser.next();
                    continue;
                }

                String tagName = parser.getName();
                if (tagName.equals("app-status-class")) {
                    processClass();
                } else if (tagName.equals("app-status")) {
                    processStatus();
                }

                // pull next event
                event = parser.next();
            }

            // all done; make sure to flush
            processEnd();

            return new HTTPStatuses(resultClasses);
        }

        private void processClass()
        {
            finishCurrentClass();

            currentClassDigit = Integer.parseInt(parser.getAttributeValue(null, "digit"));
            currentClassName = parser.getAttributeValue(null, "name");
            currentClassStatuses = new ArrayList<>();
        }

        private void processStatus()
        {
            int code = Integer.parseInt(parser.getAttributeValue(null, "code"));
            String message = parser.getAttributeValue(null, "message");

            currentClassStatuses.add(new HTTPStatus(code, message));
        }

        private void processEnd()
        {
            finishCurrentClass();
        }

        private void finishCurrentClass()
        {
            // finish editing the current status class
            if (currentClassStatuses != null) {
                resultClasses.add(new HTTPStatusClass(currentClassDigit, currentClassName, currentClassStatuses));

                currentClassDigit = 0;
                currentClassName = null;
                currentClassStatuses = null;
            }
        }
    }
}
