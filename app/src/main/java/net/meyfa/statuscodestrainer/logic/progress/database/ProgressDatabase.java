package net.meyfa.statuscodestrainer.logic.progress.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * A Room database for storing progress data.
 */
@Database(entities = { ProgressItem.class }, version = 1)
@TypeConverters({DateTimeConverter.class})
public abstract class ProgressDatabase extends RoomDatabase
{
    public abstract ProgressItemDao progressItemDao();
}
