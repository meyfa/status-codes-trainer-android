package net.meyfa.statuscodestrainer.logic.progress.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * A DAO for progress items.
 */
@Dao
public interface ProgressItemDao
{
    @Query("SELECT * FROM progressitem")
    List<ProgressItem> getAll();

    @Insert
    void insert(ProgressItem item);

    @Query("DELETE FROM progressitem")
    void clear();
}
