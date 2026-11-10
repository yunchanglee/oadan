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
import kr.co.oadan.data.model.FastSession;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FastSessionDao_Impl implements FastSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FastSession> __insertionAdapterOfFastSession;

  private final EntityDeletionOrUpdateAdapter<FastSession> __deletionAdapterOfFastSession;

  private final EntityDeletionOrUpdateAdapter<FastSession> __updateAdapterOfFastSession;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FastSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFastSession = new EntityInsertionAdapter<FastSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `fast_sessions` (`id`,`startTimeUtc`,`targetEndTimeUtc`,`actualEndTimeUtc`,`protocol`,`targetHours`,`fastedHours`,`isCompleted`,`isPartiallyCompleted`,`date`,`notes`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FastSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTimeUtc());
        statement.bindLong(3, entity.getTargetEndTimeUtc());
        if (entity.getActualEndTimeUtc() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getActualEndTimeUtc());
        }
        statement.bindString(5, entity.getProtocol());
        statement.bindDouble(6, entity.getTargetHours());
        if (entity.getFastedHours() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getFastedHours());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        final int _tmp_1 = entity.isPartiallyCompleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindString(10, entity.getDate());
        if (entity.getNotes() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNotes());
        }
        statement.bindLong(12, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfFastSession = new EntityDeletionOrUpdateAdapter<FastSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `fast_sessions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FastSession entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFastSession = new EntityDeletionOrUpdateAdapter<FastSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `fast_sessions` SET `id` = ?,`startTimeUtc` = ?,`targetEndTimeUtc` = ?,`actualEndTimeUtc` = ?,`protocol` = ?,`targetHours` = ?,`fastedHours` = ?,`isCompleted` = ?,`isPartiallyCompleted` = ?,`date` = ?,`notes` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FastSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTimeUtc());
        statement.bindLong(3, entity.getTargetEndTimeUtc());
        if (entity.getActualEndTimeUtc() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getActualEndTimeUtc());
        }
        statement.bindString(5, entity.getProtocol());
        statement.bindDouble(6, entity.getTargetHours());
        if (entity.getFastedHours() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getFastedHours());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        final int _tmp_1 = entity.isPartiallyCompleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindString(10, entity.getDate());
        if (entity.getNotes() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNotes());
        }
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM fast_sessions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FastSession session, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFastSession.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FastSession session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFastSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FastSession session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFastSession.handle(session);
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
  public Flow<FastSession> getActiveSession() {
    final String _sql = "SELECT * FROM fast_sessions WHERE actualEndTimeUtc IS NULL ORDER BY startTimeUtc DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fast_sessions"}, new Callable<FastSession>() {
      @Override
      @Nullable
      public FastSession call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final FastSession _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
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
  public Object getActiveSessionOnce(final Continuation<? super FastSession> $completion) {
    final String _sql = "SELECT * FROM fast_sessions WHERE actualEndTimeUtc IS NULL ORDER BY startTimeUtc DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FastSession>() {
      @Override
      @Nullable
      public FastSession call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final FastSession _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FastSession>> getRecentSessions(final int limit) {
    final String _sql = "SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fast_sessions"}, new Callable<List<FastSession>>() {
      @Override
      @NonNull
      public List<FastSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FastSession> _result = new ArrayList<FastSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FastSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
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
  public Flow<List<FastSession>> getAllSessions() {
    final String _sql = "SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"fast_sessions"}, new Callable<List<FastSession>>() {
      @Override
      @NonNull
      public List<FastSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FastSession> _result = new ArrayList<FastSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FastSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
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
  public Object getAllSessionsOnce(final Continuation<? super List<FastSession>> $completion) {
    final String _sql = "SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FastSession>>() {
      @Override
      @NonNull
      public List<FastSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FastSession> _result = new ArrayList<FastSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FastSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
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

  @Override
  public Object getSessionById(final long id, final Continuation<? super FastSession> $completion) {
    final String _sql = "SELECT * FROM fast_sessions WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FastSession>() {
      @Override
      @Nullable
      public FastSession call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeUtc");
          final int _cursorIndexOfTargetEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndTimeUtc");
          final int _cursorIndexOfActualEndTimeUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "actualEndTimeUtc");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTargetHours = CursorUtil.getColumnIndexOrThrow(_cursor, "targetHours");
          final int _cursorIndexOfFastedHours = CursorUtil.getColumnIndexOrThrow(_cursor, "fastedHours");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsPartiallyCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isPartiallyCompleted");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final FastSession _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTimeUtc;
            _tmpStartTimeUtc = _cursor.getLong(_cursorIndexOfStartTimeUtc);
            final long _tmpTargetEndTimeUtc;
            _tmpTargetEndTimeUtc = _cursor.getLong(_cursorIndexOfTargetEndTimeUtc);
            final Long _tmpActualEndTimeUtc;
            if (_cursor.isNull(_cursorIndexOfActualEndTimeUtc)) {
              _tmpActualEndTimeUtc = null;
            } else {
              _tmpActualEndTimeUtc = _cursor.getLong(_cursorIndexOfActualEndTimeUtc);
            }
            final String _tmpProtocol;
            _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            final float _tmpTargetHours;
            _tmpTargetHours = _cursor.getFloat(_cursorIndexOfTargetHours);
            final Float _tmpFastedHours;
            if (_cursor.isNull(_cursorIndexOfFastedHours)) {
              _tmpFastedHours = null;
            } else {
              _tmpFastedHours = _cursor.getFloat(_cursorIndexOfFastedHours);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsPartiallyCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPartiallyCompleted);
            _tmpIsPartiallyCompleted = _tmp_1 != 0;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new FastSession(_tmpId,_tmpStartTimeUtc,_tmpTargetEndTimeUtc,_tmpActualEndTimeUtc,_tmpProtocol,_tmpTargetHours,_tmpFastedHours,_tmpIsCompleted,_tmpIsPartiallyCompleted,_tmpDate,_tmpNotes,_tmpCreatedAt);
          } else {
            _result = null;
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
