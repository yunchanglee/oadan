package kr.co.oadan.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kr.co.oadan.data.model.ConditionLog;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ConditionLogDao_Impl implements ConditionLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConditionLog> __insertionAdapterOfConditionLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ConditionLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConditionLog = new EntityInsertionAdapter<ConditionLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `condition_logs` (`id`,`timestampUtc`,`date`,`weightKg`,`skeletalMuscleKg`,`bodyFatPercent`,`conditionTag`,`memo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConditionLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestampUtc());
        statement.bindString(3, entity.getDate());
        if (entity.getWeightKg() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getWeightKg());
        }
        if (entity.getSkeletalMuscleKg() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getSkeletalMuscleKg());
        }
        if (entity.getBodyFatPercent() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getBodyFatPercent());
        }
        if (entity.getConditionTag() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getConditionTag());
        }
        if (entity.getMemo() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMemo());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM condition_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ConditionLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfConditionLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ConditionLog>> getLogsForDate(final String date) {
    final String _sql = "SELECT * FROM condition_logs WHERE date = ? ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"condition_logs"}, new Callable<List<ConditionLog>>() {
      @Override
      @NonNull
      public List<ConditionLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfSkeletalMuscleKg = CursorUtil.getColumnIndexOrThrow(_cursor, "skeletalMuscleKg");
          final int _cursorIndexOfBodyFatPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyFatPercent");
          final int _cursorIndexOfConditionTag = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionTag");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<ConditionLog> _result = new ArrayList<ConditionLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConditionLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Float _tmpSkeletalMuscleKg;
            if (_cursor.isNull(_cursorIndexOfSkeletalMuscleKg)) {
              _tmpSkeletalMuscleKg = null;
            } else {
              _tmpSkeletalMuscleKg = _cursor.getFloat(_cursorIndexOfSkeletalMuscleKg);
            }
            final Float _tmpBodyFatPercent;
            if (_cursor.isNull(_cursorIndexOfBodyFatPercent)) {
              _tmpBodyFatPercent = null;
            } else {
              _tmpBodyFatPercent = _cursor.getFloat(_cursorIndexOfBodyFatPercent);
            }
            final String _tmpConditionTag;
            if (_cursor.isNull(_cursorIndexOfConditionTag)) {
              _tmpConditionTag = null;
            } else {
              _tmpConditionTag = _cursor.getString(_cursorIndexOfConditionTag);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new ConditionLog(_tmpId,_tmpTimestampUtc,_tmpDate,_tmpWeightKg,_tmpSkeletalMuscleKg,_tmpBodyFatPercent,_tmpConditionTag,_tmpMemo);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<ConditionLog> getLatestWeightLog() {
    final String _sql = "SELECT * FROM condition_logs WHERE weightKg IS NOT NULL ORDER BY timestampUtc DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"condition_logs"}, new Callable<ConditionLog>() {
      @Override
      @Nullable
      public ConditionLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfSkeletalMuscleKg = CursorUtil.getColumnIndexOrThrow(_cursor, "skeletalMuscleKg");
          final int _cursorIndexOfBodyFatPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyFatPercent");
          final int _cursorIndexOfConditionTag = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionTag");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final ConditionLog _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Float _tmpSkeletalMuscleKg;
            if (_cursor.isNull(_cursorIndexOfSkeletalMuscleKg)) {
              _tmpSkeletalMuscleKg = null;
            } else {
              _tmpSkeletalMuscleKg = _cursor.getFloat(_cursorIndexOfSkeletalMuscleKg);
            }
            final Float _tmpBodyFatPercent;
            if (_cursor.isNull(_cursorIndexOfBodyFatPercent)) {
              _tmpBodyFatPercent = null;
            } else {
              _tmpBodyFatPercent = _cursor.getFloat(_cursorIndexOfBodyFatPercent);
            }
            final String _tmpConditionTag;
            if (_cursor.isNull(_cursorIndexOfConditionTag)) {
              _tmpConditionTag = null;
            } else {
              _tmpConditionTag = _cursor.getString(_cursorIndexOfConditionTag);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _result = new ConditionLog(_tmpId,_tmpTimestampUtc,_tmpDate,_tmpWeightKg,_tmpSkeletalMuscleKg,_tmpBodyFatPercent,_tmpConditionTag,_tmpMemo);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ConditionLog>> getAllConditionLogs() {
    final String _sql = "SELECT * FROM condition_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"condition_logs"}, new Callable<List<ConditionLog>>() {
      @Override
      @NonNull
      public List<ConditionLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfSkeletalMuscleKg = CursorUtil.getColumnIndexOrThrow(_cursor, "skeletalMuscleKg");
          final int _cursorIndexOfBodyFatPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyFatPercent");
          final int _cursorIndexOfConditionTag = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionTag");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<ConditionLog> _result = new ArrayList<ConditionLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConditionLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Float _tmpSkeletalMuscleKg;
            if (_cursor.isNull(_cursorIndexOfSkeletalMuscleKg)) {
              _tmpSkeletalMuscleKg = null;
            } else {
              _tmpSkeletalMuscleKg = _cursor.getFloat(_cursorIndexOfSkeletalMuscleKg);
            }
            final Float _tmpBodyFatPercent;
            if (_cursor.isNull(_cursorIndexOfBodyFatPercent)) {
              _tmpBodyFatPercent = null;
            } else {
              _tmpBodyFatPercent = _cursor.getFloat(_cursorIndexOfBodyFatPercent);
            }
            final String _tmpConditionTag;
            if (_cursor.isNull(_cursorIndexOfConditionTag)) {
              _tmpConditionTag = null;
            } else {
              _tmpConditionTag = _cursor.getString(_cursorIndexOfConditionTag);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new ConditionLog(_tmpId,_tmpTimestampUtc,_tmpDate,_tmpWeightKg,_tmpSkeletalMuscleKg,_tmpBodyFatPercent,_tmpConditionTag,_tmpMemo);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllConditionLogsOnce(
      final Continuation<? super List<ConditionLog>> $completion) {
    final String _sql = "SELECT * FROM condition_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ConditionLog>>() {
      @Override
      @NonNull
      public List<ConditionLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfSkeletalMuscleKg = CursorUtil.getColumnIndexOrThrow(_cursor, "skeletalMuscleKg");
          final int _cursorIndexOfBodyFatPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyFatPercent");
          final int _cursorIndexOfConditionTag = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionTag");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<ConditionLog> _result = new ArrayList<ConditionLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConditionLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Float _tmpSkeletalMuscleKg;
            if (_cursor.isNull(_cursorIndexOfSkeletalMuscleKg)) {
              _tmpSkeletalMuscleKg = null;
            } else {
              _tmpSkeletalMuscleKg = _cursor.getFloat(_cursorIndexOfSkeletalMuscleKg);
            }
            final Float _tmpBodyFatPercent;
            if (_cursor.isNull(_cursorIndexOfBodyFatPercent)) {
              _tmpBodyFatPercent = null;
            } else {
              _tmpBodyFatPercent = _cursor.getFloat(_cursorIndexOfBodyFatPercent);
            }
            final String _tmpConditionTag;
            if (_cursor.isNull(_cursorIndexOfConditionTag)) {
              _tmpConditionTag = null;
            } else {
              _tmpConditionTag = _cursor.getString(_cursorIndexOfConditionTag);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new ConditionLog(_tmpId,_tmpTimestampUtc,_tmpDate,_tmpWeightKg,_tmpSkeletalMuscleKg,_tmpBodyFatPercent,_tmpConditionTag,_tmpMemo);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
