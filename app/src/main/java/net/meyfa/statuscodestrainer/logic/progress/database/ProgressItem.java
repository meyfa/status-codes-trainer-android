package net.meyfa.statuscodestrainer.logic.progress.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.DateTime;

/**
 * A database entity for progress items.
 */
@Entity
public class ProgressItem
{
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private DateTime dateTime;

    @ColumnInfo(name = "question_count")
    private int questionCount;

    @ColumnInfo(name = "correct_count")
    private int correctCount;

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;
    }

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }

    public int getQuestionCount()
    {
        return questionCount;
    }

    public void setQuestionCount(int questionCount)
    {
        this.questionCount = questionCount;
    }

    public int getCorrectCount()
    {
        return correctCount;
    }

    public void setCorrectCount(int correctCount)
    {
        this.correctCount = correctCount;
    }
}
