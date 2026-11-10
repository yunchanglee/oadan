package kr.co.oadan.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
import kr.co.oadan.data.model.WaterLog;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WaterLogDao_Impl implements WaterLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WaterLog> __insertionAdapterOfWaterLog;

  private final EntityDeletionOrUpdateAdapter<WaterLog> __deletionAdapterOfWaterLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WaterLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWaterLog = new EntityInsertionAdapter<WaterLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `water_logs` (`id`,`timestampUtc`,`amountMl`,`date`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestampUtc());
        statement.bindLong(3, entity.getAmountMl());
        statement.bindString(4, entity.getDate());
      }
    };
    this.__deletionAdapterOfWaterLog = new EntityDeletionOrUpdateAdapter<WaterLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `water_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM water_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final WaterLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWaterLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final WaterLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWaterLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
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
  public Flow<List<WaterLog>> getLogsForDate(final String date) {
    final String _sql = "SELECT * FROM water_logs WHERE date = ? ORDER BY timestampUtc ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_logs"}, new Callable<List<WaterLog>>() {
      @Override
      @NonNull
      public List<WaterLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfAmountMl = CursorUtil.getColumnIndexOrThrow(_cursor, "amountMl");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<WaterLog> _result = new ArrayList<WaterLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final int _tmpAmountMl;
            _tmpAmountMl = _cursor.getInt(_cursorIndexOfAmountMl);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item = new WaterLog(_tmpId,_tmpTimestampUtc,_tmpAmountMl,_tmpDate);
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
  public Flow<Integer> getTotalWaterForDate(final String date) {
    final String _sql = "SELECT SUM(amountMl) FROM water_logs WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_logs"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
  public Flow<List<WaterLog>> getAllWaterLogs() {
    final String _sql = "SELECT * FROM water_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_logs"}, new Callable<List<WaterLog>>() {
      @Override
      @NonNull
      public List<WaterLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfAmountMl = CursorUtil.getColumnIndexOrThrow(_cursor, "amountMl");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<WaterLog> _result = new ArrayList<WaterLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final int _tmpAmountMl;
            _tmpAmountMl = _cursor.getInt(_cursorIndexOfAmountMl);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item = new WaterLog(_tmpId,_tmpTimestampUtc,_tmpAmountMl,_tmpDate);
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
  public Object getAllWaterLogsOnce(final Continuation<? super List<WaterLog>> $completion) {
    final String _sql = "SELECT * FROM water_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WaterLog>>() {
      @Override
      @NonNull
      public List<WaterLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfAmountMl = CursorUtil.getColumnIndexOrThrow(_cursor, "amountMl");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<WaterLog> _result = new ArrayList<WaterLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final int _tmpAmountMl;
            _tmpAmountMl = _cursor.getInt(_cursorIndexOfAmountMl);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item = new WaterLog(_tmpId,_tmpTimestampUtc,_tmpAmountMl,_tmpDate);
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
